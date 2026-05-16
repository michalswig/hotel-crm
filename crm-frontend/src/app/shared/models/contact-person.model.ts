import {CompanyRef} from './helpers';

export interface ContactPerson {
  id: number;
  firstName: string;
  lastName: string;
  position?: string;
  email?: string;
  phoneNumber?: string;
  createdAt?: string;
  updatedAt?: string;
  company?: CompanyRef;
  companyId?: number;
}
