import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITurma } from '../turma.model';

@Component({
  selector: 'jhi-turma-detail',
  templateUrl: './turma-detail.component.html',
})
export class TurmaDetailComponent implements OnInit {
  turma: ITurma | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ turma }) => {
      this.turma = turma;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
