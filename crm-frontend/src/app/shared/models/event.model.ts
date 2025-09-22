import { EventType, EventStatus } from './enums';
import { UserRef } from './helpers';

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
  contactPersonId?: number;
  contactPersonName?: string;
}
