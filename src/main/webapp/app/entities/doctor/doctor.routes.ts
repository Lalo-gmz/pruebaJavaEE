import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DoctorComponent } from './list/doctor.component';
import { DoctorDetailComponent } from './detail/doctor-detail.component';
import { DoctorUpdateComponent } from './update/doctor-update.component';
import DoctorResolve from './route/doctor-routing-resolve.service';

const doctorRoute: Routes = [
  {
    path: '',
    component: DoctorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DoctorDetailComponent,
    resolve: {
      doctor: DoctorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DoctorUpdateComponent,
    resolve: {
      doctor: DoctorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DoctorUpdateComponent,
    resolve: {
      doctor: DoctorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default doctorRoute;
