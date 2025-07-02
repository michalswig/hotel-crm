import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';


export interface User {
  id: number;
  username: string;
  role: string;
}


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly userSubject = new BehaviorSubject<User | null>(null);
  readonly user$: Observable<User | null> = this.userSubject.asObservable();

  setUser(user: User): void {
    this.userSubject.next(user);
    localStorage.setItem('user', JSON.stringify(user)); // optional
  }

  getUser(): User | null {
    return this.userSubject.value;
  }

  clearUser(): void {
    this.userSubject.next(null);
    localStorage.removeItem('user');
  }

  getCurrentUserId(): number | null {
    return this.userSubject.value?.id ?? null;
  }


}
