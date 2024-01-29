import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../consultorio.test-samples';

import { ConsultorioFormService } from './consultorio-form.service';

describe('Consultorio Form Service', () => {
  let service: ConsultorioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConsultorioFormService);
  });

  describe('Service methods', () => {
    describe('createConsultorioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConsultorioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            piso: expect.any(Object),
          }),
        );
      });

      it('passing IConsultorio should create a new form with FormGroup', () => {
        const formGroup = service.createConsultorioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            piso: expect.any(Object),
          }),
        );
      });
    });

    describe('getConsultorio', () => {
      it('should return NewConsultorio for default Consultorio initial value', () => {
        const formGroup = service.createConsultorioFormGroup(sampleWithNewData);

        const consultorio = service.getConsultorio(formGroup) as any;

        expect(consultorio).toMatchObject(sampleWithNewData);
      });

      it('should return NewConsultorio for empty Consultorio initial value', () => {
        const formGroup = service.createConsultorioFormGroup();

        const consultorio = service.getConsultorio(formGroup) as any;

        expect(consultorio).toMatchObject({});
      });

      it('should return IConsultorio', () => {
        const formGroup = service.createConsultorioFormGroup(sampleWithRequiredData);

        const consultorio = service.getConsultorio(formGroup) as any;

        expect(consultorio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConsultorio should not enable id FormControl', () => {
        const formGroup = service.createConsultorioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConsultorio should disable id FormControl', () => {
        const formGroup = service.createConsultorioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
