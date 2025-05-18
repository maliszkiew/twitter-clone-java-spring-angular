import {inject, Injectable} from '@angular/core';
import {environment} from 'src/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl + '/auth';
  private registerUrl = this.apiUrl + '/register';
  private loginUrl = this.apiUrl + '/login';
  private http = inject(HttpClient);

  constructor() {
  }

  register(username: string, password: string, passwordConfirmation: string): Observable<any> {
    return this.http.post(this.registerUrl, {username, password, passwordConfirmation});
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(this.loginUrl, {username, password});
  }

}
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
