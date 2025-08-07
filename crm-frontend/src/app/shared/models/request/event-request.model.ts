export interface EventRequest {
  name: string;
  description?: string;
  type: string;
  status: string;
  eventDate: string;
  participantsNumber: number;
  estimatedTotalGrossRevenue: number;
  companyId: number;
  hotelId: number;
  createdByUserId: number;
}
