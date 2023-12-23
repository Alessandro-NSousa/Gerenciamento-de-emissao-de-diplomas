import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEntregaDiploma, NewEntregaDiploma } from '../entrega-diploma.model';

export type PartialUpdateEntregaDiploma = Partial<IEntregaDiploma> & Pick<IEntregaDiploma, 'id'>;

type RestOf<T extends IEntregaDiploma | NewEntregaDiploma> = Omit<T, 'dataDeEntrega'> & {
  dataDeEntrega?: string | null;
};

export type RestEntregaDiploma = RestOf<IEntregaDiploma>;

export type NewRestEntregaDiploma = RestOf<NewEntregaDiploma>;

export type PartialUpdateRestEntregaDiploma = RestOf<PartialUpdateEntregaDiploma>;

export type EntityResponseType = HttpResponse<IEntregaDiploma>;
export type EntityArrayResponseType = HttpResponse<IEntregaDiploma[]>;

@Injectable({ providedIn: 'root' })
export class EntregaDiplomaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/entrega-diplomas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(entregaDiploma: NewEntregaDiploma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entregaDiploma);
    return this.http
      .post<RestEntregaDiploma>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(entregaDiploma: IEntregaDiploma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entregaDiploma);
    return this.http
      .put<RestEntregaDiploma>(`${this.resourceUrl}/${this.getEntregaDiplomaIdentifier(entregaDiploma)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(entregaDiploma: PartialUpdateEntregaDiploma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entregaDiploma);
    return this.http
      .patch<RestEntregaDiploma>(`${this.resourceUrl}/${this.getEntregaDiplomaIdentifier(entregaDiploma)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEntregaDiploma>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEntregaDiploma[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEntregaDiplomaIdentifier(entregaDiploma: Pick<IEntregaDiploma, 'id'>): number {
    return entregaDiploma.id;
  }

  compareEntregaDiploma(o1: Pick<IEntregaDiploma, 'id'> | null, o2: Pick<IEntregaDiploma, 'id'> | null): boolean {
    return o1 && o2 ? this.getEntregaDiplomaIdentifier(o1) === this.getEntregaDiplomaIdentifier(o2) : o1 === o2;
  }

  addEntregaDiplomaToCollectionIfMissing<Type extends Pick<IEntregaDiploma, 'id'>>(
    entregaDiplomaCollection: Type[],
    ...entregaDiplomasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const entregaDiplomas: Type[] = entregaDiplomasToCheck.filter(isPresent);
    if (entregaDiplomas.length > 0) {
      const entregaDiplomaCollectionIdentifiers = entregaDiplomaCollection.map(
        entregaDiplomaItem => this.getEntregaDiplomaIdentifier(entregaDiplomaItem)!
      );
      const entregaDiplomasToAdd = entregaDiplomas.filter(entregaDiplomaItem => {
        const entregaDiplomaIdentifier = this.getEntregaDiplomaIdentifier(entregaDiplomaItem);
        if (entregaDiplomaCollectionIdentifiers.includes(entregaDiplomaIdentifier)) {
          return false;
        }
        entregaDiplomaCollectionIdentifiers.push(entregaDiplomaIdentifier);
        return true;
      });
      return [...entregaDiplomasToAdd, ...entregaDiplomaCollection];
    }
    return entregaDiplomaCollection;
  }

  protected convertDateFromClient<T extends IEntregaDiploma | NewEntregaDiploma | PartialUpdateEntregaDiploma>(
    entregaDiploma: T
  ): RestOf<T> {
    return {
      ...entregaDiploma,
      dataDeEntrega: entregaDiploma.dataDeEntrega?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEntregaDiploma: RestEntregaDiploma): IEntregaDiploma {
    return {
      ...restEntregaDiploma,
      dataDeEntrega: restEntregaDiploma.dataDeEntrega ? dayjs(restEntregaDiploma.dataDeEntrega) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEntregaDiploma>): HttpResponse<IEntregaDiploma> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEntregaDiploma[]>): HttpResponse<IEntregaDiploma[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
