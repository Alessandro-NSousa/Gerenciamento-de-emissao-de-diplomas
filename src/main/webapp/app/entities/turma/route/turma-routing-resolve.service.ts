import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITurma } from '../turma.model';
import { TurmaService } from '../service/turma.service';

@Injectable({ providedIn: 'root' })
export class TurmaRoutingResolveService implements Resolve<ITurma | null> {
  constructor(protected service: TurmaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITurma | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((turma: HttpResponse<ITurma>) => {
          if (turma.body) {
            return of(turma.body);
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
