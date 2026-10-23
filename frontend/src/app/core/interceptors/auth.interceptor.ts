import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = btoa(`${environment.basicUser}:${environment.basicPassword}`);
  const authReq = req.clone({
    setHeaders: {
      Authorization: `Basic ${token}`
    }
  });
  return next(authReq);
};
