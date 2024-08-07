import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 2730,
};

export const sampleWithPartialData: ILocation = {
  id: 29427,
  stateProvince: 'unnerve inferior anenst',
  countryName: 'why offbeat',
};

export const sampleWithFullData: ILocation = {
  id: 19643,
  streetAddress: 'sans',
  postalCode: 'jol playground',
  city: 'Deckowview',
  stateProvince: 'honored blah tumble',
  countryName: 'sturdy gosh',
  region: 'NA',
};

export const sampleWithNewData: NewLocation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
