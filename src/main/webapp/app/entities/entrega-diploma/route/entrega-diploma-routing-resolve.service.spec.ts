import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEntregaDiploma } from '../entrega-diploma.model';
import { EntregaDiplomaService } from '../service/entrega-diploma.service';

import { EntregaDiplomaRoutingResolveService } from './entrega-diploma-routing-resolve.service';

describe('EntregaDiploma routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EntregaDiplomaRoutingResolveService;
  let service: EntregaDiplomaService;
  let resultEntregaDiploma: IEntregaDiploma | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(EntregaDiplomaRoutingResolveService);
    service = TestBed.inject(EntregaDiplomaService);
    resultEntregaDiploma = undefined;
  });

  describe('resolve', () => {
    it('should return IEntregaDiploma returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEntregaDiploma = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEntregaDiploma).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEntregaDiploma = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEntregaDiploma).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IEntregaDiploma>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEntregaDiploma = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEntregaDiploma).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
