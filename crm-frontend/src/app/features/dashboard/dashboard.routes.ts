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
      },
      {
        path: 'companies/:companyId/contacts',
        loadComponent: () =>
          import('./pages/contacts/contacts-list/contacts-list.component').then(m => m.ContactsListComponent)
      },
      {
        path: 'companies/:companyId/contacts/new',
        loadComponent: () =>
          import('./pages/contacts/add-contact/add-contact.component').then(m => m.AddContactComponent)
      },
      {
        path: 'companies/:companyId/contacts/:contactId/edit',
        loadComponent: () =>
          import('./pages/contacts/add-contact/add-contact.component').then(m => m.AddContactComponent)
      },
      {
        path: 'interactions',
        loadComponent: () =>
          import('./pages/interactions/interactions-list.component')
            .then(m => m.InteractionsListComponent),
        title: 'My Interactions'
      },
      {
        path: 'interactions/new',
        loadComponent: () =>
          import('./pages/interactions/add-interaction/add-interaction.component')
            .then(m => m.AddInteractionComponent),
        title: 'Schedule Interaction'
      },
      {
        path: 'interactions/:id/edit',
        loadComponent: () =>
          import('./pages/interactions/add-interaction/add-interaction.component')
            .then(m => m.AddInteractionComponent),
        title: 'Edit Interaction'
      },
      {
        path: 'interactions/:id',
        loadComponent: () =>
          import('./pages/interactions/view-interaction/interaction-detail.component')
            .then(m => m.InteractionDetailComponent),
        title: 'Interaction Details'
      },
      {
        path: 'users',
        loadComponent: () => import('./pages/users/users-list.component').then(m => m.UsersListComponent),
        title: 'Users (Admin)'
      },
      {
        path: 'users/new',
        loadComponent: () => import('./pages/users/add-user/add-user.component').then(m => m.AddUserComponent),
        title: 'Create User'
      },
      {
        path: 'users/:id/edit',
        loadComponent: () => import('./pages/users/add-user/add-user.component').then(m => m.AddUserComponent),
        title: 'Edit User'
      }

    ]
  }
];
