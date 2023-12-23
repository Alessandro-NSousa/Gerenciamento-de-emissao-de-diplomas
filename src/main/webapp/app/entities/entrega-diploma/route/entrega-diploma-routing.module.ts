import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EntregaDiplomaComponent } from '../list/entrega-diploma.component';
import { EntregaDiplomaDetailComponent } from '../detail/entrega-diploma-detail.component';
import { EntregaDiplomaUpdateComponent } from '../update/entrega-diploma-update.component';
import { EntregaDiplomaRoutingResolveService } from './entrega-diploma-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const entregaDiplomaRoute: Routes = [
  {
    path: '',
    component: EntregaDiplomaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EntregaDiplomaDetailComponent,
    resolve: {
      entregaDiploma: EntregaDiplomaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EntregaDiplomaUpdateComponent,
    resolve: {
      entregaDiploma: EntregaDiplomaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EntregaDiplomaUpdateComponent,
    resolve: {
      entregaDiploma: EntregaDiplomaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(entregaDiplomaRoute)],
  exports: [RouterModule],
})
export class EntregaDiplomaRoutingModule {}
