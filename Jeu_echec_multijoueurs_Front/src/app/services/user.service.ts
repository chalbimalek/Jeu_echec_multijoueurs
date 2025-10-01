import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { CreateUserRequest } from '../models/CreateUserRequest';
import { CreateUserResponse } from '../models/CreateUserResponse';
import { Utilisateur } from '../models/Utilisateur';



@Injectable({
  providedIn: 'root'
})
export class UserService {

 private authUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
  }

  getAllUsers(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.authUrl}/getAllUsers`, {
      headers: this.getAuthHeaders()
    });
  }

  // ✅ MÉTHODE MISE À JOUR pour créer un utilisateur avec groupe sélectionné
  createUser(user: CreateUserRequest): Observable<CreateUserResponse> {
    const payload = {
      username: user.username,
      email: user.email,
      prenom: user.prenom,
      nom: user.nom,
      password: user.password,

    };

    console.log('Payload envoyé au backend:', payload);

    return this.http.post<CreateUserResponse>(`${this.authUrl}/signup`, payload, {
      headers: this.getAuthHeaders()
    });
  }
}
