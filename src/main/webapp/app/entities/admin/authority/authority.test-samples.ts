import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'f23db1b3-35e7-49a2-9574-7239cc6b85ca',
};

export const sampleWithPartialData: IAuthority = {
  name: '8f2bc98a-997a-4ca6-bea0-da339a039803',
};

export const sampleWithFullData: IAuthority = {
  name: 'd121c0bc-d1ec-4706-8699-1e454baf2395',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
