import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPersonType, NewPersonType } from '../person-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPersonType for edit and NewPersonTypeFormGroupInput for create.
 */
type PersonTypeFormGroupInput = IPersonType | PartialWithRequiredKeyOf<NewPersonType>;

type PersonTypeFormDefaults = Pick<NewPersonType, 'id'>;

type PersonTypeFormGroupContent = {
  id: FormControl<IPersonType['id'] | NewPersonType['id']>;
  jobTitle: FormControl<IPersonType['jobTitle']>;
  role: FormControl<IPersonType['role']>;
  level: FormControl<IPersonType['level']>;
};

export type PersonTypeFormGroup = FormGroup<PersonTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonTypeFormService {
  createPersonTypeFormGroup(personType: PersonTypeFormGroupInput = { id: null }): PersonTypeFormGroup {
    const personTypeRawValue = {
      ...this.getFormDefaults(),
      ...personType,
    };
    return new FormGroup<PersonTypeFormGroupContent>({
      id: new FormControl(
        { value: personTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      jobTitle: new FormControl(personTypeRawValue.jobTitle),
      role: new FormControl(personTypeRawValue.role),
      level: new FormControl(personTypeRawValue.level),
    });
  }

  getPersonType(form: PersonTypeFormGroup): IPersonType | NewPersonType {
    return form.getRawValue() as IPersonType | NewPersonType;
  }

  resetForm(form: PersonTypeFormGroup, personType: PersonTypeFormGroupInput): void {
    const personTypeRawValue = { ...this.getFormDefaults(), ...personType };
    form.reset(
      {
        ...personTypeRawValue,
        id: { value: personTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PersonTypeFormDefaults {
    return {
      id: null,
    };
  }
}
