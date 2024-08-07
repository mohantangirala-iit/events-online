import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConference, NewConference } from '../conference.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConference for edit and NewConferenceFormGroupInput for create.
 */
type ConferenceFormGroupInput = IConference | PartialWithRequiredKeyOf<NewConference>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IConference | NewConference> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type ConferenceFormRawValue = FormValueOf<IConference>;

type NewConferenceFormRawValue = FormValueOf<NewConference>;

type ConferenceFormDefaults = Pick<NewConference, 'id' | 'startDate' | 'endDate'>;

type ConferenceFormGroupContent = {
  id: FormControl<ConferenceFormRawValue['id'] | NewConference['id']>;
  conferenceName: FormControl<ConferenceFormRawValue['conferenceName']>;
  startDate: FormControl<ConferenceFormRawValue['startDate']>;
  endDate: FormControl<ConferenceFormRawValue['endDate']>;
  location: FormControl<ConferenceFormRawValue['location']>;
};

export type ConferenceFormGroup = FormGroup<ConferenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConferenceFormService {
  createConferenceFormGroup(conference: ConferenceFormGroupInput = { id: null }): ConferenceFormGroup {
    const conferenceRawValue = this.convertConferenceToConferenceRawValue({
      ...this.getFormDefaults(),
      ...conference,
    });
    return new FormGroup<ConferenceFormGroupContent>({
      id: new FormControl(
        { value: conferenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      conferenceName: new FormControl(conferenceRawValue.conferenceName, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(conferenceRawValue.startDate),
      endDate: new FormControl(conferenceRawValue.endDate),
      location: new FormControl(conferenceRawValue.location),
    });
  }

  getConference(form: ConferenceFormGroup): IConference | NewConference {
    return this.convertConferenceRawValueToConference(form.getRawValue() as ConferenceFormRawValue | NewConferenceFormRawValue);
  }

  resetForm(form: ConferenceFormGroup, conference: ConferenceFormGroupInput): void {
    const conferenceRawValue = this.convertConferenceToConferenceRawValue({ ...this.getFormDefaults(), ...conference });
    form.reset(
      {
        ...conferenceRawValue,
        id: { value: conferenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConferenceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertConferenceRawValueToConference(
    rawConference: ConferenceFormRawValue | NewConferenceFormRawValue,
  ): IConference | NewConference {
    return {
      ...rawConference,
      startDate: dayjs(rawConference.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawConference.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertConferenceToConferenceRawValue(
    conference: IConference | (Partial<NewConference> & ConferenceFormDefaults),
  ): ConferenceFormRawValue | PartialWithRequiredKeyOf<NewConferenceFormRawValue> {
    return {
      ...conference,
      startDate: conference.startDate ? conference.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: conference.endDate ? conference.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
