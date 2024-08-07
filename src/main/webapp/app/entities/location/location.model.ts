import { Region } from 'app/entities/enumerations/region.model';

export interface ILocation {
  id: number;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  countryName?: string | null;
  region?: keyof typeof Region | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
