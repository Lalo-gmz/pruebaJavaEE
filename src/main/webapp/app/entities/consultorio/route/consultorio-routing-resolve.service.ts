import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsultorio } from '../consultorio.model';
import { ConsultorioService } from '../service/consultorio.service';

export const consultorioResolve = (route: ActivatedRouteSnapshot): Observable<null | IConsultorio> => {
  const id = route.params['id'];
  if (id) {
    return inject(ConsultorioService)
      .find(id)
      .pipe(
        mergeMap((consultorio: HttpResponse<IConsultorio>) => {
          if (consultorio.body) {
            return of(consultorio.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default consultorioResolve;
