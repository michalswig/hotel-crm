import {
  HttpInterceptorFn,
  HttpRequest,
  HttpHandlerFn,
  HttpErrorResponse
} from '@angular/common/http';
import { inject } from '@angular/core';
import {
  catchError,
  switchMap,
  throwError,
  BehaviorSubject,
  filter,
  take
} from 'rxjs';
import { AuthService } from './auth.service';

const AUTH_PATHS = ['/auth/login', '/auth/refresh-token', '/auth/logout'];

let isRefreshing = false;

const refreshDone$ = new BehaviorSubject<boolean | null>(null);

export const jwtInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
) => {
  const authService = inject(AuthService);

  const isAuthRequest = AUTH_PATHS.some(path => req.url.includes(path));
  if (isAuthRequest) {
    return next(req);
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {

      if (error.status !== 401) {
        return throwError(() => error);
      }

      if (isRefreshing) {
        return refreshDone$.pipe(
          filter(result => result !== null),
          take(1),
          switchMap(success => {
            if (success) {
              return next(req);
            }
            return throwError(() => error);
          })
        );
      }

      isRefreshing = true;
      refreshDone$.next(null);

      return authService.refreshToken().pipe(
        switchMap(() => {
          isRefreshing = false;
          refreshDone$.next(true);
          return next(req);
        }),
        catchError(() => {
          isRefreshing = false;
          refreshDone$.next(false);
          authService.logout();
          return throwError(() => error);
        })
      );
    })
  );
};
