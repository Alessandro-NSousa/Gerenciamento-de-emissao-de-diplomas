import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TurmaFormService, TurmaFormGroup } from './turma-form.service';
import { ITurma } from '../turma.model';
import { TurmaService } from '../service/turma.service';

@Component({
  selector: 'jhi-turma-update',
  templateUrl: './turma-update.component.html',
})
export class TurmaUpdateComponent implements OnInit {
  isSaving = false;
  turma: ITurma | null = null;

  editForm: TurmaFormGroup = this.turmaFormService.createTurmaFormGroup();

  constructor(
    protected turmaService: TurmaService,
    protected turmaFormService: TurmaFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ turma }) => {
      this.turma = turma;
      if (turma) {
        this.updateForm(turma);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const turma = this.turmaFormService.getTurma(this.editForm);
    if (turma.id !== null) {
      this.subscribeToSaveResponse(this.turmaService.update(turma));
    } else {
      this.subscribeToSaveResponse(this.turmaService.create(turma));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITurma>>): void {
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

  protected updateForm(turma: ITurma): void {
    this.turma = turma;
    this.turmaFormService.resetForm(this.editForm, turma);
  }
}
