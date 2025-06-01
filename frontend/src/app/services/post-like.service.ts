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
export class PostLikeService {
  private apiUrl = environment.apiUrl + '/likes';
  private http = inject(HttpClient);

  private likeCountsSubject = new BehaviorSubject<Map<number, number>>(new Map<number, number>());
  public likeCounts$ = this.likeCountsSubject.asObservable();

  private likeStatusSubject = new BehaviorSubject<Map<number, boolean>>(new Map<number, boolean>());
  public likeStatus$ = this.likeStatusSubject.asObservable();

  constructor() { }

  likePost(postId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/post/${postId}`, {}, { withCredentials: true })
      .pipe(
        map(() => {
          this.updateLikeStatus(postId, true);
          this.incrementLikeCount(postId);
        })
      );
  }

  unlikePost(postId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/post/${postId}`, { withCredentials: true })
      .pipe(
        map(() => {
          this.updateLikeStatus(postId, false);
          this.decrementLikeCount(postId);
        })
      );
  }

  getLikeStatus(postId: number): Observable<boolean> {
    return this.http.get<LikeStatus>(`${this.apiUrl}/post/${postId}/status`, { withCredentials: true })
      .pipe(
        map(response => {
          this.updateLikeStatus(postId, response.liked);
          return response.liked;
        })
      );
  }

  getLikeCount(postId: number): Observable<number> {
    return this.http.get<LikeCount>(`${this.apiUrl}/post/${postId}/count`, { withCredentials: true })
      .pipe(
        map(response => {
          this.updateLikeCount(postId, response.count);
          return response.count;
        })
      );
  }

  private updateLikeStatus(postId: number, liked: boolean): void {
    const currentStatuses = new Map(this.likeStatusSubject.value);
    currentStatuses.set(postId, liked);
    this.likeStatusSubject.next(currentStatuses);
  }

  private updateLikeCount(postId: number, count: number): void {
    const currentCounts = new Map(this.likeCountsSubject.value);
    currentCounts.set(postId, count);
    this.likeCountsSubject.next(currentCounts);
  }

  private incrementLikeCount(postId: number): void {
    const currentCounts = new Map(this.likeCountsSubject.value);
    const currentCount = currentCounts.get(postId) || 0;
    currentCounts.set(postId, currentCount + 1);
    this.likeCountsSubject.next(currentCounts);
  }

  private decrementLikeCount(postId: number): void {
    const currentCounts = new Map(this.likeCountsSubject.value);
    const currentCount = currentCounts.get(postId) || 0;
    if (currentCount > 0) {
      currentCounts.set(postId, currentCount - 1);
      this.likeCountsSubject.next(currentCounts);
    }
  }

  isPostLiked(postId: number): boolean {
    return this.likeStatusSubject.value.get(postId) || false;
  }

  getPostLikeCount(postId: number): number {
    return this.likeCountsSubject.value.get(postId) || 0;
  }

  toggleLike(postId: number): Observable<void> {
    if (this.isPostLiked(postId)) {
      return this.unlikePost(postId);
    } else {
      return this.likePost(postId);
    }
  }
}
