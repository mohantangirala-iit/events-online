import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonType, NewPersonType } from '../person-type.model';

export type PartialUpdatePersonType = Partial<IPersonType> & Pick<IPersonType, 'id'>;

export type EntityResponseType = HttpResponse<IPersonType>;
export type EntityArrayResponseType = HttpResponse<IPersonType[]>;

@Injectable({ providedIn: 'root' })
export class PersonTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/person-types');

  create(personType: NewPersonType): Observable<EntityResponseType> {
    return this.http.post<IPersonType>(this.resourceUrl, personType, { observe: 'response' });
  }

  update(personType: IPersonType): Observable<EntityResponseType> {
    return this.http.put<IPersonType>(`${this.resourceUrl}/${this.getPersonTypeIdentifier(personType)}`, personType, {
      observe: 'response',
    });
  }

  partialUpdate(personType: PartialUpdatePersonType): Observable<EntityResponseType> {
    return this.http.patch<IPersonType>(`${this.resourceUrl}/${this.getPersonTypeIdentifier(personType)}`, personType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPersonTypeIdentifier(personType: Pick<IPersonType, 'id'>): number {
    return personType.id;
  }

  comparePersonType(o1: Pick<IPersonType, 'id'> | null, o2: Pick<IPersonType, 'id'> | null): boolean {
    return o1 && o2 ? this.getPersonTypeIdentifier(o1) === this.getPersonTypeIdentifier(o2) : o1 === o2;
  }

  addPersonTypeToCollectionIfMissing<Type extends Pick<IPersonType, 'id'>>(
    personTypeCollection: Type[],
    ...personTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const personTypes: Type[] = personTypesToCheck.filter(isPresent);
    if (personTypes.length > 0) {
      const personTypeCollectionIdentifiers = personTypeCollection.map(personTypeItem => this.getPersonTypeIdentifier(personTypeItem));
      const personTypesToAdd = personTypes.filter(personTypeItem => {
        const personTypeIdentifier = this.getPersonTypeIdentifier(personTypeItem);
        if (personTypeCollectionIdentifiers.includes(personTypeIdentifier)) {
          return false;
        }
        personTypeCollectionIdentifiers.push(personTypeIdentifier);
        return true;
      });
      return [...personTypesToAdd, ...personTypeCollection];
    }
    return personTypeCollection;
  }
}
