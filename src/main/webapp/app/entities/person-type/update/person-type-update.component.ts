import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Role } from 'app/entities/enumerations/role.model';
import { Level } from 'app/entities/enumerations/level.model';
import { IPersonType } from '../person-type.model';
import { PersonTypeService } from '../service/person-type.service';
import { PersonTypeFormService, PersonTypeFormGroup } from './person-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-person-type-update',
  templateUrl: './person-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PersonTypeUpdateComponent implements OnInit {
  isSaving = false;
  personType: IPersonType | null = null;
  roleValues = Object.keys(Role);
  levelValues = Object.keys(Level);

  protected personTypeService = inject(PersonTypeService);
  protected personTypeFormService = inject(PersonTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PersonTypeFormGroup = this.personTypeFormService.createPersonTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personType }) => {
      this.personType = personType;
      if (personType) {
        this.updateForm(personType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personType = this.personTypeFormService.getPersonType(this.editForm);
    if (personType.id !== null) {
      this.subscribeToSaveResponse(this.personTypeService.update(personType));
    } else {
      this.subscribeToSaveResponse(this.personTypeService.create(personType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(personType: IPersonType): void {
    this.personType = personType;
    this.personTypeFormService.resetForm(this.editForm, personType);
  }
}
