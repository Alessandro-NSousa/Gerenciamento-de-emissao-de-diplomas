import dayjs from 'dayjs/esm';
import { IProcesso } from 'app/entities/processo/processo.model';

export interface IEntregaDiploma {
  id: number;
  dataDeEntrega?: dayjs.Dayjs | null;
  observacoes?: string | null;
  processo?: Pick<IProcesso, 'id' | 'nome'> | null;
}

export type NewEntregaDiploma = Omit<IEntregaDiploma, 'id'> & { id: null };
