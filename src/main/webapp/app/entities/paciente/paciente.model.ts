import { ICita } from 'app/entities/cita/cita.model';

export interface IPaciente {
  id: number;
  nombre?: string | null;
  citas?: ICita[] | null;
}

export type NewPaciente = Omit<IPaciente, 'id'> & { id: null };
