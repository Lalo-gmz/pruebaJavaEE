import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPaciente } from '../paciente.model';
import { PacienteService } from '../service/paciente.service';
import { PacienteFormService, PacienteFormGroup } from './paciente-form.service';

@Component({
  standalone: true,
  selector: 'jhi-paciente-update',
  templateUrl: './paciente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PacienteUpdateComponent implements OnInit {
  isSaving = false;
  paciente: IPaciente | null = null;

  editForm: PacienteFormGroup = this.pacienteFormService.createPacienteFormGroup();

  constructor(
    protected pacienteService: PacienteService,
    protected pacienteFormService: PacienteFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paciente }) => {
      this.paciente = paciente;
      if (paciente) {
        this.updateForm(paciente);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paciente = this.pacienteFormService.getPaciente(this.editForm);
    if (paciente.id !== null) {
      this.subscribeToSaveResponse(this.pacienteService.update(paciente));
    } else {
      this.subscribeToSaveResponse(this.pacienteService.create(paciente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaciente>>): void {
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

  protected updateForm(paciente: IPaciente): void {
    this.paciente = paciente;
    this.pacienteFormService.resetForm(this.editForm, paciente);
  }
}
