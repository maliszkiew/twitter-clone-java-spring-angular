import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { PostsService } from '../../services/posts.service';
import { User } from '../../models/User';

@Component({
  selector: 'app-post-creator',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './post-creator.component.html',
  styleUrl: './post-creator.component.css'
})
export class PostCreatorComponent implements OnInit {
  content: string = '';
  isSubmitting: boolean = false;
  user: User | null = null;

  private authService = inject(AuthService);
  private postsService = inject(PostsService);

  ngOnInit() {
    this.user = this.authService.currentUser;
    this.authService.currentUser$.subscribe(user => {
      this.user = user;
    });
  }

  clearPost() {
    this.content = '';
  }

  submitPost() {
    if (!this.content.trim() || this.isSubmitting || !this.user) {
      return;
    }

    this.postsService.createPost(this.content).subscribe({
      next: (newPost) => {
        this.content = '';
        this.isSubmitting = false;
      },
      error: (error) => {
        console.error('Error creating post:', error);
        this.isSubmitting = false;
      }
    });
  }
}
