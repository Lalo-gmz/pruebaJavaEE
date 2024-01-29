import { IConsultorio, NewConsultorio } from './consultorio.model';

export const sampleWithRequiredData: IConsultorio = {
  id: 19051,
};

export const sampleWithPartialData: IConsultorio = {
  id: 196,
  piso: 'gadzooks especially',
};

export const sampleWithFullData: IConsultorio = {
  id: 2345,
  numero: 'as brr extreme',
  piso: 'broadside',
};

export const sampleWithNewData: NewConsultorio = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
