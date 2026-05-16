import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment'; // ← dodajemy import

export interface CompanySummary {
  companyId: number;
  companyName: string;
  ownerUsername: string;
  lastContactDate: string;
  totalRevenueYTD: number;
  totalRevenueLY: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly API_URL = `${environment.apiUrl}/companies`;

  constructor(private readonly http: HttpClient) {}

  getCompanySummary(): Observable<CompanySummary[]> {
    return this.http.get<CompanySummary[]>(`${this.API_URL}/summary`, {
      withCredentials: true
    });
  }
}
