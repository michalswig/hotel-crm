import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {EventRequest} from '../models/request/event-request.model';
import {Observable} from 'rxjs';
import {Page} from '../models/helpers';
import {EventModel} from '../models/event.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private readonly API_URL = '/api/v1/events';

  constructor(private http: HttpClient) {}

  createEvent(request: EventRequest): Observable<Event> {
    return this.http.post<Event>(this.API_URL, request);
  }

  getEvents(page: number, size: number): Observable<Page<EventModel>> {
    return this.http.get<Page<EventModel>>(`${this.API_URL}?page=${page}&size=${size}`);
  }

  getEventById(id: number): Observable<Event> {
    return this.http.get<Event>(`${this.API_URL}/${id}`);
  }

  updateEvent(id: number, request: EventRequest): Observable<Event> {
    return this.http.put<Event>(`${this.API_URL}/${id}`, request);
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
