import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, map, Observable, of} from 'rxjs';
import {User, UserService} from '../../shared/services/user.service';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly API_URL = 'http://localhost:8080/api/v1/auth';

  constructor(private readonly http: HttpClient, private readonly userService: UserService) {
  }

  login(credentials: { username: string; password: string }): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/login`, credentials, {withCredentials: true});
  }

  loadUserAfterLogin(): void {
    this.getCurrentUser().subscribe(user => {
      if (user) this.userService.setUser(user);
    });
  }

  isAuthenticated(): Observable<boolean> {
    return this.http.get(`${this.API_URL}/me`, {withCredentials: true}).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  getCurrentUser(): Observable<User | null> {
    return this.http.get<User>(
      `${this.API_URL}/me`,
      { withCredentials: true }
    ).pipe(
      catchError(() => of(null))
    );
  }

  logout(): void {
    this.http.post(`${this.API_URL}/logout`, {}, { withCredentials: true }).subscribe({
      next: () => {
        this.userService.clearUser();
        localStorage.setItem('justLoggedOut', 'true');
        location.href = '/login';
      },
      error: () => {
        this.userService.clearUser();
        location.href = '/login';
      }
    });
  }


}
