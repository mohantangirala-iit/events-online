import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPersonType } from '../person-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../person-type.test-samples';

import { PersonTypeService } from './person-type.service';

const requireRestSample: IPersonType = {
  ...sampleWithRequiredData,
};

describe('PersonType Service', () => {
  let service: PersonTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPersonType | IPersonType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PersonTypeService);
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

    it('should create a PersonType', () => {
      const personType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(personType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PersonType', () => {
      const personType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(personType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PersonType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PersonType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PersonType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPersonTypeToCollectionIfMissing', () => {
      it('should add a PersonType to an empty array', () => {
        const personType: IPersonType = sampleWithRequiredData;
        expectedResult = service.addPersonTypeToCollectionIfMissing([], personType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personType);
      });

      it('should not add a PersonType to an array that contains it', () => {
        const personType: IPersonType = sampleWithRequiredData;
        const personTypeCollection: IPersonType[] = [
          {
            ...personType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPersonTypeToCollectionIfMissing(personTypeCollection, personType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PersonType to an array that doesn't contain it", () => {
        const personType: IPersonType = sampleWithRequiredData;
        const personTypeCollection: IPersonType[] = [sampleWithPartialData];
        expectedResult = service.addPersonTypeToCollectionIfMissing(personTypeCollection, personType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personType);
      });

      it('should add only unique PersonType to an array', () => {
        const personTypeArray: IPersonType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const personTypeCollection: IPersonType[] = [sampleWithRequiredData];
        expectedResult = service.addPersonTypeToCollectionIfMissing(personTypeCollection, ...personTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personType: IPersonType = sampleWithRequiredData;
        const personType2: IPersonType = sampleWithPartialData;
        expectedResult = service.addPersonTypeToCollectionIfMissing([], personType, personType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personType);
        expect(expectedResult).toContain(personType2);
      });

      it('should accept null and undefined values', () => {
        const personType: IPersonType = sampleWithRequiredData;
        expectedResult = service.addPersonTypeToCollectionIfMissing([], null, personType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personType);
      });

      it('should return initial array if no PersonType is added', () => {
        const personTypeCollection: IPersonType[] = [sampleWithRequiredData];
        expectedResult = service.addPersonTypeToCollectionIfMissing(personTypeCollection, undefined, null);
        expect(expectedResult).toEqual(personTypeCollection);
      });
    });

    describe('comparePersonType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePersonType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePersonType(entity1, entity2);
        const compareResult2 = service.comparePersonType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePersonType(entity1, entity2);
        const compareResult2 = service.comparePersonType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePersonType(entity1, entity2);
        const compareResult2 = service.comparePersonType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
