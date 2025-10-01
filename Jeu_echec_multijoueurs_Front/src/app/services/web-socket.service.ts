import { Injectable } from '@angular/core';
import { RxStomp, RxStompConfig } from '@stomp/rx-stomp';
import { Subject } from 'rxjs';
import { AuthService } from './auth.service';
import { Invitation } from '../models/Invitation';
import { Move } from '../models/Move';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private rxStomp: RxStomp;
  private connected = false;

  private moveSubject = new Subject<Move>();
  private invitationSubject = new Subject<Invitation>();
  private invitationResponseSubject = new Subject<any>();
  private gameFinishSubject = new Subject<any>();

  public moves$ = this.moveSubject.asObservable();
  public invitations$ = this.invitationSubject.asObservable();
  public invitationResponses$ = this.invitationResponseSubject.asObservable();
  public gameFinish$ = this.gameFinishSubject.asObservable();

  constructor(private authService: AuthService) {
    this.rxStomp = new RxStomp();
  }

connect(): void {
    if (this.connected) return;

    const config: RxStompConfig = {
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      connectHeaders: {
        Authorization: `Bearer ${this.authService.getToken()}`
      },
      heartbeatIncoming: 0,
      heartbeatOutgoing: 20000,
      reconnectDelay: 5000,
      debug: (msg: string) => {
        console.log('STOMP: ' + msg);
      }
    };

    this.rxStomp.configure(config);
    this.rxStomp.activate();

    // Utiliser l'observable de connexion
    this.rxStomp.connected$.subscribe(() => {
      console.log('WebSocket connecté!');
      this.connected = true;
    });
}
waitForConnection(): Promise<void> {
    return new Promise((resolve) => {
      if (this.rxStomp.connected()) {
        resolve();
      } else {
        const subscription = this.rxStomp.connected$.subscribe(() => {
          subscription.unsubscribe();
          resolve();
        });
      }
    });
}
  disconnect(): void {
    if (this.rxStomp) {
      this.rxStomp.deactivate();
      this.connected = false;
    }
  }

  subscribeToGame(gameId: number): void {
    this.rxStomp.watch(`/topic/game/${gameId}`).subscribe((message: any) => {
      const move = JSON.parse(message.body) as Move;
      this.moveSubject.next(move);
    });

    this.rxStomp.watch(`/topic/game/${gameId}/finish`).subscribe((message: any) => {
      const data = JSON.parse(message.body);
      this.gameFinishSubject.next(data);
    });
  }

private gameStartSubject = new Subject<any>();
public gameStart$ = this.gameStartSubject.asObservable();

async subscribeToInvitations(): Promise<void> {
    const username = await this.authService.getLoggedInUsername();

    if (!username) {
      console.error('Username non disponible pour la souscription WebSocket');
      return;
    }

    console.log('Souscription aux invitations pour:', username);

    this.rxStomp.watch(`/user/queue/invitations`).subscribe((message: any) => {
      console.log('Invitation reçue via WebSocket:', message.body);
      const invitation = JSON.parse(message.body) as Invitation;
      this.invitationSubject.next(invitation);
    });

    this.rxStomp.watch(`/user/queue/invitation-response`).subscribe((message: any) => {
      console.log('Réponse invitation reçue via WebSocket:', message.body);
      const response = JSON.parse(message.body);
      this.invitationResponseSubject.next(response);
    });

    // AJOUTER CECI
    this.rxStomp.watch(`/user/queue/game-start`).subscribe((message: any) => {
      console.log('Partie démarrée via WebSocket:', message.body);
      const game = JSON.parse(message.body);
      this.gameStartSubject.next(game);
    });
}

  sendMove(gameId: number, move: Move): void {
    this.rxStomp.publish({
      destination: `/app/game/${gameId}/move`,
      body: JSON.stringify(move)
    });
  }

  finishGame(gameId: number, winner: string): void {
    this.rxStomp.publish({
      destination: `/app/game/${gameId}/finish`,
      body: JSON.stringify({ winner })
    });
  }

  isConnected(): boolean {
    return this.connected;
  }
}
