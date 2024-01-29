import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DoctorDetailComponent } from './doctor-detail.component';

describe('Doctor Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoctorDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DoctorDetailComponent,
              resolve: { doctor: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DoctorDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load doctor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DoctorDetailComponent);

      // THEN
      expect(instance.doctor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
