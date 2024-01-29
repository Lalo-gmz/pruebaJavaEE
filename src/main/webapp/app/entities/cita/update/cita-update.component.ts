import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDoctor } from 'app/entities/doctor/doctor.model';
import { DoctorService } from 'app/entities/doctor/service/doctor.service';
import { IConsultorio } from 'app/entities/consultorio/consultorio.model';
import { ConsultorioService } from 'app/entities/consultorio/service/consultorio.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { CitaService } from '../service/cita.service';
import { ICita } from '../cita.model';
import { CitaFormService, CitaFormGroup } from './cita-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cita-update',
  templateUrl: './cita-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CitaUpdateComponent implements OnInit {
  isSaving = false;
  cita: ICita | null = null;

  doctorsSharedCollection: IDoctor[] = [];
  consultoriosSharedCollection: IConsultorio[] = [];
  pacientesSharedCollection: IPaciente[] = [];

  editForm: CitaFormGroup = this.citaFormService.createCitaFormGroup();

  constructor(
    protected citaService: CitaService,
    protected citaFormService: CitaFormService,
    protected doctorService: DoctorService,
    protected consultorioService: ConsultorioService,
    protected pacienteService: PacienteService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareDoctor = (o1: IDoctor | null, o2: IDoctor | null): boolean => this.doctorService.compareDoctor(o1, o2);

  compareConsultorio = (o1: IConsultorio | null, o2: IConsultorio | null): boolean => this.consultorioService.compareConsultorio(o1, o2);

  comparePaciente = (o1: IPaciente | null, o2: IPaciente | null): boolean => this.pacienteService.comparePaciente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cita }) => {
      this.cita = cita;
      if (cita) {
        this.updateForm(cita);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cita = this.citaFormService.getCita(this.editForm);
    if (cita.id !== null) {
      this.subscribeToSaveResponse(this.citaService.update(cita));
    } else {
      this.subscribeToSaveResponse(this.citaService.create(cita));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICita>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cita: ICita): void {
    this.cita = cita;
    this.citaFormService.resetForm(this.editForm, cita);

    this.doctorsSharedCollection = this.doctorService.addDoctorToCollectionIfMissing<IDoctor>(this.doctorsSharedCollection, cita.doctor);
    this.consultoriosSharedCollection = this.consultorioService.addConsultorioToCollectionIfMissing<IConsultorio>(
      this.consultoriosSharedCollection,
      cita.consultorio,
    );
    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(
      this.pacientesSharedCollection,
      cita.paciente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.doctorService
      .query()
      .pipe(map((res: HttpResponse<IDoctor[]>) => res.body ?? []))
      .pipe(map((doctors: IDoctor[]) => this.doctorService.addDoctorToCollectionIfMissing<IDoctor>(doctors, this.cita?.doctor)))
      .subscribe((doctors: IDoctor[]) => (this.doctorsSharedCollection = doctors));

    this.consultorioService
      .query()
      .pipe(map((res: HttpResponse<IConsultorio[]>) => res.body ?? []))
      .pipe(
        map((consultorios: IConsultorio[]) =>
          this.consultorioService.addConsultorioToCollectionIfMissing<IConsultorio>(consultorios, this.cita?.consultorio),
        ),
      )
      .subscribe((consultorios: IConsultorio[]) => (this.consultoriosSharedCollection = consultorios));

    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) => this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(pacientes, this.cita?.paciente)),
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));
  }
}
