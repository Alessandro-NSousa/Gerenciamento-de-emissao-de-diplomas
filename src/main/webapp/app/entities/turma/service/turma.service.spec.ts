import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITurma } from '../turma.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../turma.test-samples';

import { TurmaService } from './turma.service';

const requireRestSample: ITurma = {
  ...sampleWithRequiredData,
};

describe('Turma Service', () => {
  let service: TurmaService;
  let httpMock: HttpTestingController;
  let expectedResult: ITurma | ITurma[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TurmaService);
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

    it('should create a Turma', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const turma = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(turma).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Turma', () => {
      const turma = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(turma).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Turma', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Turma', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Turma', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTurmaToCollectionIfMissing', () => {
      it('should add a Turma to an empty array', () => {
        const turma: ITurma = sampleWithRequiredData;
        expectedResult = service.addTurmaToCollectionIfMissing([], turma);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(turma);
      });

      it('should not add a Turma to an array that contains it', () => {
        const turma: ITurma = sampleWithRequiredData;
        const turmaCollection: ITurma[] = [
          {
            ...turma,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTurmaToCollectionIfMissing(turmaCollection, turma);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Turma to an array that doesn't contain it", () => {
        const turma: ITurma = sampleWithRequiredData;
        const turmaCollection: ITurma[] = [sampleWithPartialData];
        expectedResult = service.addTurmaToCollectionIfMissing(turmaCollection, turma);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(turma);
      });

      it('should add only unique Turma to an array', () => {
        const turmaArray: ITurma[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const turmaCollection: ITurma[] = [sampleWithRequiredData];
        expectedResult = service.addTurmaToCollectionIfMissing(turmaCollection, ...turmaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const turma: ITurma = sampleWithRequiredData;
        const turma2: ITurma = sampleWithPartialData;
        expectedResult = service.addTurmaToCollectionIfMissing([], turma, turma2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(turma);
        expect(expectedResult).toContain(turma2);
      });

      it('should accept null and undefined values', () => {
        const turma: ITurma = sampleWithRequiredData;
        expectedResult = service.addTurmaToCollectionIfMissing([], null, turma, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(turma);
      });

      it('should return initial array if no Turma is added', () => {
        const turmaCollection: ITurma[] = [sampleWithRequiredData];
        expectedResult = service.addTurmaToCollectionIfMissing(turmaCollection, undefined, null);
        expect(expectedResult).toEqual(turmaCollection);
      });
    });

    describe('compareTurma', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTurma(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTurma(entity1, entity2);
        const compareResult2 = service.compareTurma(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTurma(entity1, entity2);
        const compareResult2 = service.compareTurma(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTurma(entity1, entity2);
        const compareResult2 = service.compareTurma(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
