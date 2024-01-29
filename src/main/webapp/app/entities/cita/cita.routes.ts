import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CitaComponent } from './list/cita.component';
import { CitaDetailComponent } from './detail/cita-detail.component';
import { CitaUpdateComponent } from './update/cita-update.component';
import CitaResolve from './route/cita-routing-resolve.service';
import { CitaConsultarComponent } from './cita-consultar/cita-consultar.component';

const citaRoute: Routes = [
  {
    path: '',
    component: CitaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'consultar',
    component: CitaConsultarComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CitaDetailComponent,
    resolve: {
      cita: CitaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CitaUpdateComponent,
    resolve: {
      cita: CitaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CitaUpdateComponent,
    resolve: {
      cita: CitaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default citaRoute;
