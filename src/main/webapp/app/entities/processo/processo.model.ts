import dayjs from 'dayjs/esm';
import { ITurma } from 'app/entities/turma/turma.model';
import { StatusProcesso } from 'app/entities/enumerations/status-processo.model';
import { Status } from 'app/entities/enumerations/status.model';
import { Enviado } from 'app/entities/enumerations/enviado.model';

export interface IProcesso {
  id: number;
  statusProcesso?: StatusProcesso | null;
  matricula?: string | null;
  nome?: string | null;
  data?: dayjs.Dayjs | null;
  numeroDaDefesa?: string | null;
  statusSigaa?: Status | null;
  numeroSipac?: string | null;
  enviadoBiblioteca?: Enviado | null;
  turma?: Pick<ITurma, 'id' | 'ano'> | null;
}

export type NewProcesso = Omit<IProcesso, 'id'> & { id: null };
