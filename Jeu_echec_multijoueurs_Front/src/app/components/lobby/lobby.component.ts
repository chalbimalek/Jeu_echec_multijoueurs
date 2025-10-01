import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { GameService } from '../../services/game.service';
import { Utilisateur } from '../../models/Utilisateur';
import { Invitation } from '../../models/Invitation';
import { Game } from '../../models/Game';
import { WebSocketService } from '../../services/web-socket.service';


@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent implements OnInit, OnDestroy {
  currentUsername: string = '';
  onlineUsers: Utilisateur[] = [];
  pendingInvitations: Invitation[] = [];
  sentInvitations: Set<string> = new Set();
  activeGame: Game | null = null;
  loading = false;
  private destroy$ = new Subject<void>();


    activeTab: 'lobby' | 'history' = 'lobby';
  gameHistory: Game[] = [];
  loadingHistory = false;
  constructor(
    private authService: AuthService,
    private gameService: GameService,
    private wsService: WebSocketService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.currentUsername = (await this.authService.getLoggedInUsername()) || '';

    this.wsService.connect();

    await this.wsService.waitForConnection();
    await this.wsService.subscribeToInvitations();
this.wsService.gameStart$
  .pipe(takeUntil(this.destroy$))
  .subscribe(game => {
    console.log('Partie démarrée:', game);
    this.router.navigate(['/game', game.id]);
  });
    this.loadOnlineUsers();
    this.loadPendingInvitations();
    this.checkActiveGame();

    this.wsService.invitations$
      .pipe(takeUntil(this.destroy$))
      .subscribe(invitation => {
        console.log('Invitation reçue dans le composant:', invitation);
        this.pendingInvitations.push(invitation);
      });

    this.wsService.invitationResponses$
      .pipe(takeUntil(this.destroy$))
      .subscribe(response => {
        console.log('Réponse reçue dans le composant:', response);
        if (response.accepted) {
          this.checkActiveGame();
        }
      });
}

loadGameHistory(): void {
    if (this.gameHistory.length > 0) return;

    this.loadingHistory = true;
    this.gameService.getUserGames().subscribe({
      next: (games) => {
        this.gameHistory = games;
        this.loadingHistory = false;
      },
      error: (err) => {
        console.error('Erreur chargement historique:', err);
        this.loadingHistory = false;
      }
    });
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }



  loadOnlineUsers(): void {
    this.gameService.getOnlineUsers().subscribe({
      next: (users) => {
        this.onlineUsers = users.filter(u => u.username !== this.currentUsername);
      },
      error: (err) => console.error('Erreur chargement utilisateurs:', err)
    });
  }

  loadPendingInvitations(): void {
    this.gameService.getPendingInvitations().subscribe({
      next: (invitations) => {
        this.pendingInvitations = invitations;
      },
      error: (err) => console.error('Erreur chargement invitations:', err)
    });
  }

  checkActiveGame(): void {
    this.gameService.getActiveGame().subscribe({
      next: (response) => {
        if (response.id) {
          this.activeGame = response as Game;
        }
      },
      error: (err) => console.error('Erreur vérification partie active:', err)
    });
  }

sendInvitation(username: string): void {
    console.log('Envoi invitation à:', username);
    this.sentInvitations.add(username);
    this.gameService.sendInvitation(username).subscribe({
      next: (response) => {
        console.log('Invitation envoyée, réponse:', response);  // AJOUTER
      },
      error: (err) => {
        console.error('Erreur envoi invitation:', err);
        this.sentInvitations.delete(username);
      }
    });
}

  acceptInvitation(invitation: Invitation): void {
    this.loading = true;
    this.gameService.acceptInvitation(invitation.id).subscribe({
      next: (game: any) => {
        this.pendingInvitations = this.pendingInvitations.filter(i => i.id !== invitation.id);
        this.loading = false;
        // La redirection se fera via WebSocket
      },
      error: (err) => {
        console.error('Erreur acceptation invitation:', err);
        this.loading = false;
      }
    });
}

  rejectInvitation(invitation: Invitation): void {
    this.gameService.rejectInvitation(invitation.id).subscribe({
      next: () => {
        this.pendingInvitations = this.pendingInvitations.filter(i => i.id !== invitation.id);
      },
      error: (err) => console.error('Erreur rejet invitation:', err)
    });
  }

  joinGame(): void {
    if (this.activeGame) {
      this.router.navigate(['/game', this.activeGame.id]);
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
