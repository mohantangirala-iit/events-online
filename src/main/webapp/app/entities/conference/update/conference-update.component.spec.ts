import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { ConferenceService } from '../service/conference.service';
import { IConference } from '../conference.model';
import { ConferenceFormService } from './conference-form.service';

import { ConferenceUpdateComponent } from './conference-update.component';

describe('Conference Management Update Component', () => {
  let comp: ConferenceUpdateComponent;
  let fixture: ComponentFixture<ConferenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conferenceFormService: ConferenceFormService;
  let conferenceService: ConferenceService;
  let locationService: LocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ConferenceUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ConferenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConferenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conferenceFormService = TestBed.inject(ConferenceFormService);
    conferenceService = TestBed.inject(ConferenceService);
    locationService = TestBed.inject(LocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call location query and add missing value', () => {
      const conference: IConference = { id: 456 };
      const location: ILocation = { id: 23143 };
      conference.location = location;

      const locationCollection: ILocation[] = [{ id: 19227 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const expectedCollection: ILocation[] = [location, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ conference });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(locationCollection, location);
      expect(comp.locationsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const conference: IConference = { id: 456 };
      const location: ILocation = { id: 14723 };
      conference.location = location;

      activatedRoute.data = of({ conference });
      comp.ngOnInit();

      expect(comp.locationsCollection).toContain(location);
      expect(comp.conference).toEqual(conference);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConference>>();
      const conference = { id: 123 };
      jest.spyOn(conferenceFormService, 'getConference').mockReturnValue(conference);
      jest.spyOn(conferenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conference }));
      saveSubject.complete();

      // THEN
      expect(conferenceFormService.getConference).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(conferenceService.update).toHaveBeenCalledWith(expect.objectContaining(conference));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConference>>();
      const conference = { id: 123 };
      jest.spyOn(conferenceFormService, 'getConference').mockReturnValue({ id: null });
      jest.spyOn(conferenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conference: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conference }));
      saveSubject.complete();

      // THEN
      expect(conferenceFormService.getConference).toHaveBeenCalled();
      expect(conferenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConference>>();
      const conference = { id: 123 };
      jest.spyOn(conferenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conferenceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLocation', () => {
      it('Should forward to locationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(locationService, 'compareLocation');
        comp.compareLocation(entity, entity2);
        expect(locationService.compareLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
