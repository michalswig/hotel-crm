import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly API_URL = 'http://localhost:8080/api/v1/auth';

  constructor(private http: HttpClient) {}

  login(credentials: { username: string; password: string }): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/login`, credentials, { withCredentials: true });
  }

  isAuthenticated(): Observable<boolean> {
    return this.http.get(`${this.API_URL}/me`, { withCredentials: true }).pipe(
        map(() => true),
        catchError(() => of(false))
    );
  }

  logout(): void {
  }

}
