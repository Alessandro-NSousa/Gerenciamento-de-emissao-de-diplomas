import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TurmaFormService } from './turma-form.service';
import { TurmaService } from '../service/turma.service';
import { ITurma } from '../turma.model';

import { TurmaUpdateComponent } from './turma-update.component';

describe('Turma Management Update Component', () => {
  let comp: TurmaUpdateComponent;
  let fixture: ComponentFixture<TurmaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let turmaFormService: TurmaFormService;
  let turmaService: TurmaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TurmaUpdateComponent],
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
      .overrideTemplate(TurmaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TurmaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    turmaFormService = TestBed.inject(TurmaFormService);
    turmaService = TestBed.inject(TurmaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const turma: ITurma = { id: 456 };

      activatedRoute.data = of({ turma });
      comp.ngOnInit();

      expect(comp.turma).toEqual(turma);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITurma>>();
      const turma = { id: 123 };
      jest.spyOn(turmaFormService, 'getTurma').mockReturnValue(turma);
      jest.spyOn(turmaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ turma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: turma }));
      saveSubject.complete();

      // THEN
      expect(turmaFormService.getTurma).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(turmaService.update).toHaveBeenCalledWith(expect.objectContaining(turma));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITurma>>();
      const turma = { id: 123 };
      jest.spyOn(turmaFormService, 'getTurma').mockReturnValue({ id: null });
      jest.spyOn(turmaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ turma: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: turma }));
      saveSubject.complete();

      // THEN
      expect(turmaFormService.getTurma).toHaveBeenCalled();
      expect(turmaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITurma>>();
      const turma = { id: 123 };
      jest.spyOn(turmaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ turma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(turmaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
