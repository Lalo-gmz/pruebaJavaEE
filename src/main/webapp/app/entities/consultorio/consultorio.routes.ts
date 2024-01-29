import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ConsultorioComponent } from './list/consultorio.component';
import { ConsultorioDetailComponent } from './detail/consultorio-detail.component';
import { ConsultorioUpdateComponent } from './update/consultorio-update.component';
import ConsultorioResolve from './route/consultorio-routing-resolve.service';

const consultorioRoute: Routes = [
  {
    path: '',
    component: ConsultorioComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsultorioDetailComponent,
    resolve: {
      consultorio: ConsultorioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsultorioUpdateComponent,
    resolve: {
      consultorio: ConsultorioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsultorioUpdateComponent,
    resolve: {
      consultorio: ConsultorioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default consultorioRoute;
