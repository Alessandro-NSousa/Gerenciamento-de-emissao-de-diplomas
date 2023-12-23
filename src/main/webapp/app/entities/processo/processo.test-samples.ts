import dayjs from 'dayjs/esm';

import { StatusProcesso } from 'app/entities/enumerations/status-processo.model';
import { Status } from 'app/entities/enumerations/status.model';
import { Enviado } from 'app/entities/enumerations/enviado.model';

import { IProcesso, NewProcesso } from './processo.model';

export const sampleWithRequiredData: IProcesso = {
  id: 70541,
  matricula: 'implementation mobile',
};

export const sampleWithPartialData: IProcesso = {
  id: 87999,
  matricula: 'Dirham Dynamic Credit',
  nome: 'Brand',
  data: dayjs('2023-12-22'),
  numeroDaDefesa: 'bypass',
  enviadoBiblioteca: Enviado['NAO'],
};

export const sampleWithFullData: IProcesso = {
  id: 26956,
  statusProcesso: StatusProcesso['CONCLUIDO'],
  matricula: 'Prático Multi-lateral Goiás',
  nome: 'Global Jardim',
  data: dayjs('2023-12-23'),
  numeroDaDefesa: 'Salsicha open-source',
  statusSigaa: Status['CONCLUIDO'],
  numeroSipac: 'lilás capacitor',
  enviadoBiblioteca: Enviado['SIM'],
};

export const sampleWithNewData: NewProcesso = {
  matricula: 'Consultant Orchestrator Aço',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
