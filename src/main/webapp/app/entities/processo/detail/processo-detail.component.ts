import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProcesso } from '../processo.model';

@Component({
  selector: 'jhi-processo-detail',
  templateUrl: './processo-detail.component.html',
})
export class ProcessoDetailComponent implements OnInit {
  processo: IProcesso | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ processo }) => {
      this.processo = processo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
