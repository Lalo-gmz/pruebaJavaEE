import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsultorio, NewConsultorio } from '../consultorio.model';

export type PartialUpdateConsultorio = Partial<IConsultorio> & Pick<IConsultorio, 'id'>;

export type EntityResponseType = HttpResponse<IConsultorio>;
export type EntityArrayResponseType = HttpResponse<IConsultorio[]>;

@Injectable({ providedIn: 'root' })
export class ConsultorioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/consultorios');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(consultorio: NewConsultorio): Observable<EntityResponseType> {
    return this.http.post<IConsultorio>(this.resourceUrl, consultorio, { observe: 'response' });
  }

  update(consultorio: IConsultorio): Observable<EntityResponseType> {
    return this.http.put<IConsultorio>(`${this.resourceUrl}/${this.getConsultorioIdentifier(consultorio)}`, consultorio, {
      observe: 'response',
    });
  }

  partialUpdate(consultorio: PartialUpdateConsultorio): Observable<EntityResponseType> {
    return this.http.patch<IConsultorio>(`${this.resourceUrl}/${this.getConsultorioIdentifier(consultorio)}`, consultorio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConsultorio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConsultorio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConsultorioIdentifier(consultorio: Pick<IConsultorio, 'id'>): number {
    return consultorio.id;
  }

  compareConsultorio(o1: Pick<IConsultorio, 'id'> | null, o2: Pick<IConsultorio, 'id'> | null): boolean {
    return o1 && o2 ? this.getConsultorioIdentifier(o1) === this.getConsultorioIdentifier(o2) : o1 === o2;
  }

  addConsultorioToCollectionIfMissing<Type extends Pick<IConsultorio, 'id'>>(
    consultorioCollection: Type[],
    ...consultoriosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const consultorios: Type[] = consultoriosToCheck.filter(isPresent);
    if (consultorios.length > 0) {
      const consultorioCollectionIdentifiers = consultorioCollection.map(
        consultorioItem => this.getConsultorioIdentifier(consultorioItem)!,
      );
      const consultoriosToAdd = consultorios.filter(consultorioItem => {
        const consultorioIdentifier = this.getConsultorioIdentifier(consultorioItem);
        if (consultorioCollectionIdentifiers.includes(consultorioIdentifier)) {
          return false;
        }
        consultorioCollectionIdentifiers.push(consultorioIdentifier);
        return true;
      });
      return [...consultoriosToAdd, ...consultorioCollection];
    }
    return consultorioCollection;
  }
}
