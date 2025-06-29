import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface CompanySummary {
  companyId: number;
  companyName: string;
  ownerUsername: string;
  lastContactDate: string; // or Date, if you want to convert it
  totalRevenueYTD: number;
  totalRevenueLY: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly API_URL = 'http://localhost:8080/api/v1/companies';

  constructor(private readonly http: HttpClient) {}

  getCompanySummary(): Observable<CompanySummary[]> {
    return this.http.get<CompanySummary[]>(`${this.API_URL}/summary`, {
      withCredentials: true
    });
  }

}
