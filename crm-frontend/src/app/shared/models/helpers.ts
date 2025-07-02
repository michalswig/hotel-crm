// src/app/shared/models/helpers.ts

/** Generic type used for dropdowns, ref lookups, etc. */
export interface IdNamePair {
  id: number;
  name: string;
}

// Refs used inside other models (avoid deep nesting)
export type CompanyRef = IdNamePair;
export type HotelRef   = IdNamePair;
export type UserRef    = IdNamePair;

/** Pagination */
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number; // current page
  size: number;
}

/** Form request types (used in services and reactive forms) */
import { Event } from './event.model';
import { ContactPerson } from './contact-person.model';

export type EventCreateRequest = Omit<
  Event,
  'id' | 'createdAt' | 'updatedAt' | 'hotel' | 'company' | 'createdBy'
>;

export type EventUpdateRequest = Partial<EventCreateRequest>;

export type ContactCreateRequest = Omit<
  ContactPerson,
  'id' | 'createdAt' | 'updatedAt' | 'company'
>;
