import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EntregaDiplomaService } from '../service/entrega-diploma.service';

import { EntregaDiplomaComponent } from './entrega-diploma.component';

describe('EntregaDiploma Management Component', () => {
  let comp: EntregaDiplomaComponent;
  let fixture: ComponentFixture<EntregaDiplomaComponent>;
  let service: EntregaDiplomaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'entrega-diploma', component: EntregaDiplomaComponent }]), HttpClientTestingModule],
      declarations: [EntregaDiplomaComponent],
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
      .overrideTemplate(EntregaDiplomaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EntregaDiplomaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EntregaDiplomaService);

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
    expect(comp.entregaDiplomas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to entregaDiplomaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getEntregaDiplomaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getEntregaDiplomaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
