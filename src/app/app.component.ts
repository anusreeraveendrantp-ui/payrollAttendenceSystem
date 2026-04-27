import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  template: `
    <div class="app-layout" *ngIf="isLoggedIn(); else loginPage">
      <aside class="sidebar">
        <div class="sidebar-header">
          <div class="logo-icon">💼</div>
          <div class="logo-text">
            <span class="logo-title">Payroll</span>
            <span class="logo-sub">Management</span>
          </div>
        </div>
        <div class="user-info">
          <div class="user-avatar">{{ getUserInitial() }}</div>
          <div class="user-details">
            <span class="user-role">{{ getRole() }}</span>
            <span class="user-label">Logged in</span>
          </div>
        </div>
        <nav class="sidebar-nav">
          <a *ngIf="isAdmin()" routerLink="/employees" routerLinkActive="active" class="nav-item">
            <span class="nav-icon">👥</span><span class="nav-label">Employees</span>
          </a>
          <a routerLink="/attendance" routerLinkActive="active" class="nav-item">
            <span class="nav-icon">📅</span><span class="nav-label">Attendance</span>
          </a>
          <a routerLink="/leave" routerLinkActive="active" class="nav-item">
            <span class="nav-icon">🏖️</span><span class="nav-label">Leave</span>
          </a>
          <a routerLink="/payroll" routerLinkActive="active" class="nav-item">
            <span class="nav-icon">💰</span><span class="nav-label">Payroll</span>
          </a>
        </nav>
        <div class="sidebar-footer">
          <button class="logout-btn" (click)="logout()">
            <span class="nav-icon">🚪</span><span class="nav-label">Logout</span>
          </button>
        </div>
      </aside>
      <main class="main-content">
        <router-outlet></router-outlet>
      </main>
    </div>
    <ng-template #loginPage>
      <router-outlet></router-outlet>
    </ng-template>
  `,
  styles: [`
    .app-layout { display:flex; height:100vh; overflow:hidden; font-family:'Segoe UI',sans-serif; }
    .sidebar { width:220px; min-width:220px; background:#1a2332; display:flex; flex-direction:column; height:100vh; }
    .sidebar-header { display:flex; align-items:center; gap:10px; padding:20px 16px 16px; border-bottom:1px solid rgba(255,255,255,0.08); }
    .logo-icon { font-size:22px; background:#3b82f6; width:36px; height:36px; border-radius:8px; display:flex; align-items:center; justify-content:center; }
    .logo-text { display:flex; flex-direction:column; }
    .logo-title { color:white; font-size:15px; font-weight:700; line-height:1.2; }
    .logo-sub { color:#94a3b8; font-size:11px; }
    .user-info { display:flex; align-items:center; gap:10px; padding:14px 16px; border-bottom:1px solid rgba(255,255,255,0.08); }
    .user-avatar { width:34px; height:34px; border-radius:50%; background:#3b82f6; color:white; font-weight:700; font-size:14px; display:flex; align-items:center; justify-content:center; flex-shrink:0; }
    .user-details { display:flex; flex-direction:column; }
    .user-role { color:white; font-size:13px; font-weight:600; }
    .user-label { color:#64748b; font-size:11px; }
    .sidebar-nav { flex:1; padding:12px 10px; display:flex; flex-direction:column; gap:2px; overflow-y:auto; }
    .nav-item { display:flex; align-items:center; gap:10px; padding:10px 12px; border-radius:8px; color:#94a3b8; text-decoration:none; font-size:14px; font-weight:500; transition:all 0.15s; }
    .nav-item:hover { background:rgba(255,255,255,0.06); color:#e2e8f0; }
    .nav-item.active { background:#3b82f6; color:white; }
    .nav-icon { font-size:16px; width:20px; text-align:center; }
    .sidebar-footer { padding:12px 10px; border-top:1px solid rgba(255,255,255,0.08); }
    .logout-btn { display:flex; align-items:center; gap:10px; padding:10px 12px; border-radius:8px; color:#94a3b8; background:none; border:none; font-size:14px; font-weight:500; cursor:pointer; width:100%; transition:all 0.15s; }
    .logout-btn:hover { background:rgba(239,68,68,0.15); color:#f87171; }
    .main-content { flex:1; overflow-y:auto; background:#f1f5f9; padding:28px; }
  `]
})
export class AppComponent {
  constructor(private authService: AuthService, private router: Router) {}
  isLoggedIn(): boolean {
    // Don't show sidebar on login page even if token exists
    return this.authService.isAuthenticated() && !this.router.url.includes('/login');
  }
  isAdmin(): boolean { return this.authService.isAdmin(); }
  getRole(): string { return this.authService.getSystemRole() || 'User'; }
  getUserInitial(): string { return (this.authService.getSystemRole() || 'U').charAt(0).toUpperCase(); }
  logout(): void { this.authService.logout(); this.router.navigate(['/login']); }
}
