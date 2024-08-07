import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConference } from 'app/entities/conference/conference.model';
import { ConferenceService } from 'app/entities/conference/service/conference.service';
import { Language } from 'app/entities/enumerations/language.model';
import { EventType } from 'app/entities/enumerations/event-type.model';
import { EventService } from '../service/event.service';
import { IEvent } from '../event.model';
import { EventFormService, EventFormGroup } from './event-form.service';

@Component({
  standalone: true,
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EventUpdateComponent implements OnInit {
  isSaving = false;
  event: IEvent | null = null;
  languageValues = Object.keys(Language);
  eventTypeValues = Object.keys(EventType);

  conferencesSharedCollection: IConference[] = [];

  protected eventService = inject(EventService);
  protected eventFormService = inject(EventFormService);
  protected conferenceService = inject(ConferenceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EventFormGroup = this.eventFormService.createEventFormGroup();

  compareConference = (o1: IConference | null, o2: IConference | null): boolean => this.conferenceService.compareConference(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      this.event = event;
      if (event) {
        this.updateForm(event);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const event = this.eventFormService.getEvent(this.editForm);
    if (event.id !== null) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>): void {
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

  protected updateForm(event: IEvent): void {
    this.event = event;
    this.eventFormService.resetForm(this.editForm, event);

    this.conferencesSharedCollection = this.conferenceService.addConferenceToCollectionIfMissing<IConference>(
      this.conferencesSharedCollection,
      event.conference,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.conferenceService
      .query()
      .pipe(map((res: HttpResponse<IConference[]>) => res.body ?? []))
      .pipe(
        map((conferences: IConference[]) =>
          this.conferenceService.addConferenceToCollectionIfMissing<IConference>(conferences, this.event?.conference),
        ),
      )
      .subscribe((conferences: IConference[]) => (this.conferencesSharedCollection = conferences));
  }
}
