import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const jwt = getCookie('JWT');
  console.log(jwt);
  

  if (!jwt) return router.parseUrl('/login'); // no token

  const payload = decodeJwt(jwt);
  console.log(payload);
  
  if (!payload) return router.parseUrl('/login'); // invalid token

 console.log("ban");

  if (payload.is_banned) return router.parseUrl('/login'); 




  const now = Math.floor(Date.now() / 1000); 
  if (payload.exp < now) return router.parseUrl('/login');
console.log("hi");

  return true; // everything is valid
};
export const roleGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  // 1. Get required role from route data
  const requiredRole = route.data['role'] as string;

  // 2. Get JWT from cookie
  const jwt = getCookie('JWT');
  if (!jwt) return router.parseUrl('/login');
  console.log(jwt, 'fdf');
  

  // 3. Decode JWT
  const payload = decodeJwt(jwt);
  if (!payload) return router.parseUrl('/login');

  // 4. Check role
  if (payload.role !== requiredRole) {
    return router.parseUrl('/login'); // user cannot enter
  }

  return true; 
};
// Helper functions
function getCookie(name: string): string | null {
  const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
  return match ? decodeURIComponent(match[2]) : null;
}

function decodeJwt(token: string): any {
  try {
    const payload = token.split('.')[1];
    const decoded = atob(payload);
    return JSON.parse(decoded);
  } catch (e) {
    return null;
  }
}
export function getCurrentUsername(): string | null {
  const jwt = getCookie('JWT');
  if (!jwt) return null;

  const payload = decodeJwt(jwt);
  if (!payload) return null;

  // Adjust depending on your backend claim name
  return payload.sub || payload.username || null;
}

