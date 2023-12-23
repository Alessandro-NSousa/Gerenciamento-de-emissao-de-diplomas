import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProcesso, NewProcesso } from '../processo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcesso for edit and NewProcessoFormGroupInput for create.
 */
type ProcessoFormGroupInput = IProcesso | PartialWithRequiredKeyOf<NewProcesso>;

type ProcessoFormDefaults = Pick<NewProcesso, 'id'>;

type ProcessoFormGroupContent = {
  id: FormControl<IProcesso['id'] | NewProcesso['id']>;
  statusProcesso: FormControl<IProcesso['statusProcesso']>;
  matricula: FormControl<IProcesso['matricula']>;
  nome: FormControl<IProcesso['nome']>;
  data: FormControl<IProcesso['data']>;
  numeroDaDefesa: FormControl<IProcesso['numeroDaDefesa']>;
  statusSigaa: FormControl<IProcesso['statusSigaa']>;
  numeroSipac: FormControl<IProcesso['numeroSipac']>;
  enviadoBiblioteca: FormControl<IProcesso['enviadoBiblioteca']>;
  turma: FormControl<IProcesso['turma']>;
};

export type ProcessoFormGroup = FormGroup<ProcessoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProcessoFormService {
  createProcessoFormGroup(processo: ProcessoFormGroupInput = { id: null }): ProcessoFormGroup {
    const processoRawValue = {
      ...this.getFormDefaults(),
      ...processo,
    };
    return new FormGroup<ProcessoFormGroupContent>({
      id: new FormControl(
        { value: processoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      statusProcesso: new FormControl(processoRawValue.statusProcesso),
      matricula: new FormControl(processoRawValue.matricula, {
        validators: [Validators.required],
      }),
      nome: new FormControl(processoRawValue.nome),
      data: new FormControl(processoRawValue.data),
      numeroDaDefesa: new FormControl(processoRawValue.numeroDaDefesa),
      statusSigaa: new FormControl(processoRawValue.statusSigaa),
      numeroSipac: new FormControl(processoRawValue.numeroSipac),
      enviadoBiblioteca: new FormControl(processoRawValue.enviadoBiblioteca),
      turma: new FormControl(processoRawValue.turma),
    });
  }

  getProcesso(form: ProcessoFormGroup): IProcesso | NewProcesso {
    return form.getRawValue() as IProcesso | NewProcesso;
  }

  resetForm(form: ProcessoFormGroup, processo: ProcessoFormGroupInput): void {
    const processoRawValue = { ...this.getFormDefaults(), ...processo };
    form.reset(
      {
        ...processoRawValue,
        id: { value: processoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProcessoFormDefaults {
    return {
      id: null,
    };
  }
}
