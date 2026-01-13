import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {

  getToken(): string | null {
    return this.getCookie('JWT');
  }

  getPayload(): any | null {
    const token = this.getToken();
    if (!token) return null;
    return this.decodeJwt(token);
  }

  isAuthenticated(): boolean {
    const payload = this.getPayload();
    if (!payload) return false;

    const now = Math.floor(Date.now() / 1000);
    return payload.exp > now && !payload.is_banned;
  }

  hasRole(role: string): boolean {
    const payload = this.getPayload();
    return payload?.role === role;
  }

  /* ---------- existing helpers ---------- */

  private getCookie(name: string): string | null {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? decodeURIComponent(match[2]) : null;
  }

  private decodeJwt(token: string): any {
    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch {
      return null;
    }
  }
}
