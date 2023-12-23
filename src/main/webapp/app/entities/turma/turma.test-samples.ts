import { ITurma, NewTurma } from './turma.model';

export const sampleWithRequiredData: ITurma = {
  id: 7606,
};

export const sampleWithPartialData: ITurma = {
  id: 71526,
  sigla: 'Investor Congelado pricing',
  ano: 'Avon',
};

export const sampleWithFullData: ITurma = {
  id: 2581,
  curso: 'Roraima Division',
  sigla: 'online Plástico',
  ano: 'Avenida',
};

export const sampleWithNewData: NewTurma = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
