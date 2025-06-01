import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PostComponent } from '../post/post.component';
import { PostsService } from '../../services/posts.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/User';
import {PostCreatorComponent} from '../post-creator/post-creator.component';
import {Post} from '../../models/Post';
@Component({
  selector: 'app-profile-panel',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    PostComponent,
    PostCreatorComponent
  ],
  templateUrl: './profile-panel.component.html',
  styleUrl: './profile-panel.component.css'
})
export class ProfilePanelComponent implements OnInit {
  posts: Post[] = [];
  page: number = 0;
  size: number = 10;
  loading: boolean = false;
  error: string | null = null;
  user: User | null = null;

  authService = inject(AuthService);
  postService = inject(PostsService);

  ngOnInit() {
    this.user = this.authService.currentUser;

    this.authService.currentUser$.subscribe(user => {
      this.user = user;
      if (user) {
        this.loadPosts();
      }
    });

    this.postService.newPost$.subscribe(newPost => {
      this.loadPosts();
    })
  }

  loadPosts() {
    if (!this.user) return;

    this.loading = true;
    this.postService.getUserPosts(this.user.username, this.page, this.size).subscribe({
      next: (response) => {
        this.posts = response;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading posts', error);
        this.loading = false;
      }
    });
  }

  logout() {
    this.authService.logout().subscribe();
  }
}
