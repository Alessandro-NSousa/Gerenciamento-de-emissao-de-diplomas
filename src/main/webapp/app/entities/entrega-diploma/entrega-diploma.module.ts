import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EntregaDiplomaComponent } from './list/entrega-diploma.component';
import { EntregaDiplomaDetailComponent } from './detail/entrega-diploma-detail.component';
import { EntregaDiplomaUpdateComponent } from './update/entrega-diploma-update.component';
import { EntregaDiplomaDeleteDialogComponent } from './delete/entrega-diploma-delete-dialog.component';
import { EntregaDiplomaRoutingModule } from './route/entrega-diploma-routing.module';

@NgModule({
  imports: [SharedModule, EntregaDiplomaRoutingModule],
  declarations: [
    EntregaDiplomaComponent,
    EntregaDiplomaDetailComponent,
    EntregaDiplomaUpdateComponent,
    EntregaDiplomaDeleteDialogComponent,
  ],
})
export class EntregaDiplomaModule {}
