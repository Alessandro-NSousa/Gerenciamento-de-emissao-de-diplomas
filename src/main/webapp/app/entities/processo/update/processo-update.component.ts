import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProcessoFormService, ProcessoFormGroup } from './processo-form.service';
import { IProcesso } from '../processo.model';
import { ProcessoService } from '../service/processo.service';
import { ITurma } from 'app/entities/turma/turma.model';
import { TurmaService } from 'app/entities/turma/service/turma.service';
import { StatusProcesso } from 'app/entities/enumerations/status-processo.model';
import { Status } from 'app/entities/enumerations/status.model';
import { Enviado } from 'app/entities/enumerations/enviado.model';

@Component({
  selector: 'jhi-processo-update',
  templateUrl: './processo-update.component.html',
})
export class ProcessoUpdateComponent implements OnInit {
  isSaving = false;
  processo: IProcesso | null = null;
  statusProcessoValues = Object.keys(StatusProcesso);
  statusValues = Object.keys(Status);
  enviadoValues = Object.keys(Enviado);

  turmasSharedCollection: ITurma[] = [];

  editForm: ProcessoFormGroup = this.processoFormService.createProcessoFormGroup();

  constructor(
    protected processoService: ProcessoService,
    protected processoFormService: ProcessoFormService,
    protected turmaService: TurmaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTurma = (o1: ITurma | null, o2: ITurma | null): boolean => this.turmaService.compareTurma(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ processo }) => {
      this.processo = processo;
      if (processo) {
        this.updateForm(processo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const processo = this.processoFormService.getProcesso(this.editForm);
    if (processo.id !== null) {
      this.subscribeToSaveResponse(this.processoService.update(processo));
    } else {
      this.subscribeToSaveResponse(this.processoService.create(processo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcesso>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(processo: IProcesso): void {
    this.processo = processo;
    this.processoFormService.resetForm(this.editForm, processo);

    this.turmasSharedCollection = this.turmaService.addTurmaToCollectionIfMissing<ITurma>(this.turmasSharedCollection, processo.turma);
  }

  protected loadRelationshipsOptions(): void {
    this.turmaService
      .query()
      .pipe(map((res: HttpResponse<ITurma[]>) => res.body ?? []))
      .pipe(map((turmas: ITurma[]) => this.turmaService.addTurmaToCollectionIfMissing<ITurma>(turmas, this.processo?.turma)))
      .subscribe((turmas: ITurma[]) => (this.turmasSharedCollection = turmas));
  }
}
