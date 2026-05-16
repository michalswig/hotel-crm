import {InteractionStatus, InteractionType} from './enums';
import {ContactPerson} from './contact-person.model';
import {UserRef} from './helpers';

export interface Interaction {
  id: number;
  type: InteractionType;
  status?: InteractionStatus;
  companyId: number;
  companyName?: string;

  contactPersonId: number;
  contactPersonName?: string;

  userId: number;

  scheduledAt: string;
  completedAt?: string;
  followUpAt?: string;
  notes?: string;

  createdAt?: string;
  updatedAt?: string;
}

