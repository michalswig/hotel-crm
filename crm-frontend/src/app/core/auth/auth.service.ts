import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly API_URL = 'http://localhost:8080/api/v1/auth';

  constructor(private readonly http: HttpClient) {}

  login(credentials: { username: string; password: string }): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/login`, credentials, { withCredentials: true });
  }

  isAuthenticated(): Observable<boolean> {
    return this.http.get(`${this.API_URL}/me`, { withCredentials: true }).pipe(
        map(() => true),
        catchError(() => of(false))
    );
  }

  getCurrentUser(): Observable<{ username: string; role: string } | null> {
    return this.http.get<{ username: string; role: string }>(
      `${this.API_URL}/me`,
      { withCredentials: true }
    ).pipe(
      catchError((err) => {
        return of(null);
      })
    );
  }

  logout(): void {
    this.http.post(`${this.API_URL}/logout`, {}, { withCredentials: true }).subscribe({
      next: () => {
        localStorage.clear();
        localStorage.setItem('justLoggedOut', 'true');
        location.href = '/login';
      },
      error: () => {
        localStorage.clear();
        location.href = '/login';
      }
    });
  }


}
