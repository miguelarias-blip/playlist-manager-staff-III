import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export interface HttpErrorMessage {
  status: number;
  message: string;
}

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let message = 'Ocurrió un error inesperado';

      if (error.error && error.error.message) {
        message = error.error.message;
      } else {
        switch (error.status) {
          case 400:
            message = 'Solicitud inválida';
            break;
          case 401:
            message = 'No autorizado: credenciales inválidas';
            break;
          case 404:
            message = 'Recurso no encontrado';
            break;
          case 500:
            message = 'Error interno del servidor';
            break;
        }
      }

      const httpError: HttpErrorMessage = {
        status: error.status,
        message
      };

      return throwError(() => httpError);
    })
  );
};
