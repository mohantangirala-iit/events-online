import { IPersonType, NewPersonType } from './person-type.model';

export const sampleWithRequiredData: IPersonType = {
  id: 1953,
};

export const sampleWithPartialData: IPersonType = {
  id: 11623,
  jobTitle: 15783,
  level: 'ADVANCED',
};

export const sampleWithFullData: IPersonType = {
  id: 21085,
  jobTitle: 2007,
  role: 'MODERATOR',
  level: 'INTERMEDIATE',
};

export const sampleWithNewData: NewPersonType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
