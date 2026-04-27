import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  const requiredRole = route.data['role'];
  
  if (requiredRole === 'ADMIN' && !authService.isAdmin()) {
    // Redirect EMPLOYEE users trying to access admin pages
    router.navigate(['/attendance']);
    return false;
  }

  return true;
};
