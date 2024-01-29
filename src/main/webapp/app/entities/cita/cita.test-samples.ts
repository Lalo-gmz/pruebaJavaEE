import dayjs from 'dayjs/esm';

import { ICita, NewCita } from './cita.model';

export const sampleWithRequiredData: ICita = {
  id: 10669,
};

export const sampleWithPartialData: ICita = {
  id: 21633,
};

export const sampleWithFullData: ICita = {
  id: 13792,
  horarioConsultaInicio: dayjs('2024-01-28T20:37'),
  horarioConsultaFin: dayjs('2024-01-29T12:11'),
};

export const sampleWithNewData: NewCita = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
