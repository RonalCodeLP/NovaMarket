import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = async (_route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (await auth.ensureAuthenticated()) {
    return true;
  }

  await router.navigate(['/auth'], {
    queryParams: { returnUrl: state.url },
  });
  return false;
};
