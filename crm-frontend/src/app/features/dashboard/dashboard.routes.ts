import { Routes } from '@angular/router';
import { DashboardLayoutComponent } from './layout/dashboard-layout/dashboard-layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { CompaniesListComponent } from './pages/companies-list/companies-list.component';
import {AddCompanyComponent} from './pages/add-company/add-company.component';

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
      }
    ]
  }
];
