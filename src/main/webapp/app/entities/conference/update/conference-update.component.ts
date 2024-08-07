import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IConference } from '../conference.model';
import { ConferenceService } from '../service/conference.service';
import { ConferenceFormService, ConferenceFormGroup } from './conference-form.service';

@Component({
  standalone: true,
  selector: 'jhi-conference-update',
  templateUrl: './conference-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConferenceUpdateComponent implements OnInit {
  isSaving = false;
  conference: IConference | null = null;

  locationsCollection: ILocation[] = [];

  protected conferenceService = inject(ConferenceService);
  protected conferenceFormService = inject(ConferenceFormService);
  protected locationService = inject(LocationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConferenceFormGroup = this.conferenceFormService.createConferenceFormGroup();

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conference }) => {
      this.conference = conference;
      if (conference) {
        this.updateForm(conference);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conference = this.conferenceFormService.getConference(this.editForm);
    if (conference.id !== null) {
      this.subscribeToSaveResponse(this.conferenceService.update(conference));
    } else {
      this.subscribeToSaveResponse(this.conferenceService.create(conference));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConference>>): void {
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

  protected updateForm(conference: IConference): void {
    this.conference = conference;
    this.conferenceFormService.resetForm(this.editForm, conference);

    this.locationsCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(
      this.locationsCollection,
      conference.location,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query({ 'conferenceId.specified': 'false' })
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, this.conference?.location),
        ),
      )
      .subscribe((locations: ILocation[]) => (this.locationsCollection = locations));
  }
}
