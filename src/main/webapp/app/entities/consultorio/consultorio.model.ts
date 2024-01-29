import { ICita } from 'app/entities/cita/cita.model';

export interface IConsultorio {
  id: number;
  numero?: string | null;
  piso?: string | null;
  citas?: ICita[] | null;
}

export type NewConsultorio = Omit<IConsultorio, 'id'> & { id: null };
