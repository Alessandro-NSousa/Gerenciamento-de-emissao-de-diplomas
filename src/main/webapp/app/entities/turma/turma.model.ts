export interface ITurma {
  id: number;
  curso?: string | null;
  sigla?: string | null;
  ano?: string | null;
}

export type NewTurma = Omit<ITurma, 'id'> & { id: null };
