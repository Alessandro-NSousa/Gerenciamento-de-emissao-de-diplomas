import dayjs from 'dayjs/esm';

import { IEntregaDiploma, NewEntregaDiploma } from './entrega-diploma.model';

export const sampleWithRequiredData: IEntregaDiploma = {
  id: 26281,
};

export const sampleWithPartialData: IEntregaDiploma = {
  id: 67163,
  dataDeEntrega: dayjs('2023-12-22'),
  observacoes: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IEntregaDiploma = {
  id: 77568,
  dataDeEntrega: dayjs('2023-12-22'),
  observacoes: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewEntregaDiploma = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
