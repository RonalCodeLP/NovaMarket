import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { from, switchMap } from 'rxjs';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);

  if (!auth.isReady() || !auth.isAuthenticated()) {
    return next(req);
  }

  return from(auth.updateTokenIfNeeded()).pipe(
    switchMap(() => {
      const token = auth.token();
      if (!token) {
        return next(req);
      }

      return next(req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      }));
    }),
  );
};
