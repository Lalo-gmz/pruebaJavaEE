import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConsultorio } from '../consultorio.model';
import { ConsultorioService } from '../service/consultorio.service';
import { ConsultorioFormService, ConsultorioFormGroup } from './consultorio-form.service';

@Component({
  standalone: true,
  selector: 'jhi-consultorio-update',
  templateUrl: './consultorio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConsultorioUpdateComponent implements OnInit {
  isSaving = false;
  consultorio: IConsultorio | null = null;

  editForm: ConsultorioFormGroup = this.consultorioFormService.createConsultorioFormGroup();

  constructor(
    protected consultorioService: ConsultorioService,
    protected consultorioFormService: ConsultorioFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consultorio }) => {
      this.consultorio = consultorio;
      if (consultorio) {
        this.updateForm(consultorio);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consultorio = this.consultorioFormService.getConsultorio(this.editForm);
    if (consultorio.id !== null) {
      this.subscribeToSaveResponse(this.consultorioService.update(consultorio));
    } else {
      this.subscribeToSaveResponse(this.consultorioService.create(consultorio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsultorio>>): void {
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

  protected updateForm(consultorio: IConsultorio): void {
    this.consultorio = consultorio;
    this.consultorioFormService.resetForm(this.editForm, consultorio);
  }
}
