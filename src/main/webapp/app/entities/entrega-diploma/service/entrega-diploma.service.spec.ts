import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEntregaDiploma } from '../entrega-diploma.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../entrega-diploma.test-samples';

import { EntregaDiplomaService, RestEntregaDiploma } from './entrega-diploma.service';

const requireRestSample: RestEntregaDiploma = {
  ...sampleWithRequiredData,
  dataDeEntrega: sampleWithRequiredData.dataDeEntrega?.format(DATE_FORMAT),
};

describe('EntregaDiploma Service', () => {
  let service: EntregaDiplomaService;
  let httpMock: HttpTestingController;
  let expectedResult: IEntregaDiploma | IEntregaDiploma[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EntregaDiplomaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a EntregaDiploma', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const entregaDiploma = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(entregaDiploma).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EntregaDiploma', () => {
      const entregaDiploma = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(entregaDiploma).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EntregaDiploma', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EntregaDiploma', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EntregaDiploma', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEntregaDiplomaToCollectionIfMissing', () => {
      it('should add a EntregaDiploma to an empty array', () => {
        const entregaDiploma: IEntregaDiploma = sampleWithRequiredData;
        expectedResult = service.addEntregaDiplomaToCollectionIfMissing([], entregaDiploma);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entregaDiploma);
      });

      it('should not add a EntregaDiploma to an array that contains it', () => {
        const entregaDiploma: IEntregaDiploma = sampleWithRequiredData;
        const entregaDiplomaCollection: IEntregaDiploma[] = [
          {
            ...entregaDiploma,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEntregaDiplomaToCollectionIfMissing(entregaDiplomaCollection, entregaDiploma);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EntregaDiploma to an array that doesn't contain it", () => {
        const entregaDiploma: IEntregaDiploma = sampleWithRequiredData;
        const entregaDiplomaCollection: IEntregaDiploma[] = [sampleWithPartialData];
        expectedResult = service.addEntregaDiplomaToCollectionIfMissing(entregaDiplomaCollection, entregaDiploma);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entregaDiploma);
      });

      it('should add only unique EntregaDiploma to an array', () => {
        const entregaDiplomaArray: IEntregaDiploma[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const entregaDiplomaCollection: IEntregaDiploma[] = [sampleWithRequiredData];
        expectedResult = service.addEntregaDiplomaToCollectionIfMissing(entregaDiplomaCollection, ...entregaDiplomaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entregaDiploma: IEntregaDiploma = sampleWithRequiredData;
        const entregaDiploma2: IEntregaDiploma = sampleWithPartialData;
        expectedResult = service.addEntregaDiplomaToCollectionIfMissing([], entregaDiploma, entregaDiploma2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entregaDiploma);
        expect(expectedResult).toContain(entregaDiploma2);
      });

      it('should accept null and undefined values', () => {
        const entregaDiploma: IEntregaDiploma = sampleWithRequiredData;
        expectedResult = service.addEntregaDiplomaToCollectionIfMissing([], null, entregaDiploma, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entregaDiploma);
      });

      it('should return initial array if no EntregaDiploma is added', () => {
        const entregaDiplomaCollection: IEntregaDiploma[] = [sampleWithRequiredData];
        expectedResult = service.addEntregaDiplomaToCollectionIfMissing(entregaDiplomaCollection, undefined, null);
        expect(expectedResult).toEqual(entregaDiplomaCollection);
      });
    });

    describe('compareEntregaDiploma', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEntregaDiploma(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEntregaDiploma(entity1, entity2);
        const compareResult2 = service.compareEntregaDiploma(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEntregaDiploma(entity1, entity2);
        const compareResult2 = service.compareEntregaDiploma(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEntregaDiploma(entity1, entity2);
        const compareResult2 = service.compareEntregaDiploma(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
