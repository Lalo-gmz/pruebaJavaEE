import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { ICita } from '../cita.model';
import { EntityArrayResponseType, CitaService } from '../service/cita.service';
import { CitaDeleteDialogComponent } from '../delete/cita-delete-dialog.component';
import { IDoctor } from 'app/entities/doctor/doctor.model';
import dayjs from 'dayjs';

@Component({
  standalone: true,
  selector: 'jhi-cita',
  templateUrl: './cita.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
  ],
})
export class CitaConsultarComponent implements OnInit {
  citas?: ICita[];
  citasFilter?: ICita[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  fInicio: any;
  fFin: any;
  consultorio?: number;
  doctor: string = '';
  citasHoy: number = 0;
  citasManana: number = 0;

  constructor(
    protected citaService: CitaService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
  ) {}

  trackId = (_index: number, item: ICita): number => this.citaService.getCitaIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  buscarPorNombreDoctor(): void {
    if (this.doctor !== '') {
      this.citas = this.citasFilter;
    } else {
      this.buscarCitasPorDoctor(this.doctor);
      this.buscarCitasDeHoy();
    }
  }

  buscarCitasPorDoctor(doctor: string): void {
    this.citas = this.citasFilter?.filter(cita => {
      if (cita.doctor) {
        this.getNombreCompletoDoctor(cita.doctor).toLowerCase().includes(doctor.toLowerCase());
      }
    });
  }

  buscarCitasDeHoy(): void {
    const fechaHoy = dayjs();
    const fechaHoyFormato = fechaHoy.format('YYYY-MM-DD');

    this.citasHoy =
      this.citas?.filter(cita => {
        // Verificar si la fecha de inicio de la cita no es undefined
        if (cita.horarioConsultaInicio) {
          const fechaInicioCita = cita.horarioConsultaInicio.toDate(); // Convertir de Dayjs a Date

          // Formato la fecha de la cita para comparar solo con la fecha actual
          const fechaCitaFormato = dayjs(fechaInicioCita).format('YYYY-MM-DD');

          return fechaCitaFormato === fechaHoyFormato;
        }

        return false; // Si la fecha de inicio de la cita es undefined, no es de hoy
      })?.length || 0; // Si no hay coincidencias, establece citasHoy en 0
  }

  getNombreCompletoDoctor(doctorCheck: IDoctor | null): string {
    if (doctorCheck) {
      const nombre = doctorCheck.nombre || '';
      const apellidoPaterno = doctorCheck.apellidoPaterno || '';
      const apellidoMaterno = doctorCheck.apellidoMaterno || '';

      return `${nombre} ${apellidoPaterno} ${apellidoMaterno}`.trim();
    }

    return '';
  }

  delete(cita: ICita): void {
    const modalRef = this.modalService.open(CitaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cita = cita;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations()),
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending)),
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.citas = dataFromBody;
    this.citasFilter = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: ICita[] | null): ICita[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.citaService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
