import { Component, Input, inject, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Comment } from '../../models/Comment';
import { AuthService } from '../../services/auth.service';
import { CommentLikeService } from '../../services/comment-like.service';

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent implements OnInit {
  @Input() comment!: Comment;
  @Output() delete = new EventEmitter<number>();

  likeCount: number = 0;
  isLiked: boolean = false;
  likeLoading: boolean = false;

  private authService = inject(AuthService);
  private commentLikeService = inject(CommentLikeService);

  ngOnInit(): void {
    this.loadLikeData();
  }

  get isCommentAuthor(): boolean {
    return this.authService.currentUser?.id === this.comment.authorId;
  }

  deleteComment(): void {
    this.delete.emit(this.comment.id);
  }

  loadLikeData(): void {
    if (this.authService.isLoggedIn) {
      this.commentLikeService.getLikeStatus(this.comment.id).subscribe({
        next: (liked) => {
          this.isLiked = liked;
        },
        error: (error) => {
          console.error('Error getting comment like status', error);
        }
      });
    }

    this.commentLikeService.getLikeCount(this.comment.id).subscribe({
      next: (count) => {
        this.likeCount = count;
      },
      error: (error) => {
        console.error('Error getting comment like count', error);
      }
    });
  }

  toggleLike(): void {
    if (!this.authService.isLoggedIn) {
      return;
    }

    this.likeLoading = true;

    this.commentLikeService.toggleLike(this.comment.id).subscribe({
      next: () => {
        this.isLiked = this.commentLikeService.isCommentLiked(this.comment.id);
        this.likeCount = this.commentLikeService.getCommentLikeCount(this.comment.id);
        this.likeLoading = false;
      },
      error: (error) => {
        console.error('Error toggling comment like', error);
        this.likeLoading = false;
      }
    });
  }
}
