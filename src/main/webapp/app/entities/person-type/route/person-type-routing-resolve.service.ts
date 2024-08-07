import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonType } from '../person-type.model';
import { PersonTypeService } from '../service/person-type.service';

const personTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPersonType> => {
  const id = route.params['id'];
  if (id) {
    return inject(PersonTypeService)
      .find(id)
      .pipe(
        mergeMap((personType: HttpResponse<IPersonType>) => {
          if (personType.body) {
            return of(personType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default personTypeResolve;
