import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProcessoService } from '../service/processo.service';

import { ProcessoComponent } from './processo.component';

describe('Processo Management Component', () => {
  let comp: ProcessoComponent;
  let fixture: ComponentFixture<ProcessoComponent>;
  let service: ProcessoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'processo', component: ProcessoComponent }]), HttpClientTestingModule],
      declarations: [ProcessoComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ProcessoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcessoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProcessoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.processos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to processoService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getProcessoIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getProcessoIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
