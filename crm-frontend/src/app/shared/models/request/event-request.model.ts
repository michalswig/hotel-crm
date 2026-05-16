import {EventStatus, EventType} from '../enums';

export interface EventRequest {
  name: string;
  description?: string;
  type: EventType;            // ← use enums, not string
  status: EventStatus;        // ← use enums, not string
  eventDate: string;          // 'YYYY-MM-DDTHH:mm' from <input type="datetime-local">
  participantsNumber?: number;
  estimatedTotalGrossRevenue?: number;
  companyId: number;
  contactPersonId?: number | 0; // ← NEW (0 on UPDATE means “clear contact”)
}
