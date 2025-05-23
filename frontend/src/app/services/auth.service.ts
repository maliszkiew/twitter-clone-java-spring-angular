import { inject, Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, catchError, map, Observable, of, tap } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../models/User';

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

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    this.checkAuthStatus().subscribe();
  }

  public register(username: string, password: string): Observable<any> {
    return this.http.post(this.registerUrl, { username, password }, {withCredentials: true}).pipe(
      tap((response: any) => {
        this.isLoggedInSubject.next(true);
        this.updateCurrentUser(response);
      }),
      catchError(error => {
        console.error('Registration error:', error);
        throw error;
      })
    );
  }

  public login(username: string, password: string): Observable<any> {
    return this.http.post(this.loginUrl, { username, password }, {withCredentials: true}).pipe(
      tap((response: any) => {
        this.isLoggedInSubject.next(true);
        this.updateCurrentUser(response);
      }),
      catchError(error => {
        console.error('Login error:', error);
        throw error;
      })
    );
  }

  public logout(): Observable<any> {
    return this.http.post(this.logoutUrl, {}, {withCredentials: true}).pipe(
      tap(() => {
        this.isLoggedInSubject.next(false);
        this.currentUserSubject.next(null);
        this.router.navigate(['/login']);
      }),
      catchError(error => {
        console.error('Logout error:', error);
        this.isLoggedInSubject.next(false);
        this.currentUserSubject.next(null);
        throw error;
      })
    );
  }

  public checkAuthStatus(): Observable<boolean> {
    return this.http.get(this.checkAuthUrl, {withCredentials: true}).pipe(
      map((response: any) => {
        console.log('Auth status response:', response);
        if (response && (response.id || (response.user && response.user.id))) {
          this.isLoggedInSubject.next(true);
          this.updateCurrentUser(response.user || response);
          return true;
        } else {
          this.isLoggedInSubject.next(false);
          this.currentUserSubject.next(null);
          return false;
        }
      }),
      catchError((error) => {
        console.error('Auth status check error:', error);
        this.isLoggedInSubject.next(false);
        this.currentUserSubject.next(null);
        return of(false);
      })
    );
  }

  private updateCurrentUser(userData: any): void {
    if (!userData) return;

    console.log('Updating user data:', userData);

    this.currentUserSubject.next({
      id: userData.id,
      username: userData.username,
      userRole: userData.userRole,
      bio: userData.bio,
      postsCount: userData.postsCount
    });
  }

  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }

  get isLoggedIn(): boolean {
    return this.isLoggedInSubject.value;
  }
}
