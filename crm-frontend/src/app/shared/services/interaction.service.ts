import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {
  InteractionCompleteRequest,
  InteractionCreateRequest,
  InteractionFilter,
  InteractionUpdateRequest
} from '../models/request/interaction-requests';
import {Observable} from 'rxjs';
import {Page} from '../models/helpers';
import {Interaction} from '../models/interaction.model';

@Injectable({ providedIn: 'root' })
export class InteractionService {
  private readonly baseUrl = `${environment.apiUrl}/interactions`;

  constructor(private readonly http: HttpClient) {}

  schedule(req: InteractionCreateRequest): Observable<Interaction> {
    return this.http.post<Interaction>(this.baseUrl, req, { withCredentials: true });
  }

  filter(filter: InteractionFilter, page = 0, size = 10): Observable<Page<Interaction>> {
    let params = new HttpParams().set('page', page).set('size', size);
    Object.entries(filter || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}`.trim() !== '') params = params.set(k, `${v}`);
    });
    return this.http.get<Page<Interaction>>(`${this.baseUrl}/filter`, { params, withCredentials: true });
  }

  getById(id: number): Observable<Interaction> {
    return this.http.get<Interaction>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  update(id: number, req: InteractionUpdateRequest): Observable<Interaction> {
    return this.http.put<Interaction>(`${this.baseUrl}/${id}`, req, { withCredentials: true });
  }

  complete(id: number, req: InteractionCompleteRequest): Observable<Interaction> {
    return this.http.patch<Interaction>(`${this.baseUrl}/${id}/complete`, req, { withCredentials: true });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  calendar(from: string, to: string, page = 0, size = 10): Observable<Page<Interaction>> {
    let params = new HttpParams().set('from', from).set('to', to).set('page', page).set('size', size);
    return this.http.get<Page<Interaction>>(`${this.baseUrl}/calendar`, { params, withCredentials: true });
  }

  upcomingFollowUps(): Observable<Interaction[]> {
    return this.http.get<Interaction[]>(`${this.baseUrl}/follow-ups`, { withCredentials: true });
  }
}
