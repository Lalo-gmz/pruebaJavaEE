import { Routes } from '@angular/router';

import ErrorComponent from './error.component';

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    title: '¡Página de error!',
  },
  {
    path: 'accessdenied',
    component: ErrorComponent,
    data: {
      errorMessage: 'No tiene permisos para acceder a la página.',
    },
    title: '¡Página de error!',
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      errorMessage: 'La página no existe.',
    },
    title: '¡Página de error!',
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];
