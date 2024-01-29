import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConsultorio } from '../consultorio.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../consultorio.test-samples';

import { ConsultorioService } from './consultorio.service';

const requireRestSample: IConsultorio = {
  ...sampleWithRequiredData,
};

describe('Consultorio Service', () => {
  let service: ConsultorioService;
  let httpMock: HttpTestingController;
  let expectedResult: IConsultorio | IConsultorio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConsultorioService);
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

    it('should create a Consultorio', () => {
      const consultorio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(consultorio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Consultorio', () => {
      const consultorio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(consultorio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Consultorio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Consultorio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Consultorio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConsultorioToCollectionIfMissing', () => {
      it('should add a Consultorio to an empty array', () => {
        const consultorio: IConsultorio = sampleWithRequiredData;
        expectedResult = service.addConsultorioToCollectionIfMissing([], consultorio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(consultorio);
      });

      it('should not add a Consultorio to an array that contains it', () => {
        const consultorio: IConsultorio = sampleWithRequiredData;
        const consultorioCollection: IConsultorio[] = [
          {
            ...consultorio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConsultorioToCollectionIfMissing(consultorioCollection, consultorio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Consultorio to an array that doesn't contain it", () => {
        const consultorio: IConsultorio = sampleWithRequiredData;
        const consultorioCollection: IConsultorio[] = [sampleWithPartialData];
        expectedResult = service.addConsultorioToCollectionIfMissing(consultorioCollection, consultorio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(consultorio);
      });

      it('should add only unique Consultorio to an array', () => {
        const consultorioArray: IConsultorio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const consultorioCollection: IConsultorio[] = [sampleWithRequiredData];
        expectedResult = service.addConsultorioToCollectionIfMissing(consultorioCollection, ...consultorioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const consultorio: IConsultorio = sampleWithRequiredData;
        const consultorio2: IConsultorio = sampleWithPartialData;
        expectedResult = service.addConsultorioToCollectionIfMissing([], consultorio, consultorio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(consultorio);
        expect(expectedResult).toContain(consultorio2);
      });

      it('should accept null and undefined values', () => {
        const consultorio: IConsultorio = sampleWithRequiredData;
        expectedResult = service.addConsultorioToCollectionIfMissing([], null, consultorio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(consultorio);
      });

      it('should return initial array if no Consultorio is added', () => {
        const consultorioCollection: IConsultorio[] = [sampleWithRequiredData];
        expectedResult = service.addConsultorioToCollectionIfMissing(consultorioCollection, undefined, null);
        expect(expectedResult).toEqual(consultorioCollection);
      });
    });

    describe('compareConsultorio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConsultorio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConsultorio(entity1, entity2);
        const compareResult2 = service.compareConsultorio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConsultorio(entity1, entity2);
        const compareResult2 = service.compareConsultorio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConsultorio(entity1, entity2);
        const compareResult2 = service.compareConsultorio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
