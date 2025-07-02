import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../../shared/models/helpers';
import {Company} from '../../shared/models/company.model';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private readonly API = 'http://localhost:8080/api/v1/companies';

  constructor(private readonly http: HttpClient) {
  }

  getMine(page = 0, size = 10): Observable<Page<Company>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<Page<Company>>(
      `${this.API}/filter`,
      { params, withCredentials: true }
    );
  }

}
