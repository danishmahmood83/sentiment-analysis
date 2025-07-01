require('cypress-xpath');
describe('📊 Stock Sentiment Dashboard E2E Tests', () => {
  beforeEach(() => {
    cy.visit('http://localhost:3000');
  });

  it('renders the main dashboard heading', () => {
    cy.contains('Stock Sentiment Dashboard').should('be.visible');
  });


it('types a symbol, clicks Search, and validates the results dropdown', () => {
  // Step 1: Type the search term
  cy.get('input[placeholder="Enter company name or symbol..."]').type('Apple');

  // Step 2: Click the Search button
  cy.get('button.bg-blue-600.text-white').should('be.visible').click();

  // Step 3: Validate that the dropdown appears and contains valid options
  cy.get('select[class*="w-full"][class*="p-2"][class*="border"][class*="rounded"]')
    .should('be.visible')
    .find('option')
    .should('have.length.greaterThan', 1); // Confirms options beyond placeholder
});

it('searches for a symbol, selects it, and validates Add to Scheduler button appears', () => {
  // Step 1: Type into the input field
  cy.get('input[placeholder="Enter company name or symbol..."]').type('Apple');

  // Step 2: Click the Search button
  cy.get('button.bg-blue-600.text-white').should('be.visible').click();

  // Step 3: Wait for dropdown with AAPL to appear
  cy.get('select').contains('AAPL - Apple Inc. (NASDAQ)').should('exist');

  // Step 4: Select the AAPL option by label
  cy.get('select')
    .filter((_, el) =>
      Array.from(el.options).some(opt => opt.text.includes('AAPL - Apple Inc.'))
    )
    .first()
    .select('0'); // assuming value="0" for AAPL

  // Step 5: Ensure "Add to Scheduler" button appears
  cy.contains('Add to Scheduler').should('be.visible');
});

it('adds selected symbol to scheduler dropdown', () => {
  // Step 1: Type a symbol and search
  cy.get('input[placeholder="Enter company name or symbol..."]').type('Apple');
  cy.get('button.bg-blue-600.text-white').click();

  // Step 2: Wait for and select the first real option
  cy.get('select').contains('Apple').should('exist');
  cy.get('select')
    .filter((_, el) =>
      Array.from(el.options).some(opt => opt.text.includes('AAPL'))
    )
    .first()
    .select('0'); // assuming AAPL value is "0"

  // Step 3: Click "Add to Scheduler" button
  cy.get('button.bg-green-600.text-white').should('be.visible').click();

  // Step 4: Verify AAPL is now present in the 3rd <select> (scheduler list)
  cy.get('#root > div > div:nth-child(3) > select')
    .should('be.visible')
    .find('option')
    .should('contain.text', 'AAPL');
});

it('waits for AAPL to appear in the removal dropdown', () => {
  // First, ensure the dropdown is present and has options
  cy.get('select.w-full.p-2.border.rounded.mb-4')
    .should('be.visible')
    .find('option')
    .should('have.length.greaterThan', 1);

  // Retry until AAPL is available in the option list
  cy.get('select.w-full.p-2.border.rounded.mb-4')
    .find('option')
    .should($options => {
      const hasAAPL = [...$options].some(opt => opt.textContent.includes('AAPL'));
      expect(hasAAPL).to.be.true;
    });


});

it('selects a symbol from the dropdown and ensures the checkbox appears', () => {
  // Step 1: Select the first non-placeholder option
cy.get('select.w-full.p-2.border.rounded.mb-4')
  .should('be.visible')
  .eq(1).select('AAPL');
});

it('selects a symbol and checks that a checkbox appears', () => {
  // Step 1: Select the dropdown by class (or use a more targeted selector if needed)
  cy.get('select.w-full.p-2.border.rounded.mb-4')
    .should('be.visible')
    . eq(1).select('AAPL');

  // Step 2: Verify the checkbox shows up after selection
  cy.get('input[type="checkbox"]')
    .should('exist')
    .and('be.visible');
});

it('selects a symbol and checked checkbox appears', () => {
  // Step 1: Select the dropdown by class (or use a more targeted selector if needed)
  cy.get('select.w-full.p-2.border.rounded.mb-4')
    .should('be.visible')
    . eq(1).select('AAPL');

  // Step 2: Verify the checkbox shows up after selection
 cy.xpath('//*[@id="root"]/div/div[3]/div[1]/label[1]/input')
   .should('exist')
   .and('be.visible')
   .check()
   .should('be.checked');
});


it('selects AAPL, checks the checkbox, and verifies chart appears', () => {
  // Step 1: Select the dropdown and choose AAPL
  cy.get('select.w-full.p-2.border.rounded.mb-4')
    .should('have.length.greaterThan', 1)
    .eq(1)
    .should('be.visible')
    .select('AAPL');

  // Step 2: Check the checkbox that appears for AAPL
  cy.xpath('//*[@id="root"]/div/div[3]/div[1]/label[1]/input')
    .should('exist')
    .and('be.visible')
    .check()
    .should('be.checked');

  // Step 3: Ensure the sentiment chart appears
  cy.xpath('//*[@id="root"]/div/div[3]/div[2]/div/canvas')
    .should('exist')
    .and('be.visible');
});


it('selects AAPL from removal dropdown and checks for Remove button', () => {
  // Step 1: Select AAPL from the second dropdown
  cy.xpath('//*[@id="root"]/div/div[2]/select')
    .should('be.visible')
    .select('AAPL');

  // Step 2: Verify that the Remove button appears
  cy.xpath('//*[@id="root"]/div/div[2]/button')
    .should('exist')
    .and('be.visible')
    .and('contain.text', 'Remove');
});

it('selects AAPL from removal dropdown and clicks Remove button', () => {
  // Step 1: Select AAPL from the scheduler removal dropdown
  // Step 1: Select AAPL from the second dropdown
  cy.xpath('//*[@id="root"]/div/div[2]/select')
    .should('be.visible')
    .select('AAPL');

  // Step 2: Verify that the Remove button appears
  cy.xpath('//*[@id="root"]/div/div[2]/button')
    .should('exist')
    .and('be.visible')
    .and('contain.text', 'Remove')
    .click();
});

it('verifies AAPL is not an option in either dropdown', () => {
  // Check removal dropdown
  cy.xpath('//*[@id="root"]/div/div[2]/select/option')
    .then($options => {
      const values = [...$options].map(opt => opt.value);
      expect(values).to.not.include('AAPL');
    });

  // Check sentiment dropdown
  cy.xpath('//*[@id="root"]/div/div[3]/select/option')
    .then($options => {
      const values = [...$options].map(opt => opt.value);
      expect(values).to.not.include('AAPL');
    });
});


});
