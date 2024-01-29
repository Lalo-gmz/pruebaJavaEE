import { ICita } from 'app/entities/cita/cita.model';

export interface IDoctor {
  id: number;
  nombre?: string | null;
  apellidoPaterno?: string | null;
  apellidoMaterno?: string | null;
  especialidad?: string | null;
  citas?: ICita[] | null;
}

export type NewDoctor = Omit<IDoctor, 'id'> & { id: null };
