import { Component, inject, ElementRef, ViewChild } from '@angular/core';
import {PostsService} from '../../services/posts.service';
import {PostComponent} from '../post/post.component';
import {CommonModule} from '@angular/common';
import {Post} from '../../models/Post';

@Component({
  selector: 'app-feed-panel',
  imports: [CommonModule, PostComponent],
  standalone: true,
  templateUrl: './feed-panel.component.html',
  styleUrl: './feed-panel.component.css'
})
export class FeedPanelComponent {
  posts: Post[] = [];
  page: number = 0;
  size: number = 10;
  loading: boolean = false;
  error: string | null = null;

  postService = inject(PostsService);
  constructor() {
    this.loadPosts();
  }

  loadPosts() {
    const scrollPosition = window.scrollY;

    this.loading = true;
    this.postService.getLatestPosts(this.page, this.size).subscribe({
      next: (response) => {
        this.updatePosts(response);
        this.page++;
        this.loading = false;

        setTimeout(() => {
          window.scrollTo(0, scrollPosition);
        }, 0);
      },
      error: (error) => {
        console.error('Error loading posts', error);
        this.loading = false;
      }
    });
  }

  updatePosts(newPosts: Post[]) {
    newPosts.forEach(post => {this.posts.push(post);})
  }

}
