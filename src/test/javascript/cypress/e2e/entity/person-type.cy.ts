import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('PersonType e2e test', () => {
  const personTypePageUrl = '/person-type';
  const personTypePageUrlPattern = new RegExp('/person-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const personTypeSample = {};

  let personType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/person-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/person-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/person-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (personType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/person-types/${personType.id}`,
      }).then(() => {
        personType = undefined;
      });
    }
  });

  it('PersonTypes menu should load PersonTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('person-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PersonType').should('exist');
    cy.url().should('match', personTypePageUrlPattern);
  });

  describe('PersonType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(personTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PersonType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/person-type/new$'));
        cy.getEntityCreateUpdateHeading('PersonType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/person-types',
          body: personTypeSample,
        }).then(({ body }) => {
          personType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/person-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [personType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(personTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PersonType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('personType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personTypePageUrlPattern);
      });

      it('edit button click should load edit PersonType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personTypePageUrlPattern);
      });

      it('edit button click should load edit PersonType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personTypePageUrlPattern);
      });

      it('last delete button click should delete instance of PersonType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('personType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personTypePageUrlPattern);

        personType = undefined;
      });
    });
  });

  describe('new PersonType page', () => {
    beforeEach(() => {
      cy.visit(`${personTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PersonType');
    });

    it('should create an instance of PersonType', () => {
      cy.get(`[data-cy="jobTitle"]`).type('4412');
      cy.get(`[data-cy="jobTitle"]`).should('have.value', '4412');

      cy.get(`[data-cy="role"]`).select('GUEST');

      cy.get(`[data-cy="level"]`).select('BASIC');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        personType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', personTypePageUrlPattern);
    });
  });
});
