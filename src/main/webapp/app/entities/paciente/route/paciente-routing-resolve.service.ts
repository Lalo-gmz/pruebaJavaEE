import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaciente } from '../paciente.model';
import { PacienteService } from '../service/paciente.service';

export const pacienteResolve = (route: ActivatedRouteSnapshot): Observable<null | IPaciente> => {
  const id = route.params['id'];
  if (id) {
    return inject(PacienteService)
      .find(id)
      .pipe(
        mergeMap((paciente: HttpResponse<IPaciente>) => {
          if (paciente.body) {
            return of(paciente.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default pacienteResolve;
