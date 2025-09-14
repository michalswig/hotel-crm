import {Industry} from './enums';
import {UserRef} from './helpers';
import {ContactPerson} from './contact-person.model';

export interface Company {
  id: number;
  name: string;
  taxId?: string;
  industry?: Industry;
  email?: string;
  phoneNumber?: string;
  website?: string;
  address?: string;
  postalCode?: string;
  city?: string;
  country?: string;
  createdBy?: UserRef;
  createdByUserId: number;
  createdAt?: string;
  updatedAt?: string;
  contactPersons?: ContactPerson[];
  events?: Event[];

  primaryContactId?: number;
  primaryContactName?: string;
  primaryContactEmail?: string;
}
