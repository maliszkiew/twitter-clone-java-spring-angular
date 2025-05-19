import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { FeedPanelComponent } from './components/feed-panel/feed-panel.component';
import { ProfilePanelComponent } from './components/profile-panel/profile-panel.component';
import { SettingsPanelComponent } from './components/settings-panel/settings-panel.component';
import { authGuard } from './guards/auth.guard';
import { guestGuard } from './guards/guest.guard';

export const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [authGuard],
    children: [
      { path: 'feed', component: FeedPanelComponent },
      { path: 'profile', component: ProfilePanelComponent },
      { path: 'settings', component: SettingsPanelComponent },
      { path: '', redirectTo: 'feed', pathMatch: 'full' }
    ]
  },
  { path: 'register', component: RegisterComponent, canActivate: [guestGuard] },
  { path: 'login', component: LoginComponent, canActivate: [guestGuard] },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', redirectTo: '/home' }
];
