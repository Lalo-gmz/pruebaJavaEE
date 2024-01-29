import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CitaDetailComponent } from './cita-detail.component';

describe('Cita Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CitaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CitaDetailComponent,
              resolve: { cita: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CitaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load cita on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CitaDetailComponent);

      // THEN
      expect(instance.cita).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
