import { Component, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Post } from '../../models/Post';
import { CommentService } from '../../services/comment.service';
import { Comment } from '../../models/Comment';
import { CommentComponent } from '../comment/comment.component';
import { FormsModule } from '@angular/forms';
import { PostLikeService } from '../../services/post-like.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule, CommentComponent, FormsModule],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent implements OnInit {
  @Input() post!: Post;

  comments: Comment[] = [];
  showComments: boolean = false;
  commentContent: string = '';
  loading: boolean = false;
  currentPage: number = 0;
  pageSize: number = 5;

  likeCount: number = 0;
  isLiked: boolean = false;
  likeLoading: boolean = false;

  private commentService = inject(CommentService);
  private likeService = inject(PostLikeService);
  private authService = inject(AuthService);

  ngOnInit(): void {
    this.loadLikeData();
  }

  loadLikeData(): void {
    if (this.authService.isLoggedIn) {
      this.likeService.getLikeStatus(this.post.id).subscribe({
        next: (liked) => {
          this.isLiked = liked;
        },
        error: (error) => {
          console.error('Error getting like status', error);
        }
      });
    }

    this.likeService.getLikeCount(this.post.id).subscribe({
      next: (count) => {
        this.likeCount = count;
      },
      error: (error) => {
        console.error('Error getting like count', error);
      }
    });
  }

  toggleLike(): void {
    if (!this.authService.isLoggedIn) {
      return;
    }

    this.likeLoading = true;

    this.likeService.toggleLike(this.post.id).subscribe({
      next: () => {
        this.isLiked = this.likeService.isPostLiked(this.post.id);
        this.likeCount = this.likeService.getPostLikeCount(this.post.id);
        this.likeLoading = false;
      },
      error: (error) => {
        console.error('Error toggling like', error);
        this.likeLoading = false;
      }
    });
  }

  toggleComments(): void {
    this.showComments = !this.showComments;
    if (this.showComments && this.comments.length === 0) {
      this.loadComments();
    }
  }

  loadComments(): void {
    this.loading = true;
    this.commentService.getCommentsByPostId(this.post.id, this.currentPage, this.pageSize)
      .subscribe({
        next: (comments) => {
          this.comments = [...this.comments, ...comments];
          this.currentPage++;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading comments', error);
          this.loading = false;
        }
      });
  }

  submitComment(): void {
    if (!this.commentContent.trim()) return;

    this.commentService.createComment(this.post.id, this.commentContent)
      .subscribe({
        next: (newComment) => {
          this.comments.unshift(newComment);
          this.commentContent = '';
        },
        error: (error) => {
          console.error('Error creating comment', error);
        }
      });
  }

  handleDeleteComment(commentId: number): void {
    this.commentService.deleteComment(commentId)
      .subscribe({
        next: () => {
          this.comments = this.comments.filter(comment => comment.id !== commentId);
        },
        error: (error) => {
          console.error('Error deleting comment', error);
        }
      });
  }
}
