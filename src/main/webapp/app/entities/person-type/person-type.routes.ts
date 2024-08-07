import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PersonTypeComponent } from './list/person-type.component';
import { PersonTypeDetailComponent } from './detail/person-type-detail.component';
import { PersonTypeUpdateComponent } from './update/person-type-update.component';
import PersonTypeResolve from './route/person-type-routing-resolve.service';

const personTypeRoute: Routes = [
  {
    path: '',
    component: PersonTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonTypeDetailComponent,
    resolve: {
      personType: PersonTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonTypeUpdateComponent,
    resolve: {
      personType: PersonTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonTypeUpdateComponent,
    resolve: {
      personType: PersonTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default personTypeRoute;
