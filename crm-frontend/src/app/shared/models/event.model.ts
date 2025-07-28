import {EventStatus, EventType} from './enums';
import {CompanyRef, HotelRef, UserRef} from './helpers';

export interface Event {
  id: number;
  name: string;
  description?: string;
  type: EventType;
  status: EventStatus;
  participantsNumber: number;
  estimatedTotalGrossRevenue: string;   // keep BigDecimal as string
  hotel?: HotelRef;
  hotelId?: number;
  company?: CompanyRef;
  companyId?: number;
  createdBy?: UserRef;
  createdById?: number;
  eventDate: string;    // ISO
  createdAt?: string;
  updatedAt?: string;
}
