import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ContactPerson} from '../models/contact-person.model';
import {Observable} from 'rxjs';
import {Page} from '../models/helpers';

@Injectable({ providedIn: 'root' })
export class ContactPersonService {
  private readonly baseUrl = `${environment.apiUrl}/companies`;

  constructor(private http: HttpClient) {}

  create(companyId: number, body: Partial<ContactPerson>): Observable<ContactPerson> {
    return this.http.post<ContactPerson>(
      `${this.baseUrl}/${companyId}/contacts`, body, { withCredentials: true }
    );
  }

  list(companyId: number, page = 0, size = 10): Observable<Page<ContactPerson>> {
    const params = new HttpParams()
      .set('page', String(page))
      .set('size', String(size));

    return this.http.get<Page<ContactPerson>>(
      `${this.baseUrl}/${companyId}/contacts`,
      { params, withCredentials: true }
    );
  }

  update(companyId: number, contactId: number, body: Partial<ContactPerson>) {
    return this.http.put<ContactPerson>(
      `${this.baseUrl}/${companyId}/contacts/${contactId}`, body, { withCredentials: true }
    );
  }

  delete(companyId: number, contactId: number) {
    return this.http.delete<void>(
      `${this.baseUrl}/${companyId}/contacts/${contactId}`, { withCredentials: true }
    );
  }

}
