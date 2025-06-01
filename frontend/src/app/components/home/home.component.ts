import { Component } from '@angular/core';
import {FeedPanelComponent} from '../feed-panel/feed-panel.component';
import {LeftPanelComponent} from '../left-panel/left-panel.component';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [
    LeftPanelComponent,
    RouterOutlet
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
