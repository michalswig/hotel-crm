import {Routes} from '@angular/router';
import {LoginComponent} from './features/auth/login.component';
import {authGuard} from './core/auth/auth.guard';
import {DashboardLayoutComponent} from './features/dashboard/layout/dashboard-layout/dashboard-layout.component';
import {loginGuard} from './core/auth/login.guard';
import {DashboardComponent} from './features/dashboard/pages/dashboard/dashboard.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [loginGuard],
  },
  {
    path: 'dashboard',
    component: DashboardLayoutComponent,
    canActivate: [authGuard],
    children: [
      {path: '', component: DashboardComponent}
    ]
  },
  {
    path: 'dashboard',
    loadChildren: () =>
      import('./features/dashboard/dashboard.routes').then(m => m.DASHBOARD_ROUTES),
    canActivate: [authGuard]
  },
  {path: '**', redirectTo: 'login'}
];
