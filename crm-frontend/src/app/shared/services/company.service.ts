import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../models/helpers';
import {Company} from '../models/company.model';
import {CompanyRequest} from '../models/request/company-request.model';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private readonly baseUrl = `${environment.apiUrl}/companies`;

  constructor(private readonly http: HttpClient) {
  }

  getFilteredCompanies(page = 0, size = 10, nameFilter = ''): Observable<Page<Company>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if(nameFilter){
      params = params.set('name', nameFilter)
    }

    return this.http.get<Page<Company>>(
      `${this.baseUrl}/filter`,
      { params, withCredentials: true }
    );
  }

  createCompany(request: CompanyRequest): Observable<Company> {
    return this.http.post<Company>(`${this.baseUrl}`, request, { withCredentials: true });
  }

  getCompanyById(id: number): Observable<Company> {
    return this.http.get<Company>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  updateCompany(id: number, request: CompanyRequest): Observable<Company> {
    return this.http.put<Company>(`${this.baseUrl}/${id}`, request, { withCredentials: true });
  }

  deleteCompany(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

}
