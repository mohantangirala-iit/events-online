import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 27952,
  login: '4T4DQ',
};

export const sampleWithPartialData: IUser = {
  id: 27656,
  login: 'nsl',
};

export const sampleWithFullData: IUser = {
  id: 8182,
  login: 'PJpv-@e1D\\2N\\2f-TmT\\{Wb',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
