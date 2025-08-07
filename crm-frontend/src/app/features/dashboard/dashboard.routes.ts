import {Routes} from '@angular/router';
import {DashboardLayoutComponent} from './layout/dashboard-layout/dashboard-layout.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {CompaniesListComponent} from './pages/companies-list/companies-list.component';
import {AddCompanyComponent} from './pages/add-company/add-company.component';
import {EventsListComponent} from './pages/events/events-list.component';
import {AddEventComponent} from './pages/events/add-event/add-event.component';

export const DASHBOARD_ROUTES: Routes = [
  {
    path: '',
    component: DashboardLayoutComponent,
    children: [
      {
        path: '',
        component: DashboardComponent,
        title: 'Dashboard Overview',
      },
      {
        path: 'companies',
        component: CompaniesListComponent,
        title: 'My Companies',
      },
      {
        path: 'companies/new',
        component: AddCompanyComponent,
        title: 'Add Company'
      },
      {
        path: 'companies/:id/edit',
        component: AddCompanyComponent,
        title: 'Edit Company'
      },
      {
        path: 'events', component: EventsListComponent, title: 'My Events'
      },
      {
        path: 'events/new',
        loadComponent: () => import('./pages/events/add-event/add-event.component').then(m => m.AddEventComponent),
        title: 'Add Event'
      },
      {
        path: 'events/:id/edit',
        component: AddEventComponent,
        title: 'Edit Event'
      }
    ]
  }
];
