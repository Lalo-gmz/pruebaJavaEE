import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IDoctor } from 'app/entities/doctor/doctor.model';
import { DoctorService } from 'app/entities/doctor/service/doctor.service';
import { IConsultorio } from 'app/entities/consultorio/consultorio.model';
import { ConsultorioService } from 'app/entities/consultorio/service/consultorio.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ICita } from '../cita.model';
import { CitaService } from '../service/cita.service';
import { CitaFormService } from './cita-form.service';

import { CitaUpdateComponent } from './cita-update.component';

describe('Cita Management Update Component', () => {
  let comp: CitaUpdateComponent;
  let fixture: ComponentFixture<CitaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let citaFormService: CitaFormService;
  let citaService: CitaService;
  let doctorService: DoctorService;
  let consultorioService: ConsultorioService;
  let pacienteService: PacienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CitaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CitaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CitaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    citaFormService = TestBed.inject(CitaFormService);
    citaService = TestBed.inject(CitaService);
    doctorService = TestBed.inject(DoctorService);
    consultorioService = TestBed.inject(ConsultorioService);
    pacienteService = TestBed.inject(PacienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Doctor query and add missing value', () => {
      const cita: ICita = { id: 456 };
      const doctor: IDoctor = { id: 2571 };
      cita.doctor = doctor;

      const doctorCollection: IDoctor[] = [{ id: 32422 }];
      jest.spyOn(doctorService, 'query').mockReturnValue(of(new HttpResponse({ body: doctorCollection })));
      const additionalDoctors = [doctor];
      const expectedCollection: IDoctor[] = [...additionalDoctors, ...doctorCollection];
      jest.spyOn(doctorService, 'addDoctorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(doctorService.query).toHaveBeenCalled();
      expect(doctorService.addDoctorToCollectionIfMissing).toHaveBeenCalledWith(
        doctorCollection,
        ...additionalDoctors.map(expect.objectContaining),
      );
      expect(comp.doctorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Consultorio query and add missing value', () => {
      const cita: ICita = { id: 456 };
      const consultorio: IConsultorio = { id: 7957 };
      cita.consultorio = consultorio;

      const consultorioCollection: IConsultorio[] = [{ id: 11473 }];
      jest.spyOn(consultorioService, 'query').mockReturnValue(of(new HttpResponse({ body: consultorioCollection })));
      const additionalConsultorios = [consultorio];
      const expectedCollection: IConsultorio[] = [...additionalConsultorios, ...consultorioCollection];
      jest.spyOn(consultorioService, 'addConsultorioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(consultorioService.query).toHaveBeenCalled();
      expect(consultorioService.addConsultorioToCollectionIfMissing).toHaveBeenCalledWith(
        consultorioCollection,
        ...additionalConsultorios.map(expect.objectContaining),
      );
      expect(comp.consultoriosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Paciente query and add missing value', () => {
      const cita: ICita = { id: 456 };
      const paciente: IPaciente = { id: 30750 };
      cita.paciente = paciente;

      const pacienteCollection: IPaciente[] = [{ id: 22140 }];
      jest.spyOn(pacienteService, 'query').mockReturnValue(of(new HttpResponse({ body: pacienteCollection })));
      const additionalPacientes = [paciente];
      const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
      jest.spyOn(pacienteService, 'addPacienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(pacienteService.query).toHaveBeenCalled();
      expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(
        pacienteCollection,
        ...additionalPacientes.map(expect.objectContaining),
      );
      expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cita: ICita = { id: 456 };
      const doctor: IDoctor = { id: 31920 };
      cita.doctor = doctor;
      const consultorio: IConsultorio = { id: 32495 };
      cita.consultorio = consultorio;
      const paciente: IPaciente = { id: 2555 };
      cita.paciente = paciente;

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(comp.doctorsSharedCollection).toContain(doctor);
      expect(comp.consultoriosSharedCollection).toContain(consultorio);
      expect(comp.pacientesSharedCollection).toContain(paciente);
      expect(comp.cita).toEqual(cita);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICita>>();
      const cita = { id: 123 };
      jest.spyOn(citaFormService, 'getCita').mockReturnValue(cita);
      jest.spyOn(citaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cita }));
      saveSubject.complete();

      // THEN
      expect(citaFormService.getCita).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(citaService.update).toHaveBeenCalledWith(expect.objectContaining(cita));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICita>>();
      const cita = { id: 123 };
      jest.spyOn(citaFormService, 'getCita').mockReturnValue({ id: null });
      jest.spyOn(citaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cita: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cita }));
      saveSubject.complete();

      // THEN
      expect(citaFormService.getCita).toHaveBeenCalled();
      expect(citaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICita>>();
      const cita = { id: 123 };
      jest.spyOn(citaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(citaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDoctor', () => {
      it('Should forward to doctorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(doctorService, 'compareDoctor');
        comp.compareDoctor(entity, entity2);
        expect(doctorService.compareDoctor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareConsultorio', () => {
      it('Should forward to consultorioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(consultorioService, 'compareConsultorio');
        comp.compareConsultorio(entity, entity2);
        expect(consultorioService.compareConsultorio).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePaciente', () => {
      it('Should forward to pacienteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pacienteService, 'comparePaciente');
        comp.comparePaciente(entity, entity2);
        expect(pacienteService.comparePaciente).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
