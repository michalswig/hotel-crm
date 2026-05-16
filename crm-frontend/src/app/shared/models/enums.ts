/* INDUSTRY ---------------------------------------------------- */
export enum Industry {
  HOSPITALITY       = 'HOSPITALITY',
  CORPORATE         = 'CORPORATE',
  EDUCATION         = 'EDUCATION',
  TRAVEL_AGENCY     = 'TRAVEL_AGENCY',
  EVENT_MANAGEMENT  = 'EVENT_MANAGEMENT',
  MEDIA             = 'MEDIA',
  GOVERNMENT        = 'GOVERNMENT',
  HEALTHCARE        = 'HEALTHCARE',
  SPORTS            = 'SPORTS',
  NGO               = 'NGO',
  TECHNOLOGY        = 'TECHNOLOGY',
  FINANCE           = 'FINANCE',
  REAL_ESTATE       = 'REAL_ESTATE',
  ENTERTAINMENT     = 'ENTERTAINMENT',
  OTHER             = 'OTHER'
}

/* EVENT ------------------------------------------------------- */
export enum EventStatus  { TENTATIVE = 'TENTATIVE', CONFIRMED = 'CONFIRMED', CANCELLED = 'CANCELLED', DONE = 'DONE' }
export enum EventType    { CONFERENCE = 'CONFERENCE', INCENTIVE = 'INCENTIVE', GALA_DINNER = 'GALA_DINNER', WORKSHOP = 'WORKSHOP', SITE_INSPECTION = 'SITE_INSPECTION', OTHER = 'OTHER' }

/* INTERACTION ------------------------------------------------- */
export enum InteractionType { CALL = 'CALL', EMAIL = 'EMAIL', MEETING = 'MEETING' }
export enum InteractionStatus { PLANNED = 'PLANNED', DONE = 'DONE', OVERDUE = 'OVERDUE', CANCELLED = 'CANCELLED' }

/* ROLE -------------------------------------------------------- */
export enum RoleType { ADMINISTRATOR = 'ADMINISTRATOR', MANAGER = 'MANAGER', SPECIALIST = 'SPECIALIST' }
