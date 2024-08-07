import dayjs from 'dayjs/esm';

import { IConference, NewConference } from './conference.model';

export const sampleWithRequiredData: IConference = {
  id: 4310,
  conferenceName: 'fluid sulk',
};

export const sampleWithPartialData: IConference = {
  id: 23625,
  conferenceName: 'hm considering around',
};

export const sampleWithFullData: IConference = {
  id: 9152,
  conferenceName: 'whether afraid',
  startDate: dayjs('2024-08-04T20:20'),
  endDate: dayjs('2024-08-04T21:46'),
};

export const sampleWithNewData: NewConference = {
  conferenceName: 'unimpressively',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
