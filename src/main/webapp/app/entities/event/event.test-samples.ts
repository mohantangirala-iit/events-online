import dayjs from 'dayjs/esm';

import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 13632,
};

export const sampleWithPartialData: IEvent = {
  id: 4345,
  title: 'till',
  description: 'joyfully scupper',
  level: 'with overconfidently encircle',
  eventType: 'DISCUSSION',
};

export const sampleWithFullData: IEvent = {
  id: 10052,
  title: 'blah recite',
  description: 'angrily once',
  audience: 'opposite',
  level: 'suggestion',
  language: 'SPANISH',
  date: dayjs('2024-08-04T07:32'),
  startTime: dayjs('2024-08-04T10:18'),
  endTime: dayjs('2024-08-04T16:53'),
  eventType: 'DISCUSSION',
};

export const sampleWithNewData: NewEvent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
