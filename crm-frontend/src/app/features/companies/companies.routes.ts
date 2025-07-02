import { Routes } from '@angular/router';
import { CompaniesListComponent } from './list/companies-list.component';

export const COMPANY_ROUTES: Routes = [
  {
    path: '',
    component: CompaniesListComponent,
    title: 'Company List'
  }
];
