import {UserRef} from './helpers';
import {EventType} from '@angular/router';
import {EventStatus} from './enums';

export interface EventModel {
  id: number;
  name: string;
  description?: string;
  type: EventType;
  status: EventStatus;
  eventDate: string;
  participantsNumber: number;
  estimatedTotalGrossRevenue: number;
  createdAt: string;
  updatedAt: string;
  companyId: number;
  hotelId: number;
  createdByUserId: number;
  createdBy?: UserRef;
}
