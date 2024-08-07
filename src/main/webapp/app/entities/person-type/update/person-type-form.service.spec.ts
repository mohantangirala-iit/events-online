import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../person-type.test-samples';

import { PersonTypeFormService } from './person-type-form.service';

describe('PersonType Form Service', () => {
  let service: PersonTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PersonTypeFormService);
  });

  describe('Service methods', () => {
    describe('createPersonTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPersonTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            jobTitle: expect.any(Object),
            role: expect.any(Object),
            level: expect.any(Object),
          }),
        );
      });

      it('passing IPersonType should create a new form with FormGroup', () => {
        const formGroup = service.createPersonTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            jobTitle: expect.any(Object),
            role: expect.any(Object),
            level: expect.any(Object),
          }),
        );
      });
    });

    describe('getPersonType', () => {
      it('should return NewPersonType for default PersonType initial value', () => {
        const formGroup = service.createPersonTypeFormGroup(sampleWithNewData);

        const personType = service.getPersonType(formGroup) as any;

        expect(personType).toMatchObject(sampleWithNewData);
      });

      it('should return NewPersonType for empty PersonType initial value', () => {
        const formGroup = service.createPersonTypeFormGroup();

        const personType = service.getPersonType(formGroup) as any;

        expect(personType).toMatchObject({});
      });

      it('should return IPersonType', () => {
        const formGroup = service.createPersonTypeFormGroup(sampleWithRequiredData);

        const personType = service.getPersonType(formGroup) as any;

        expect(personType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPersonType should not enable id FormControl', () => {
        const formGroup = service.createPersonTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPersonType should disable id FormControl', () => {
        const formGroup = service.createPersonTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
