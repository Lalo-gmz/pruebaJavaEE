import dayjs from 'dayjs/esm';
import { IDoctor } from 'app/entities/doctor/doctor.model';
import { IConsultorio } from 'app/entities/consultorio/consultorio.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';

export interface ICita {
  id: number;
  horarioConsultaInicio?: dayjs.Dayjs | null;
  horarioConsultaFin?: dayjs.Dayjs | null;
  doctor?: IDoctor | null;
  consultorio?: IConsultorio | null;
  paciente?: IPaciente | null;
}

export type NewCita = Omit<ICita, 'id'> & { id: null };
