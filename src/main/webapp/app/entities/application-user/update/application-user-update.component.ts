import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPersonType } from 'app/entities/person-type/person-type.model';
import { PersonTypeService } from 'app/entities/person-type/service/person-type.service';
import { IConference } from 'app/entities/conference/conference.model';
import { ConferenceService } from 'app/entities/conference/service/conference.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ApplicationUserService } from '../service/application-user.service';
import { IApplicationUser } from '../application-user.model';
import { ApplicationUserFormService, ApplicationUserFormGroup } from './application-user-form.service';

@Component({
  standalone: true,
  selector: 'jhi-application-user-update',
  templateUrl: './application-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApplicationUserUpdateComponent implements OnInit {
  isSaving = false;
  applicationUser: IApplicationUser | null = null;

  persontypesCollection: IPersonType[] = [];
  peopleCollection: IConference[] = [];
  usersSharedCollection: IUser[] = [];

  protected applicationUserService = inject(ApplicationUserService);
  protected applicationUserFormService = inject(ApplicationUserFormService);
  protected personTypeService = inject(PersonTypeService);
  protected conferenceService = inject(ConferenceService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ApplicationUserFormGroup = this.applicationUserFormService.createApplicationUserFormGroup();

  comparePersonType = (o1: IPersonType | null, o2: IPersonType | null): boolean => this.personTypeService.comparePersonType(o1, o2);

  compareConference = (o1: IConference | null, o2: IConference | null): boolean => this.conferenceService.compareConference(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationUser }) => {
      this.applicationUser = applicationUser;
      if (applicationUser) {
        this.updateForm(applicationUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationUser = this.applicationUserFormService.getApplicationUser(this.editForm);
    if (applicationUser.id !== null) {
      this.subscribeToSaveResponse(this.applicationUserService.update(applicationUser));
    } else {
      this.subscribeToSaveResponse(this.applicationUserService.create(applicationUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationUser>>): void {
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

  protected updateForm(applicationUser: IApplicationUser): void {
    this.applicationUser = applicationUser;
    this.applicationUserFormService.resetForm(this.editForm, applicationUser);

    this.persontypesCollection = this.personTypeService.addPersonTypeToCollectionIfMissing<IPersonType>(
      this.persontypesCollection,
      applicationUser.persontype,
    );
    this.peopleCollection = this.conferenceService.addConferenceToCollectionIfMissing<IConference>(
      this.peopleCollection,
      applicationUser.person,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      applicationUser.internalUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personTypeService
      .query({ filter: 'applicationuser-is-null' })
      .pipe(map((res: HttpResponse<IPersonType[]>) => res.body ?? []))
      .pipe(
        map((personTypes: IPersonType[]) =>
          this.personTypeService.addPersonTypeToCollectionIfMissing<IPersonType>(personTypes, this.applicationUser?.persontype),
        ),
      )
      .subscribe((personTypes: IPersonType[]) => (this.persontypesCollection = personTypes));

    this.conferenceService
      .query({ 'applicationUserId.specified': 'false' })
      .pipe(map((res: HttpResponse<IConference[]>) => res.body ?? []))
      .pipe(
        map((conferences: IConference[]) =>
          this.conferenceService.addConferenceToCollectionIfMissing<IConference>(conferences, this.applicationUser?.person),
        ),
      )
      .subscribe((conferences: IConference[]) => (this.peopleCollection = conferences));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.applicationUser?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
