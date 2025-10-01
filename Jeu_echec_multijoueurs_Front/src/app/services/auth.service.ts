import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, map, of, throwError } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/api';
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient, private router: Router) {}


    public getAuthHeaders(): HttpHeaders {
      const token = localStorage.getItem('accessToken');
      return new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      });
    }

  setToken(token: string): void {
    localStorage.setItem('accessToken', token);
  }
getCurrentUser(): Observable<any> {
  const token = localStorage.getItem('accessToken');
  return this.http.get<any>(`${this.baseUrl}/auth/me`, {
    headers: { Authorization: `Bearer ${token}` }
  });
}





  getToken(): string | null {
    return localStorage.getItem('accessToken');
  }
  login(loginRequest: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/signin`, loginRequest).pipe(
      map((response: any) => {
        if (response && response.accessToken) {
          localStorage.setItem('accessToken', response.accessToken);
          console.log('Token retrieved and stored successfully:', response.accessToken);
          console.log(this.getUserIdFromToken());


        } else {

          console.log('No token found in the response:', response);
        }
        return response;
      }),
      catchError((error) => {
        console.error('Error during login:', error);
        return throwError(error);
      })
    );
  }


logout(): void {
  console.log("Début de la déconnexion");

  const token = localStorage.getItem('accessToken');
  if (!token) {
    console.log("Aucun token trouvé, redirection directe");
    this.router.navigate(['/']);
    return;
  }

  const headers = {
    'Authorization': `Bearer ${token}`
  };

  this.http.post(`${this.baseUrl}/auth/logout`, {}, { headers }).subscribe({
    next: (response: any) => {
      console.log('Logout backend réussi:', response);
      localStorage.removeItem('accessToken');
      console.log("Jeton d'authentification supprimé avec succès");
      this.router.navigate(['/']);
      console.log("Redirection vers la page de connexion après déconnexion");
    },
    error: (err) => {
      console.error('Erreur lors du logout backend:', err);
      localStorage.removeItem('accessToken');
      this.router.navigate(['/']);
    }
  });
}


  isAuthenticated(): boolean {
    const token = localStorage.getItem('accessToken');
    console.log("isAuthenticated suceess");

    return !!token && !this.jwtHelper.isTokenExpired(token);
  }

  isUserLoggedIn(): boolean {
    console.log("boo true");

    return this.isAuthenticated();
  }
  getUserIdFromToken(): string | null {
    const token = localStorage.getItem('accessToken');
    if (token) {
      try {
        const decodedToken: any = jwt_decode(token);
        const userId = decodedToken.sub;
        console.log("l username :", userId);
        return userId;
      } catch (error) {
        console.error('Erreur lors du décryptage du token JWT :', error);
        return null;
      }
    } else {
      return null;
    }
  }
  getUserIdFromUsername(username: string): Observable<number> {
    return this.http.get<number>('http://localhost:8080/api/auth/getuser/id', {
      params: { username },
    });
  }
  async getLoggedInUsername(): Promise<string | null> {
    const token = this.getToken();
    if (token) {
      try {
        const decodedToken: any = this.jwtHelper.decodeToken(token);
        return decodedToken.username;
      } catch (error) {
        console.error('Erreur lors du décryptage du token JWT:', error);
        return null;
      }
    } else {
      return null;
    }
  }


}



function jwt_decode(token: string): any {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split('')
      .map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join('')
  );

  return JSON.parse(jsonPayload);
}
