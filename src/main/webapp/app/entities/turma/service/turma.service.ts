import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITurma, NewTurma } from '../turma.model';

export type PartialUpdateTurma = Partial<ITurma> & Pick<ITurma, 'id'>;

export type EntityResponseType = HttpResponse<ITurma>;
export type EntityArrayResponseType = HttpResponse<ITurma[]>;

@Injectable({ providedIn: 'root' })
export class TurmaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/turmas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(turma: NewTurma): Observable<EntityResponseType> {
    return this.http.post<ITurma>(this.resourceUrl, turma, { observe: 'response' });
  }

  update(turma: ITurma): Observable<EntityResponseType> {
    return this.http.put<ITurma>(`${this.resourceUrl}/${this.getTurmaIdentifier(turma)}`, turma, { observe: 'response' });
  }

  partialUpdate(turma: PartialUpdateTurma): Observable<EntityResponseType> {
    return this.http.patch<ITurma>(`${this.resourceUrl}/${this.getTurmaIdentifier(turma)}`, turma, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITurma>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITurma[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTurmaIdentifier(turma: Pick<ITurma, 'id'>): number {
    return turma.id;
  }

  compareTurma(o1: Pick<ITurma, 'id'> | null, o2: Pick<ITurma, 'id'> | null): boolean {
    return o1 && o2 ? this.getTurmaIdentifier(o1) === this.getTurmaIdentifier(o2) : o1 === o2;
  }

  addTurmaToCollectionIfMissing<Type extends Pick<ITurma, 'id'>>(
    turmaCollection: Type[],
    ...turmasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const turmas: Type[] = turmasToCheck.filter(isPresent);
    if (turmas.length > 0) {
      const turmaCollectionIdentifiers = turmaCollection.map(turmaItem => this.getTurmaIdentifier(turmaItem)!);
      const turmasToAdd = turmas.filter(turmaItem => {
        const turmaIdentifier = this.getTurmaIdentifier(turmaItem);
        if (turmaCollectionIdentifiers.includes(turmaIdentifier)) {
          return false;
        }
        turmaCollectionIdentifiers.push(turmaIdentifier);
        return true;
      });
      return [...turmasToAdd, ...turmaCollection];
    }
    return turmaCollection;
  }
}
