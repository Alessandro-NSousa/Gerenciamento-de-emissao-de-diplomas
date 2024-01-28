import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { faFilePdf } from '@fortawesome/free-solid-svg-icons';

import { IProcesso } from '../processo.model';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, ProcessoService } from '../service/processo.service';
import { ProcessoDeleteDialogComponent } from '../delete/processo-delete-dialog.component';
import { SortService } from 'app/shared/sort/sort.service';

declare const require: any;
const jsPDF = require('jspdf');
require('jspdf-autotable');

@Component({
  selector: 'jhi-processo',
  templateUrl: './processo.component.html',
})
export class ProcessoComponent implements OnInit {
  processos?: IProcesso[];
  isLoading = false;
  nomeFilter = '';
  matriculaFilter = '';
  turmaFilter = '';
  statusProcessoFilter = '';

  predicate = 'id';
  ascending = true;

  constructor(
    protected processoService: ProcessoService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IProcesso): number => this.processoService.getProcessoIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  pdf = faFilePdf;

  delete(processo: IProcesso): void {
    const modalRef = this.modalService.open(ProcessoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.processo = processo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.processos = this.refineData(dataFromBody);
  }

  protected refineData(data: IProcesso[]): IProcesso[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IProcesso[] | null): IProcesso[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject = {
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.processoService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  applyFilters(): void {
    let processosFiltrados: IProcesso[] = [];

    if (this.processos) {
      processosFiltrados = this.processos;

      if (this.nomeFilter) {
        processosFiltrados = processosFiltrados.filter(processo => processo.nome?.toLowerCase().includes(this.nomeFilter.toLowerCase()));
      }

      if (this.matriculaFilter) {
        processosFiltrados = processosFiltrados.filter(processo =>
          processo.matricula?.toLowerCase().includes(this.matriculaFilter.toLowerCase())
        );
      }

      if (this.turmaFilter) {
        processosFiltrados = processosFiltrados.filter(processo =>
          processo.turma?.ano?.toLowerCase().includes(this.turmaFilter.toLowerCase())
        );
      }

      /*if (this.statusProcessoFilter) {
        processosFiltrados = processosFiltrados.filter(processo =>
          processo.statusProcesso === this.statusProcessoFilter
        );
      }*/
    }

    if (!this.nomeFilter && !this.matriculaFilter && !this.turmaFilter && !this.statusProcessoFilter) {
      processosFiltrados = this.processos || [];
    }

    this.processos = this.refineData(processosFiltrados);
  }

  search(): void {
    // Aplica os filtros quando o botão de pesquisa for clicado
    this.applyFilters();

    // Se nenhum filtro estiver preenchido, recarregar todos os dados
    if (!this.nomeFilter && !this.matriculaFilter && !this.turmaFilter && !this.statusProcessoFilter) {
      this.load();
    }
  }

  /*gerarPDF() {
    const doc = new jsPDF("landscape");
    
    doc.autoTable({ html: '#tb' });
    doc.save('documento.pdf');
  }*/

  gerarPDF() {
    const doc = new jsPDF('landscape');

    // Selecione todas as linhas da tabela
    const linhas = document.querySelectorAll('#tb tbody tr');

    // Para cada linha, remova a última célula
    linhas.forEach(linha => {
      const ultimaCelula = linha.lastElementChild;
      if (ultimaCelula) {
        linha.removeChild(ultimaCelula);
      }
    });

    // Gere o PDF
    doc.autoTable({ html: '#tb' });
    doc.save('documento.pdf');

    // Restaure as últimas células removidas (opcional)
    linhas.forEach(linha => {
      const ultimaCelula = document.createElement('td');
      linha.appendChild(ultimaCelula);
    });
  }
}
