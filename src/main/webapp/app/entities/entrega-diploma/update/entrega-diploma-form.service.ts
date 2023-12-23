import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEntregaDiploma, NewEntregaDiploma } from '../entrega-diploma.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEntregaDiploma for edit and NewEntregaDiplomaFormGroupInput for create.
 */
type EntregaDiplomaFormGroupInput = IEntregaDiploma | PartialWithRequiredKeyOf<NewEntregaDiploma>;

type EntregaDiplomaFormDefaults = Pick<NewEntregaDiploma, 'id'>;

type EntregaDiplomaFormGroupContent = {
  id: FormControl<IEntregaDiploma['id'] | NewEntregaDiploma['id']>;
  dataDeEntrega: FormControl<IEntregaDiploma['dataDeEntrega']>;
  observacoes: FormControl<IEntregaDiploma['observacoes']>;
  processo: FormControl<IEntregaDiploma['processo']>;
};

export type EntregaDiplomaFormGroup = FormGroup<EntregaDiplomaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EntregaDiplomaFormService {
  createEntregaDiplomaFormGroup(entregaDiploma: EntregaDiplomaFormGroupInput = { id: null }): EntregaDiplomaFormGroup {
    const entregaDiplomaRawValue = {
      ...this.getFormDefaults(),
      ...entregaDiploma,
    };
    return new FormGroup<EntregaDiplomaFormGroupContent>({
      id: new FormControl(
        { value: entregaDiplomaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dataDeEntrega: new FormControl(entregaDiplomaRawValue.dataDeEntrega),
      observacoes: new FormControl(entregaDiplomaRawValue.observacoes),
      processo: new FormControl(entregaDiplomaRawValue.processo),
    });
  }

  getEntregaDiploma(form: EntregaDiplomaFormGroup): IEntregaDiploma | NewEntregaDiploma {
    return form.getRawValue() as IEntregaDiploma | NewEntregaDiploma;
  }

  resetForm(form: EntregaDiplomaFormGroup, entregaDiploma: EntregaDiplomaFormGroupInput): void {
    const entregaDiplomaRawValue = { ...this.getFormDefaults(), ...entregaDiploma };
    form.reset(
      {
        ...entregaDiplomaRawValue,
        id: { value: entregaDiplomaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EntregaDiplomaFormDefaults {
    return {
      id: null,
    };
  }
}
