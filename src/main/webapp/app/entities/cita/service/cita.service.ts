import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICita, NewCita } from '../cita.model';

export type PartialUpdateCita = Partial<ICita> & Pick<ICita, 'id'>;

type RestOf<T extends ICita | NewCita> = Omit<T, 'horarioConsultaInicio' | 'horarioConsultaFin'> & {
  horarioConsultaInicio?: string | null;
  horarioConsultaFin?: string | null;
};

export type RestCita = RestOf<ICita>;

export type NewRestCita = RestOf<NewCita>;

export type PartialUpdateRestCita = RestOf<PartialUpdateCita>;

export type EntityResponseType = HttpResponse<ICita>;
export type EntityArrayResponseType = HttpResponse<ICita[]>;

@Injectable({ providedIn: 'root' })
export class CitaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/citas');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(cita: NewCita): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cita);
    return this.http.post<RestCita>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cita: ICita): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cita);
    return this.http
      .put<RestCita>(`${this.resourceUrl}/${this.getCitaIdentifier(cita)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cita: PartialUpdateCita): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cita);
    return this.http
      .patch<RestCita>(`${this.resourceUrl}/${this.getCitaIdentifier(cita)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCita>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCita[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCitaIdentifier(cita: Pick<ICita, 'id'>): number {
    return cita.id;
  }

  compareCita(o1: Pick<ICita, 'id'> | null, o2: Pick<ICita, 'id'> | null): boolean {
    return o1 && o2 ? this.getCitaIdentifier(o1) === this.getCitaIdentifier(o2) : o1 === o2;
  }

  addCitaToCollectionIfMissing<Type extends Pick<ICita, 'id'>>(
    citaCollection: Type[],
    ...citasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const citas: Type[] = citasToCheck.filter(isPresent);
    if (citas.length > 0) {
      const citaCollectionIdentifiers = citaCollection.map(citaItem => this.getCitaIdentifier(citaItem)!);
      const citasToAdd = citas.filter(citaItem => {
        const citaIdentifier = this.getCitaIdentifier(citaItem);
        if (citaCollectionIdentifiers.includes(citaIdentifier)) {
          return false;
        }
        citaCollectionIdentifiers.push(citaIdentifier);
        return true;
      });
      return [...citasToAdd, ...citaCollection];
    }
    return citaCollection;
  }

  protected convertDateFromClient<T extends ICita | NewCita | PartialUpdateCita>(cita: T): RestOf<T> {
    return {
      ...cita,
      horarioConsultaInicio: cita.horarioConsultaInicio?.toJSON() ?? null,
      horarioConsultaFin: cita.horarioConsultaFin?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCita: RestCita): ICita {
    return {
      ...restCita,
      horarioConsultaInicio: restCita.horarioConsultaInicio ? dayjs(restCita.horarioConsultaInicio) : undefined,
      horarioConsultaFin: restCita.horarioConsultaFin ? dayjs(restCita.horarioConsultaFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCita>): HttpResponse<ICita> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCita[]>): HttpResponse<ICita[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
