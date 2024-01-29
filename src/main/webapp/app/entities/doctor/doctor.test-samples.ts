import { IDoctor, NewDoctor } from './doctor.model';

export const sampleWithRequiredData: IDoctor = {
  id: 6028,
};

export const sampleWithPartialData: IDoctor = {
  id: 13963,
  nombre: 'an',
  apellidoPaterno: 'qua upwardly huzzah',
  especialidad: 'upside-down',
};

export const sampleWithFullData: IDoctor = {
  id: 26575,
  nombre: 'ouch',
  apellidoPaterno: 'invert',
  apellidoMaterno: 'shrilly unlike anxiously',
  especialidad: 'whenever yowza',
};

export const sampleWithNewData: NewDoctor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
