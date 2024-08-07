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

describe('Conference e2e test', () => {
  const conferencePageUrl = '/conference';
  const conferencePageUrlPattern = new RegExp('/conference(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const conferenceSample = { conferenceName: 'till uh-huh' };

  let conference;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/conferences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/conferences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/conferences/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (conference) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/conferences/${conference.id}`,
      }).then(() => {
        conference = undefined;
      });
    }
  });

  it('Conferences menu should load Conferences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('conference');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Conference').should('exist');
    cy.url().should('match', conferencePageUrlPattern);
  });

  describe('Conference page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(conferencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Conference page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/conference/new$'));
        cy.getEntityCreateUpdateHeading('Conference');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', conferencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/conferences',
          body: conferenceSample,
        }).then(({ body }) => {
          conference = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/conferences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/conferences?page=0&size=20>; rel="last",<http://localhost/api/conferences?page=0&size=20>; rel="first"',
              },
              body: [conference],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(conferencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Conference page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('conference');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', conferencePageUrlPattern);
      });

      it('edit button click should load edit Conference page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Conference');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', conferencePageUrlPattern);
      });

      it.skip('edit button click should load edit Conference page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Conference');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', conferencePageUrlPattern);
      });

      it('last delete button click should delete instance of Conference', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('conference').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', conferencePageUrlPattern);

        conference = undefined;
      });
    });
  });

  describe('new Conference page', () => {
    beforeEach(() => {
      cy.visit(`${conferencePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Conference');
    });

    it('should create an instance of Conference', () => {
      cy.get(`[data-cy="conferenceName"]`).type('amongst');
      cy.get(`[data-cy="conferenceName"]`).should('have.value', 'amongst');

      cy.get(`[data-cy="startDate"]`).type('2024-08-04T20:29');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-08-04T20:29');

      cy.get(`[data-cy="endDate"]`).type('2024-08-04T05:13');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-08-04T05:13');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        conference = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', conferencePageUrlPattern);
    });
  });
});
