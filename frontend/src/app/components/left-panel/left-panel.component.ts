import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-left-panel',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './left-panel.component.html',
  styleUrl: './left-panel.component.css'
})
export class LeftPanelComponent {
  private authService = inject(AuthService);

  logout(event: Event): void {
    event.preventDefault();
    this.authService.logout().subscribe();
  }
}
