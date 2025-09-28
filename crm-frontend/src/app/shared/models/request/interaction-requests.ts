import { InteractionStatus, InteractionType } from '../enums';

export interface InteractionCreateRequest {
  type: InteractionType;
  companyId: number;
  contactPersonId: number;
  scheduledAt: string;
  notes?: string;
}

export interface InteractionUpdateRequest {
  type?: InteractionType;
  scheduledAt?: string;
  contactPersonId?: number;
  notes?: string;
  status?: InteractionStatus;
}

export interface InteractionCompleteRequest {
  notes?: string;
  followUpAt?: string;
}

export interface InteractionFilter {
  id?: number;
  type?: InteractionType;
  status?: InteractionStatus;
  companyId?: number;
  contactPersonId?: number;
  notes?: string;
  q?: string;

  scheduledFrom?: string;
  scheduledTo?: string;
  completedFrom?: string;
  completedTo?: string;
  followUpFrom?: string;
  followUpTo?: string;

  pendingOnly?: boolean;
  completedOnly?: boolean;
  withFollowUpOnly?: boolean;
}
