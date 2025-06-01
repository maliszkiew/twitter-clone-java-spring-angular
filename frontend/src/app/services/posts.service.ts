import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Post } from '../models/Post';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PostsService {
  private apiUrl = environment.apiUrl + '/post';
  private latestPostsUrl = this.apiUrl + '/latest';

  private newPostSubject = new Subject<Post>();
  public newPost$ = this.newPostSubject.asObservable();

  private http = inject(HttpClient);

  constructor() { }

  getLatestPosts(page: number, size: number): Observable<Post[]> {
    return this.http.get<Post[]>(this.latestPostsUrl, {
      params: {
        page: page.toString(),
        size: size.toString()
      },
      withCredentials: true
    });
  }

  getUserPosts(username: string, page: number, size: number): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.apiUrl}/user/${username}`, {
      params: {
        page: page.toString(),
        size: size.toString()
      },
      withCredentials: true
    });
  }

  createPost(content: string): Observable<Post> {
    return this.http.post<Post>(this.apiUrl, { content: content }, { withCredentials: true })
      .pipe(
        tap(newPost => {
          this.newPostSubject.next(newPost);
        })
      );
  }
}
