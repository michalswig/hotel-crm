import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { map } from 'rxjs';

export const loginGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (localStorage.getItem('justLoggedOut')) {
    localStorage.removeItem('justLoggedOut');
    return true;
  }

  return authService.isAuthenticated().pipe(
    map(isAuth => {
      if (isAuth) {
        router.navigate(['/dashboard']);
        return false;
      }
      return true;
    })
  );
};

