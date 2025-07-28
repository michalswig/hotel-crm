import {InteractionType} from './enums';
import {ContactPerson} from './contact-person.model';
import {UserRef} from './helpers';

export interface Interaction {
  id: number;
  type: InteractionType;
  notes?: string;
  interactionDate: string;
  contactPerson?: ContactPerson;
  contactPersonId?: number;
  user?: UserRef;
  userId?: number;
}

