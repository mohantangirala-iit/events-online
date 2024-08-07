import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'eventsOnlineApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'conference',
    data: { pageTitle: 'eventsOnlineApp.conference.home.title' },
    loadChildren: () => import('./conference/conference.routes'),
  },
  {
    path: 'location',
    data: { pageTitle: 'eventsOnlineApp.location.home.title' },
    loadChildren: () => import('./location/location.routes'),
  },
  {
    path: 'event',
    data: { pageTitle: 'eventsOnlineApp.event.home.title' },
    loadChildren: () => import('./event/event.routes'),
  },
  {
    path: 'application-user',
    data: { pageTitle: 'eventsOnlineApp.applicationUser.home.title' },
    loadChildren: () => import('./application-user/application-user.routes'),
  },
  {
    path: 'person-type',
    data: { pageTitle: 'eventsOnlineApp.personType.home.title' },
    loadChildren: () => import('./person-type/person-type.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
