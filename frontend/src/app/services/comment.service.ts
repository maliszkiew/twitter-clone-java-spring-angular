import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Comment } from '../models/Comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = environment.apiUrl + '/comments';
  private http = inject(HttpClient);

  constructor() { }

  getCommentsByPostId(postId: number, page: number = 0, size: number = 10): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/post/${postId}`, {
      params: {
        page: page.toString(),
        size: size.toString()
      },
      withCredentials: true
    });
  }

  createComment(postId: number, content: string): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}/post/${postId}`,
      { content: content },
      { withCredentials: true }
    );
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${commentId}`,
      { withCredentials: true }
    );
  }

}
