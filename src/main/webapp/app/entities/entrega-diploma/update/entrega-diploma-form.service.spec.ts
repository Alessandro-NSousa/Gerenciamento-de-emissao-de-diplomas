import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../entrega-diploma.test-samples';

import { EntregaDiplomaFormService } from './entrega-diploma-form.service';

describe('EntregaDiploma Form Service', () => {
  let service: EntregaDiplomaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EntregaDiplomaFormService);
  });

  describe('Service methods', () => {
    describe('createEntregaDiplomaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEntregaDiplomaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataDeEntrega: expect.any(Object),
            observacoes: expect.any(Object),
            processo: expect.any(Object),
          })
        );
      });

      it('passing IEntregaDiploma should create a new form with FormGroup', () => {
        const formGroup = service.createEntregaDiplomaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataDeEntrega: expect.any(Object),
            observacoes: expect.any(Object),
            processo: expect.any(Object),
          })
        );
      });
    });

    describe('getEntregaDiploma', () => {
      it('should return NewEntregaDiploma for default EntregaDiploma initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEntregaDiplomaFormGroup(sampleWithNewData);

        const entregaDiploma = service.getEntregaDiploma(formGroup) as any;

        expect(entregaDiploma).toMatchObject(sampleWithNewData);
      });

      it('should return NewEntregaDiploma for empty EntregaDiploma initial value', () => {
        const formGroup = service.createEntregaDiplomaFormGroup();

        const entregaDiploma = service.getEntregaDiploma(formGroup) as any;

        expect(entregaDiploma).toMatchObject({});
      });

      it('should return IEntregaDiploma', () => {
        const formGroup = service.createEntregaDiplomaFormGroup(sampleWithRequiredData);

        const entregaDiploma = service.getEntregaDiploma(formGroup) as any;

        expect(entregaDiploma).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEntregaDiploma should not enable id FormControl', () => {
        const formGroup = service.createEntregaDiplomaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEntregaDiploma should disable id FormControl', () => {
        const formGroup = service.createEntregaDiplomaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
