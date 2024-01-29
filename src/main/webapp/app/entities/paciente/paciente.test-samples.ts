import { IPaciente, NewPaciente } from './paciente.model';

export const sampleWithRequiredData: IPaciente = {
  id: 11073,
};

export const sampleWithPartialData: IPaciente = {
  id: 742,
};

export const sampleWithFullData: IPaciente = {
  id: 12242,
  nombre: 'alarm that nag',
};

export const sampleWithNewData: NewPaciente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
