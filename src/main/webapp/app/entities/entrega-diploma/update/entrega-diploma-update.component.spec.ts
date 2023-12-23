import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EntregaDiplomaFormService } from './entrega-diploma-form.service';
import { EntregaDiplomaService } from '../service/entrega-diploma.service';
import { IEntregaDiploma } from '../entrega-diploma.model';
import { IProcesso } from 'app/entities/processo/processo.model';
import { ProcessoService } from 'app/entities/processo/service/processo.service';

import { EntregaDiplomaUpdateComponent } from './entrega-diploma-update.component';

describe('EntregaDiploma Management Update Component', () => {
  let comp: EntregaDiplomaUpdateComponent;
  let fixture: ComponentFixture<EntregaDiplomaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let entregaDiplomaFormService: EntregaDiplomaFormService;
  let entregaDiplomaService: EntregaDiplomaService;
  let processoService: ProcessoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EntregaDiplomaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EntregaDiplomaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EntregaDiplomaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entregaDiplomaFormService = TestBed.inject(EntregaDiplomaFormService);
    entregaDiplomaService = TestBed.inject(EntregaDiplomaService);
    processoService = TestBed.inject(ProcessoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Processo query and add missing value', () => {
      const entregaDiploma: IEntregaDiploma = { id: 456 };
      const processo: IProcesso = { id: 61687 };
      entregaDiploma.processo = processo;

      const processoCollection: IProcesso[] = [{ id: 10446 }];
      jest.spyOn(processoService, 'query').mockReturnValue(of(new HttpResponse({ body: processoCollection })));
      const additionalProcessos = [processo];
      const expectedCollection: IProcesso[] = [...additionalProcessos, ...processoCollection];
      jest.spyOn(processoService, 'addProcessoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ entregaDiploma });
      comp.ngOnInit();

      expect(processoService.query).toHaveBeenCalled();
      expect(processoService.addProcessoToCollectionIfMissing).toHaveBeenCalledWith(
        processoCollection,
        ...additionalProcessos.map(expect.objectContaining)
      );
      expect(comp.processosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const entregaDiploma: IEntregaDiploma = { id: 456 };
      const processo: IProcesso = { id: 21298 };
      entregaDiploma.processo = processo;

      activatedRoute.data = of({ entregaDiploma });
      comp.ngOnInit();

      expect(comp.processosSharedCollection).toContain(processo);
      expect(comp.entregaDiploma).toEqual(entregaDiploma);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEntregaDiploma>>();
      const entregaDiploma = { id: 123 };
      jest.spyOn(entregaDiplomaFormService, 'getEntregaDiploma').mockReturnValue(entregaDiploma);
      jest.spyOn(entregaDiplomaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entregaDiploma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: entregaDiploma }));
      saveSubject.complete();

      // THEN
      expect(entregaDiplomaFormService.getEntregaDiploma).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(entregaDiplomaService.update).toHaveBeenCalledWith(expect.objectContaining(entregaDiploma));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEntregaDiploma>>();
      const entregaDiploma = { id: 123 };
      jest.spyOn(entregaDiplomaFormService, 'getEntregaDiploma').mockReturnValue({ id: null });
      jest.spyOn(entregaDiplomaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entregaDiploma: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: entregaDiploma }));
      saveSubject.complete();

      // THEN
      expect(entregaDiplomaFormService.getEntregaDiploma).toHaveBeenCalled();
      expect(entregaDiplomaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEntregaDiploma>>();
      const entregaDiploma = { id: 123 };
      jest.spyOn(entregaDiplomaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entregaDiploma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entregaDiplomaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProcesso', () => {
      it('Should forward to processoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(processoService, 'compareProcesso');
        comp.compareProcesso(entity, entity2);
        expect(processoService.compareProcesso).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
