import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPersonType } from 'app/entities/person-type/person-type.model';
import { PersonTypeService } from 'app/entities/person-type/service/person-type.service';
import { IConference } from 'app/entities/conference/conference.model';
import { ConferenceService } from 'app/entities/conference/service/conference.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IApplicationUser } from '../application-user.model';
import { ApplicationUserService } from '../service/application-user.service';
import { ApplicationUserFormService } from './application-user-form.service';

import { ApplicationUserUpdateComponent } from './application-user-update.component';

describe('ApplicationUser Management Update Component', () => {
  let comp: ApplicationUserUpdateComponent;
  let fixture: ComponentFixture<ApplicationUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicationUserFormService: ApplicationUserFormService;
  let applicationUserService: ApplicationUserService;
  let personTypeService: PersonTypeService;
  let conferenceService: ConferenceService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ApplicationUserUpdateComponent],
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
      .overrideTemplate(ApplicationUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicationUserFormService = TestBed.inject(ApplicationUserFormService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    personTypeService = TestBed.inject(PersonTypeService);
    conferenceService = TestBed.inject(ConferenceService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call persontype query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const persontype: IPersonType = { id: 26801 };
      applicationUser.persontype = persontype;

      const persontypeCollection: IPersonType[] = [{ id: 28128 }];
      jest.spyOn(personTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: persontypeCollection })));
      const expectedCollection: IPersonType[] = [persontype, ...persontypeCollection];
      jest.spyOn(personTypeService, 'addPersonTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(personTypeService.query).toHaveBeenCalled();
      expect(personTypeService.addPersonTypeToCollectionIfMissing).toHaveBeenCalledWith(persontypeCollection, persontype);
      expect(comp.persontypesCollection).toEqual(expectedCollection);
    });

    it('Should call person query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const person: IConference = { id: 29393 };
      applicationUser.person = person;

      const personCollection: IConference[] = [{ id: 17047 }];
      jest.spyOn(conferenceService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const expectedCollection: IConference[] = [person, ...personCollection];
      jest.spyOn(conferenceService, 'addConferenceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(conferenceService.query).toHaveBeenCalled();
      expect(conferenceService.addConferenceToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
      expect(comp.peopleCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const internalUser: IUser = { id: 23787 };
      applicationUser.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 22253 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const persontype: IPersonType = { id: 5512 };
      applicationUser.persontype = persontype;
      const person: IConference = { id: 19363 };
      applicationUser.person = person;
      const internalUser: IUser = { id: 18330 };
      applicationUser.internalUser = internalUser;

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(comp.persontypesCollection).toContain(persontype);
      expect(comp.peopleCollection).toContain(person);
      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.applicationUser).toEqual(applicationUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicationUser>>();
      const applicationUser = { id: 123 };
      jest.spyOn(applicationUserFormService, 'getApplicationUser').mockReturnValue(applicationUser);
      jest.spyOn(applicationUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationUser }));
      saveSubject.complete();

      // THEN
      expect(applicationUserFormService.getApplicationUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(applicationUserService.update).toHaveBeenCalledWith(expect.objectContaining(applicationUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicationUser>>();
      const applicationUser = { id: 123 };
      jest.spyOn(applicationUserFormService, 'getApplicationUser').mockReturnValue({ id: null });
      jest.spyOn(applicationUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationUser }));
      saveSubject.complete();

      // THEN
      expect(applicationUserFormService.getApplicationUser).toHaveBeenCalled();
      expect(applicationUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicationUser>>();
      const applicationUser = { id: 123 };
      jest.spyOn(applicationUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(applicationUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePersonType', () => {
      it('Should forward to personTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(personTypeService, 'comparePersonType');
        comp.comparePersonType(entity, entity2);
        expect(personTypeService.comparePersonType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareConference', () => {
      it('Should forward to conferenceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(conferenceService, 'compareConference');
        comp.compareConference(entity, entity2);
        expect(conferenceService.compareConference).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
