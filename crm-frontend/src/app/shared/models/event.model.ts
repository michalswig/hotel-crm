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
  hotelId: number;            // still returned by API
  createdByUserId: number;    // still returned by API
  createdBy?: UserRef;
  contactPersonId?: number;   // ← already present
  contactPersonName?: string; // ← already present
}
