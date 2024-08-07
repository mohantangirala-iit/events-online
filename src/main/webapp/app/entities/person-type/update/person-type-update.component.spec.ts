import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { PersonTypeService } from '../service/person-type.service';
import { IPersonType } from '../person-type.model';
import { PersonTypeFormService } from './person-type-form.service';

import { PersonTypeUpdateComponent } from './person-type-update.component';

describe('PersonType Management Update Component', () => {
  let comp: PersonTypeUpdateComponent;
  let fixture: ComponentFixture<PersonTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personTypeFormService: PersonTypeFormService;
  let personTypeService: PersonTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PersonTypeUpdateComponent],
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
      .overrideTemplate(PersonTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personTypeFormService = TestBed.inject(PersonTypeFormService);
    personTypeService = TestBed.inject(PersonTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const personType: IPersonType = { id: 456 };

      activatedRoute.data = of({ personType });
      comp.ngOnInit();

      expect(comp.personType).toEqual(personType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonType>>();
      const personType = { id: 123 };
      jest.spyOn(personTypeFormService, 'getPersonType').mockReturnValue(personType);
      jest.spyOn(personTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personType }));
      saveSubject.complete();

      // THEN
      expect(personTypeFormService.getPersonType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(personTypeService.update).toHaveBeenCalledWith(expect.objectContaining(personType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonType>>();
      const personType = { id: 123 };
      jest.spyOn(personTypeFormService, 'getPersonType').mockReturnValue({ id: null });
      jest.spyOn(personTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personType }));
      saveSubject.complete();

      // THEN
      expect(personTypeFormService.getPersonType).toHaveBeenCalled();
      expect(personTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonType>>();
      const personType = { id: 123 };
      jest.spyOn(personTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
