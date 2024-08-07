import dayjs from 'dayjs/esm';
import { ILocation } from 'app/entities/location/location.model';

export interface IConference {
  id: number;
  conferenceName?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  location?: ILocation | null;
}

export type NewConference = Omit<IConference, 'id'> & { id: null };
