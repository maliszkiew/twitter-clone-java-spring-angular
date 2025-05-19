import { inject, Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, catchError, map, Observable, of, tap} from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl + '/auth';
  private registerUrl = this.apiUrl + '/register';
  private loginUrl = this.apiUrl + '/login';
  private logoutUrl = this.apiUrl + '/logout';
  private checkAuthUrl = this.apiUrl + '/status';

  private http = inject(HttpClient);
  private router = inject(Router);

  private isLoggedInSubject = new BehaviorSubject<boolean>(false);
  public isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor() {
    this.checkAuthStatus().subscribe();
  }

  register(username: string, password: string, passwordConfirmation: string): Observable<any> {
    return this.http.post(this.registerUrl, {username, password, passwordConfirmation}, { withCredentials: true });
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(this.loginUrl, {username, password}, { withCredentials: true })
      .pipe(tap(() => {
        this.isLoggedInSubject.next(true);
        this.router.navigate(['/home']);
      }));
  }

  logout(): Observable<any> {
    return this.http.post(this.logoutUrl, {}, { withCredentials: true })
      .pipe(tap(() => {
        this.isLoggedInSubject.next(false);
        this.router.navigate(['/login']);
      }));
  }

  checkAuthStatus(): Observable<boolean> {
    return this.http.get(this.checkAuthUrl, {
      withCredentials: true,
      observe: 'response',
    }).pipe(
      map(response => {
        const isLoggedIn = response.status >= 200 && response.status < 300;
        this.isLoggedInSubject.next(isLoggedIn);
        return isLoggedIn;
      }),
      catchError(() => {
        this.isLoggedInSubject.next(false);
        return of(false);
      })
    );
  }

  isLoggedIn(): boolean {
    return this.isLoggedInSubject.value;
  }
}
