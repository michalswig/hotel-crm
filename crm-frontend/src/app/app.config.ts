import {APP_INITIALIZER, ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {jwtInterceptor} from './core/auth/jwt.interceptor';
import {AuthService} from './core/auth/auth.service';
import {MatDialogModule} from '@angular/material/dialog';

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(MatDialogModule),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([jwtInterceptor])
    ),
    {
      provide: APP_INITIALIZER,
      multi: true,
      deps: [AuthService],
      useFactory: (auth: AuthService) => () => {
        auth.loadUserAfterLogin();
        return true;
      }
    }
  ]
};
