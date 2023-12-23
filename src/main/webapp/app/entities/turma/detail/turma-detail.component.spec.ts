import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TurmaDetailComponent } from './turma-detail.component';

describe('Turma Management Detail Component', () => {
  let comp: TurmaDetailComponent;
  let fixture: ComponentFixture<TurmaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TurmaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ turma: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TurmaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TurmaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load turma on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.turma).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
