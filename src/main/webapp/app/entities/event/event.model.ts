import dayjs from 'dayjs/esm';
import { IConference } from 'app/entities/conference/conference.model';
import { Language } from 'app/entities/enumerations/language.model';
import { EventType } from 'app/entities/enumerations/event-type.model';

export interface IEvent {
  id: number;
  title?: string | null;
  description?: string | null;
  audience?: string | null;
  level?: string | null;
  language?: keyof typeof Language | null;
  date?: dayjs.Dayjs | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  eventType?: keyof typeof EventType | null;
  conference?: IConference | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
