import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICita, NewCita } from '../cita.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICita for edit and NewCitaFormGroupInput for create.
 */
type CitaFormGroupInput = ICita | PartialWithRequiredKeyOf<NewCita>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICita | NewCita> = Omit<T, 'horarioConsultaInicio' | 'horarioConsultaFin'> & {
  horarioConsultaInicio?: string | null;
  horarioConsultaFin?: string | null;
};

type CitaFormRawValue = FormValueOf<ICita>;

type NewCitaFormRawValue = FormValueOf<NewCita>;

type CitaFormDefaults = Pick<NewCita, 'id' | 'horarioConsultaInicio' | 'horarioConsultaFin'>;

type CitaFormGroupContent = {
  id: FormControl<CitaFormRawValue['id'] | NewCita['id']>;
  horarioConsultaInicio: FormControl<CitaFormRawValue['horarioConsultaInicio']>;
  horarioConsultaFin: FormControl<CitaFormRawValue['horarioConsultaFin']>;
  doctor: FormControl<CitaFormRawValue['doctor']>;
  consultorio: FormControl<CitaFormRawValue['consultorio']>;
  paciente: FormControl<CitaFormRawValue['paciente']>;
};

export type CitaFormGroup = FormGroup<CitaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CitaFormService {
  createCitaFormGroup(cita: CitaFormGroupInput = { id: null }): CitaFormGroup {
    const citaRawValue = this.convertCitaToCitaRawValue({
      ...this.getFormDefaults(),
      ...cita,
    });
    return new FormGroup<CitaFormGroupContent>({
      id: new FormControl(
        { value: citaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      horarioConsultaInicio: new FormControl(citaRawValue.horarioConsultaInicio),
      horarioConsultaFin: new FormControl(citaRawValue.horarioConsultaFin),
      doctor: new FormControl(citaRawValue.doctor),
      consultorio: new FormControl(citaRawValue.consultorio),
      paciente: new FormControl(citaRawValue.paciente),
    });
  }

  getCita(form: CitaFormGroup): ICita | NewCita {
    return this.convertCitaRawValueToCita(form.getRawValue() as CitaFormRawValue | NewCitaFormRawValue);
  }

  resetForm(form: CitaFormGroup, cita: CitaFormGroupInput): void {
    const citaRawValue = this.convertCitaToCitaRawValue({ ...this.getFormDefaults(), ...cita });
    form.reset(
      {
        ...citaRawValue,
        id: { value: citaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CitaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      horarioConsultaInicio: currentTime,
      horarioConsultaFin: currentTime,
    };
  }

  private convertCitaRawValueToCita(rawCita: CitaFormRawValue | NewCitaFormRawValue): ICita | NewCita {
    return {
      ...rawCita,
      horarioConsultaInicio: dayjs(rawCita.horarioConsultaInicio, DATE_TIME_FORMAT),
      horarioConsultaFin: dayjs(rawCita.horarioConsultaFin, DATE_TIME_FORMAT),
    };
  }

  private convertCitaToCitaRawValue(
    cita: ICita | (Partial<NewCita> & CitaFormDefaults),
  ): CitaFormRawValue | PartialWithRequiredKeyOf<NewCitaFormRawValue> {
    return {
      ...cita,
      horarioConsultaInicio: cita.horarioConsultaInicio ? cita.horarioConsultaInicio.format(DATE_TIME_FORMAT) : undefined,
      horarioConsultaFin: cita.horarioConsultaFin ? cita.horarioConsultaFin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
