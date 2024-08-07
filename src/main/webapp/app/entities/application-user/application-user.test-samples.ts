import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 8658,
};

export const sampleWithPartialData: IApplicationUser = {
  id: 12606,
  phoneNumber: 'recap',
};

export const sampleWithFullData: IApplicationUser = {
  id: 24287,
  phoneNumber: 'gah why',
};

export const sampleWithNewData: NewApplicationUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
