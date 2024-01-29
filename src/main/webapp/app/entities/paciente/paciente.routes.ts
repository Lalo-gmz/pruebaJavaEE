import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PacienteComponent } from './list/paciente.component';
import { PacienteDetailComponent } from './detail/paciente-detail.component';
import { PacienteUpdateComponent } from './update/paciente-update.component';
import PacienteResolve from './route/paciente-routing-resolve.service';

const pacienteRoute: Routes = [
  {
    path: '',
    component: PacienteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PacienteDetailComponent,
    resolve: {
      paciente: PacienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PacienteUpdateComponent,
    resolve: {
      paciente: PacienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PacienteUpdateComponent,
    resolve: {
      paciente: PacienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pacienteRoute;
