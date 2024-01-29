import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConsultorioService } from '../service/consultorio.service';
import { IConsultorio } from '../consultorio.model';
import { ConsultorioFormService } from './consultorio-form.service';

import { ConsultorioUpdateComponent } from './consultorio-update.component';

describe('Consultorio Management Update Component', () => {
  let comp: ConsultorioUpdateComponent;
  let fixture: ComponentFixture<ConsultorioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let consultorioFormService: ConsultorioFormService;
  let consultorioService: ConsultorioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ConsultorioUpdateComponent],
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
      .overrideTemplate(ConsultorioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConsultorioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    consultorioFormService = TestBed.inject(ConsultorioFormService);
    consultorioService = TestBed.inject(ConsultorioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const consultorio: IConsultorio = { id: 456 };

      activatedRoute.data = of({ consultorio });
      comp.ngOnInit();

      expect(comp.consultorio).toEqual(consultorio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsultorio>>();
      const consultorio = { id: 123 };
      jest.spyOn(consultorioFormService, 'getConsultorio').mockReturnValue(consultorio);
      jest.spyOn(consultorioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consultorio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consultorio }));
      saveSubject.complete();

      // THEN
      expect(consultorioFormService.getConsultorio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(consultorioService.update).toHaveBeenCalledWith(expect.objectContaining(consultorio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsultorio>>();
      const consultorio = { id: 123 };
      jest.spyOn(consultorioFormService, 'getConsultorio').mockReturnValue({ id: null });
      jest.spyOn(consultorioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consultorio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consultorio }));
      saveSubject.complete();

      // THEN
      expect(consultorioFormService.getConsultorio).toHaveBeenCalled();
      expect(consultorioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsultorio>>();
      const consultorio = { id: 123 };
      jest.spyOn(consultorioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consultorio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(consultorioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
