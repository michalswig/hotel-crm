import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {EventRequest} from '../models/request/event-request.model';
import {Observable} from 'rxjs';
import {Page} from '../models/helpers';
import {EventModel} from '../models/event.model';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private readonly baseUrl = `${environment.apiUrl}/events`;

  constructor(private readonly http: HttpClient) {}

  createEvent(request: EventRequest): Observable<EventModel> {
    return this.http.post<EventModel>(this.baseUrl, request, { withCredentials: true });
  }

  getEvents(page: number, size: number): Observable<Page<EventModel>> {
    return this.http.get<Page<EventModel>>(this.baseUrl, {
      params: new HttpParams().set('page', page).set('size', size),
      withCredentials: true
    });
  }

  getFilteredEvents(filter: any, page: number, size: number): Observable<Page<EventModel>> {
    let params = new HttpParams().set('page', page).set('size', size);
    Object.entries(filter || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}`.trim() !== '') params = params.set(k, `${v}`);
    });
    return this.http.get<Page<EventModel>>(`${this.baseUrl}/filter`, { params, withCredentials: true });
  }

  getEventById(id: number): Observable<EventModel> {
    return this.http.get<EventModel>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  updateEvent(id: number, request: EventRequest): Observable<EventModel> {
    return this.http.put<EventModel>(`${this.baseUrl}/${id}`, request, { withCredentials: true });
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }
}
