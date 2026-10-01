import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import { AuthStateService } from '../services/auth-state.service';

export const authGuard: CanActivateFn = (_route, state) => {
  const authState = inject(AuthStateService);
  const router = inject(Router);
  console.log('[AuthGuard] Checking authentication for route:', state.url);
  return authState.validateSession().pipe(

    map((sessionStatus) => {
      console.log('[AuthGuard] Session status:', sessionStatus.status);
      if (sessionStatus.status === 'authenticated') return true;
      // if (sessionStatus.status === 'forbidden') return router.createUrlTree(['/403']);
      // if (sessionStatus.status === 'unavailable') return router.createUrlTree(['/401']);
      return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
    }),
  );
};

export const guestGuard: CanActivateFn = () => {
  const authState = inject(AuthStateService);
  const router = inject(Router);
  console.log('[GuestGuard] Checking guest access');
  return authState.validateSession().pipe(
    map((sessionStatus) => {
      console.log('[GuestGuard] Session status:', sessionStatus.status);
      if (sessionStatus.status !== 'unauthenticated') return router.createUrlTree(['/catalog']);
      // if (sessionStatus.status === 'forbidden') return router.createUrlTree(['/403']);
      // if (sessionStatus.status === 'unavailable') return router.createUrlTree(['/401']);
      return true;
    }),
  );
};

