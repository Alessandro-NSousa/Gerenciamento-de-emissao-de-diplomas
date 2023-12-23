import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEntregaDiploma } from '../entrega-diploma.model';
import { EntregaDiplomaService } from '../service/entrega-diploma.service';

@Injectable({ providedIn: 'root' })
export class EntregaDiplomaRoutingResolveService implements Resolve<IEntregaDiploma | null> {
  constructor(protected service: EntregaDiplomaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEntregaDiploma | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((entregaDiploma: HttpResponse<IEntregaDiploma>) => {
          if (entregaDiploma.body) {
            return of(entregaDiploma.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
