export interface CompanyRequest {
  name: string;
  taxId?: string;
  industry: string;
  email?: string;
  phoneNumber?: string;
  website?: string;
  address?: string;
  postalCode?: string;
  city?: string;
  country?: string;
  createdByUserId: number;
}
