import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';

interface LikeStatus {
  liked: boolean;
}

interface LikeCount {
  count: number;
}

@Injectable({
  providedIn: 'root'
})
export class CommentLikeService {
  private apiUrl = environment.apiUrl + '/comments/likes';
  private http = inject(HttpClient);

  private likeCountsSubject = new BehaviorSubject<Map<number, number>>(new Map<number, number>());
  public likeCounts$ = this.likeCountsSubject.asObservable();

  private likeStatusSubject = new BehaviorSubject<Map<number, boolean>>(new Map<number, boolean>());
  public likeStatus$ = this.likeStatusSubject.asObservable();

  constructor() { }

  likeComment(commentId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${commentId}`, {}, { withCredentials: true })
      .pipe(
        map(() => {
          this.updateLikeStatus(commentId, true);
          this.incrementLikeCount(commentId);
        })
      );
  }

  unlikeComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${commentId}`, { withCredentials: true })
      .pipe(
        map(() => {
          this.updateLikeStatus(commentId, false);
          this.decrementLikeCount(commentId);
        })
      );
  }

  getLikeStatus(commentId: number): Observable<boolean> {
    return this.http.get<LikeStatus>(`${this.apiUrl}/${commentId}/status`, { withCredentials: true })
      .pipe(
        map(response => {
          this.updateLikeStatus(commentId, response.liked);
          return response.liked;
        })
      );
  }

  getLikeCount(commentId: number): Observable<number> {
    return this.http.get<LikeCount>(`${this.apiUrl}/${commentId}/count`, { withCredentials: true })
      .pipe(
        map(response => {
          this.updateLikeCount(commentId, response.count);
          return response.count;
        })
      );
  }

  private updateLikeStatus(commentId: number, liked: boolean): void {
    const currentStatuses = new Map(this.likeStatusSubject.value);
    currentStatuses.set(commentId, liked);
    this.likeStatusSubject.next(currentStatuses);
  }

  private updateLikeCount(commentId: number, count: number): void {
    const currentCounts = new Map(this.likeCountsSubject.value);
    currentCounts.set(commentId, count);
    this.likeCountsSubject.next(currentCounts);
  }

  private incrementLikeCount(commentId: number): void {
    const currentCounts = new Map(this.likeCountsSubject.value);
    const currentCount = currentCounts.get(commentId) || 0;
    currentCounts.set(commentId, currentCount + 1);
    this.likeCountsSubject.next(currentCounts);
  }

  private decrementLikeCount(commentId: number): void {
    const currentCounts = new Map(this.likeCountsSubject.value);
    const currentCount = currentCounts.get(commentId) || 0;
    if (currentCount > 0) {
      currentCounts.set(commentId, currentCount - 1);
      this.likeCountsSubject.next(currentCounts);
    }
  }

  isCommentLiked(commentId: number): boolean {
    return this.likeStatusSubject.value.get(commentId) || false;
  }

  getCommentLikeCount(commentId: number): number {
    return this.likeCountsSubject.value.get(commentId) || 0;
  }

  toggleLike(commentId: number): Observable<void> {
    if (this.isCommentLiked(commentId)) {
      return this.unlikeComment(commentId);
    } else {
      return this.likeComment(commentId);
    }
  }
}
