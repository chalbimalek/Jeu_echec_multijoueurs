import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AuthService } from '../services/auth.service';
import { Game } from '../models/Game';
import { Move } from '../models/Move';
import { Utilisateur } from '../models/Utilisateur';
import { Invitation } from '../models/Invitation';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getOnlineUsers(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.apiUrl}/users/online`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  sendInvitation(toUsername: string): Observable<Invitation> {
    return this.http.post<Invitation>(
      `${this.apiUrl}/invitations/send`,
      { toUsername },
      { headers: this.authService.getAuthHeaders() }
    );
  }

  getPendingInvitations(): Observable<Invitation[]> {
    return this.http.get<Invitation[]>(
      `${this.apiUrl}/invitations/pending`,
      { headers: this.authService.getAuthHeaders() }
    );
  }

  acceptInvitation(invitationId: number): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/invitations/${invitationId}/accept`,
      {},
      { headers: this.authService.getAuthHeaders() }
    );
  }

  rejectInvitation(invitationId: number): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/invitations/${invitationId}/reject`,
      {},
      { headers: this.authService.getAuthHeaders() }
    );
  }

  createGame(opponent: string): Observable<Game> {
    return this.http.post<Game>(
      `${this.apiUrl}/games/create`,
      { opponent },
      { headers: this.authService.getAuthHeaders() }
    );
  }

  getActiveGame(): Observable<Game | any> {
    return this.http.get<Game>(
      `${this.apiUrl}/games/active`,
      { headers: this.authService.getAuthHeaders() }
    );
  }

  getGameMoves(gameId: number): Observable<Move[]> {
    return this.http.get<Move[]>(
      `${this.apiUrl}/games/${gameId}/moves`,
      { headers: this.authService.getAuthHeaders() }
    );
  }

  getUserGames(): Observable<Game[]> {
    return this.http.get<Game[]>(
      `${this.apiUrl}/games/history`,
      { headers: this.authService.getAuthHeaders() }
    );
  }
}
