import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'doctor',
    data: { pageTitle: 'Doctors' },
    loadChildren: () => import('./doctor/doctor.routes'),
  },
  {
    path: 'paciente',
    data: { pageTitle: 'Pacientes' },
    loadChildren: () => import('./paciente/paciente.routes'),
  },
  {
    path: 'consultorio',
    data: { pageTitle: 'Consultorios' },
    loadChildren: () => import('./consultorio/consultorio.routes'),
  },
  {
    path: 'cita',
    data: { pageTitle: 'Citas' },
    loadChildren: () => import('./cita/cita.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
