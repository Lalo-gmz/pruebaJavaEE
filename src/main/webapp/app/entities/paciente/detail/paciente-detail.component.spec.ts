import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PacienteDetailComponent } from './paciente-detail.component';

describe('Paciente Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PacienteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PacienteDetailComponent,
              resolve: { paciente: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PacienteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load paciente on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PacienteDetailComponent);

      // THEN
      expect(instance.paciente).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
