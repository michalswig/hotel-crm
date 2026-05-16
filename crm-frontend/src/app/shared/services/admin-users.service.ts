import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AdminUser {
  id: number;
  username: string;
  hotelId: number | null;
  hotelName: string | null;
  roleId: number | null;
  role: string | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateUserRequest {
  userName: string;
  password: string;
  hotelId: number;
  roleId: number;
}

export interface UpdateUserRequest {
  userName: string;
  hotelId: number;
  roleId: number;
}

@Injectable({ providedIn: 'root' })
export class AdminUsersService {
  private readonly baseUrl = `${environment.apiUrl}/users`;
  constructor(private readonly http: HttpClient) {}

  list(): Observable<AdminUser[]> {
    return this.http.get<AdminUser[]>(this.baseUrl, { withCredentials: true });
  }

  getById(id: number): Observable<AdminUser> {
    return this.http.get<AdminUser>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  create(req: CreateUserRequest): Observable<AdminUser> {
    return this.http.post<AdminUser>(this.baseUrl, req, { withCredentials: true });
  }

  update(id: number, req: UpdateUserRequest): Observable<AdminUser> {
    return this.http.put<AdminUser>(`${this.baseUrl}/${id}`, req, { withCredentials: true });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }
}
