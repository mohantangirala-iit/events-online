import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEvent, NewEvent } from '../event.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvent for edit and NewEventFormGroupInput for create.
 */
type EventFormGroupInput = IEvent | PartialWithRequiredKeyOf<NewEvent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEvent | NewEvent> = Omit<T, 'date' | 'startTime' | 'endTime'> & {
  date?: string | null;
  startTime?: string | null;
  endTime?: string | null;
};

type EventFormRawValue = FormValueOf<IEvent>;

type NewEventFormRawValue = FormValueOf<NewEvent>;

type EventFormDefaults = Pick<NewEvent, 'id' | 'date' | 'startTime' | 'endTime'>;

type EventFormGroupContent = {
  id: FormControl<EventFormRawValue['id'] | NewEvent['id']>;
  title: FormControl<EventFormRawValue['title']>;
  description: FormControl<EventFormRawValue['description']>;
  audience: FormControl<EventFormRawValue['audience']>;
  level: FormControl<EventFormRawValue['level']>;
  language: FormControl<EventFormRawValue['language']>;
  date: FormControl<EventFormRawValue['date']>;
  startTime: FormControl<EventFormRawValue['startTime']>;
  endTime: FormControl<EventFormRawValue['endTime']>;
  eventType: FormControl<EventFormRawValue['eventType']>;
  conference: FormControl<EventFormRawValue['conference']>;
};

export type EventFormGroup = FormGroup<EventFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventFormService {
  createEventFormGroup(event: EventFormGroupInput = { id: null }): EventFormGroup {
    const eventRawValue = this.convertEventToEventRawValue({
      ...this.getFormDefaults(),
      ...event,
    });
    return new FormGroup<EventFormGroupContent>({
      id: new FormControl(
        { value: eventRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(eventRawValue.title),
      description: new FormControl(eventRawValue.description),
      audience: new FormControl(eventRawValue.audience),
      level: new FormControl(eventRawValue.level),
      language: new FormControl(eventRawValue.language),
      date: new FormControl(eventRawValue.date),
      startTime: new FormControl(eventRawValue.startTime),
      endTime: new FormControl(eventRawValue.endTime),
      eventType: new FormControl(eventRawValue.eventType),
      conference: new FormControl(eventRawValue.conference),
    });
  }

  getEvent(form: EventFormGroup): IEvent | NewEvent {
    return this.convertEventRawValueToEvent(form.getRawValue() as EventFormRawValue | NewEventFormRawValue);
  }

  resetForm(form: EventFormGroup, event: EventFormGroupInput): void {
    const eventRawValue = this.convertEventToEventRawValue({ ...this.getFormDefaults(), ...event });
    form.reset(
      {
        ...eventRawValue,
        id: { value: eventRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EventFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertEventRawValueToEvent(rawEvent: EventFormRawValue | NewEventFormRawValue): IEvent | NewEvent {
    return {
      ...rawEvent,
      date: dayjs(rawEvent.date, DATE_TIME_FORMAT),
      startTime: dayjs(rawEvent.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawEvent.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertEventToEventRawValue(
    event: IEvent | (Partial<NewEvent> & EventFormDefaults),
  ): EventFormRawValue | PartialWithRequiredKeyOf<NewEventFormRawValue> {
    return {
      ...event,
      date: event.date ? event.date.format(DATE_TIME_FORMAT) : undefined,
      startTime: event.startTime ? event.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: event.endTime ? event.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
