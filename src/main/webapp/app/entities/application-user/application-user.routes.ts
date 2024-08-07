import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ApplicationUserComponent } from './list/application-user.component';
import { ApplicationUserDetailComponent } from './detail/application-user-detail.component';
import { ApplicationUserUpdateComponent } from './update/application-user-update.component';
import ApplicationUserResolve from './route/application-user-routing-resolve.service';

const applicationUserRoute: Routes = [
  {
    path: '',
    component: ApplicationUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicationUserDetailComponent,
    resolve: {
      applicationUser: ApplicationUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicationUserUpdateComponent,
    resolve: {
      applicationUser: ApplicationUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicationUserUpdateComponent,
    resolve: {
      applicationUser: ApplicationUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default applicationUserRoute;
