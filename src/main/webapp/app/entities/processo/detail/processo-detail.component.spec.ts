import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProcessoDetailComponent } from './processo-detail.component';

describe('Processo Management Detail Component', () => {
  let comp: ProcessoDetailComponent;
  let fixture: ComponentFixture<ProcessoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcessoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ processo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProcessoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProcessoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load processo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.processo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
