import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConsultorioDetailComponent } from './consultorio-detail.component';

describe('Consultorio Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultorioDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConsultorioDetailComponent,
              resolve: { consultorio: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConsultorioDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load consultorio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConsultorioDetailComponent);

      // THEN
      expect(instance.consultorio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
