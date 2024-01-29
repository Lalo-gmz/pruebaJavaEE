import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConsultorio, NewConsultorio } from '../consultorio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConsultorio for edit and NewConsultorioFormGroupInput for create.
 */
type ConsultorioFormGroupInput = IConsultorio | PartialWithRequiredKeyOf<NewConsultorio>;

type ConsultorioFormDefaults = Pick<NewConsultorio, 'id'>;

type ConsultorioFormGroupContent = {
  id: FormControl<IConsultorio['id'] | NewConsultorio['id']>;
  numero: FormControl<IConsultorio['numero']>;
  piso: FormControl<IConsultorio['piso']>;
};

export type ConsultorioFormGroup = FormGroup<ConsultorioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConsultorioFormService {
  createConsultorioFormGroup(consultorio: ConsultorioFormGroupInput = { id: null }): ConsultorioFormGroup {
    const consultorioRawValue = {
      ...this.getFormDefaults(),
      ...consultorio,
    };
    return new FormGroup<ConsultorioFormGroupContent>({
      id: new FormControl(
        { value: consultorioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numero: new FormControl(consultorioRawValue.numero),
      piso: new FormControl(consultorioRawValue.piso),
    });
  }

  getConsultorio(form: ConsultorioFormGroup): IConsultorio | NewConsultorio {
    return form.getRawValue() as IConsultorio | NewConsultorio;
  }

  resetForm(form: ConsultorioFormGroup, consultorio: ConsultorioFormGroupInput): void {
    const consultorioRawValue = { ...this.getFormDefaults(), ...consultorio };
    form.reset(
      {
        ...consultorioRawValue,
        id: { value: consultorioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConsultorioFormDefaults {
    return {
      id: null,
    };
  }
}
