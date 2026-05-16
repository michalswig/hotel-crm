import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface RoleDto {
  id: number;
  name: string; // enum name
}

@Injectable({ providedIn: 'root' })
export class RolesService {
  private readonly baseUrl = `${environment.apiUrl}/roles`;
  constructor(private readonly http: HttpClient) {}

  list(): Observable<RoleDto[]> {
    return this.http.get<RoleDto[]>(this.baseUrl, { withCredentials: true });
  }
}
