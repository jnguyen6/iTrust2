package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;

/**
 * Step definitions for a patient to view their obstetrics records.
 *
 * @author Madhura Waghmare (mswaghma)
 */
public class PatientViewObstetricsRecordsStepDefs extends CucumberTest {

    private final String baseUrl       = "http://localhost:8080/iTrust2";
    private final String patientString = "JillBob";
    private final String obgynString   = "tylerOBGYN";


    /**
     * Asserts that the text is on the page
     *
     * @param text
     *            text to check
     */
    public void assertTextPresent ( final String text ) {
        try {
            assertTrue( driver.getPageSource().contains( text ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    @Given ( "^There exists a patient in the system who is eligible for an obstetrics record.$" )
    public void pateintExistsInSystem () {
        attemptLogout();
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.id( "username" ) );
        username.clear();
        username.sendKeys( "tylerOBGYN" );
        final WebElement password = driver.findElement( By.id( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        wait.until( ExpectedConditions.titleContains( "iTrust2: HCP Home" ) );

        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('OBGYNHCPDocumentObstetricsRecord').click();" );
        final WebDriverWait wait2 = new WebDriverWait( driver, 20 );
        wait2.until( ExpectedConditions.titleContains( "iTrust2: View Patient Obstetrics Records" ) );
        waitForAngular();
        assertNotNull( driver.findElement( By.id( patientString ) ) );

        driver.findElement( By.id( "logout" ) ).click();
    }

    @Then ( "^I log on as a patient.$" )
    public void loginPatient () {
        attemptLogout();

        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( patientString );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        assertEquals( "iTrust2: Patient Home", driver.getTitle() );
    }

    @Then ( "^I log in as an OB/GYN HCP to document an obstetrics record for a patient.$" )
    public void obgynLogin () {
        attemptLogout();

        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "tylerOBGYN" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        assertTextPresent( "Welcome to iTrust2 - HCP" );
    }

    @When ( "^I navigate to the document obstetrics record page to document an obstetrics record for a patient.$" )
    public void navigateToPage () {
        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        wait.until( ExpectedConditions.titleContains( "iTrust2: HCP Home" ) );
        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('OBGYNHCPDocumentObstetricsRecord').click();" );
        final WebDriverWait wait2 = new WebDriverWait( driver, 20 );
        wait2.until( ExpectedConditions.titleContains( "iTrust2: View Patient Obstetrics Records" ) );
        assertTextPresent( "Current Pregnancy" );
        assertTextPresent( "Previous Pregnancies" );
    }

    @And ( "^I select radio button with text (.+) to select a patient.$" )
    public void selectPatient ( final String username ) {
        waitForAngular();
        driver.findElement( By.id( username ) ).click();
    }

    @And ( "^I enter (.+) for a current obstetrics record for the patient.$" )
    public void enterLMP ( final String date ) {
    	final WebElement dateElement = driver.findElement( By.name( "currentlmp" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    @And ( "^I click create obstetrics record to document an obstetrics record for a patient.$" )
    public void clickToCreateObstetricsRecord () {
        waitForAngular();
        driver.findElement( By.name( "submit" ) );
    }

    @And ( "^I enter (.+), (\\d+), (\\d+), (\\d+), (.+), (.+) for a previous obstetrics record to document an obstetrics record for a patient.$" )
    public void previousRecordData ( final String date, final int conception, final int weeks, final int hours,
            final String delivery, final String twins ) {
        waitForAngular();
        driver.findElement( By.name( "addPreviousPregnancy" ) ).click();
        final WebElement dateElement = driver.findElement( By.name( "previouslmp" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );

        final WebElement conception_year = driver.findElement( By.name( "conception" ) );
        conception_year.clear();
        conception_year.sendKeys( Integer.toString( conception ) );

        final WebElement weeks_pregnant = driver.findElement( By.name( "weeksPreg" ) );
        weeks_pregnant.clear();
        weeks_pregnant.sendKeys( Integer.toString( weeks ) );

        final WebElement hours_in_labor = driver.findElement( By.name( "hoursInLabor" ) );
        hours_in_labor.clear();
        hours_in_labor.sendKeys( Integer.toString( hours ) );

        final Select delivery_dropdown = new Select( driver.findElement( By.id( "type" ) ) );
        delivery_dropdown.selectByVisibleText( delivery );

        final Select twins_dropdown = new Select( driver.findElement( By.id( "twins" ) ) );
        twins_dropdown.selectByVisibleText( twins );
    }

    @And ( "^I click add to document an obstetrics record for a patient.$" )
    public void addPreviousPregnancy () {
        driver.findElement( By.name( "add" ) ).click();
        try {
            Thread.sleep( 5000 );
        }
        catch ( final InterruptedException e ) {
            e.printStackTrace();
        }
    }

    @Then ( "^I log out as OB/GYN HCP.$" )
    public void obgynlogout () {
        driver.findElement( By.id( "logout" ) ).click();
        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        wait.until( ExpectedConditions.titleContains( "iTrust2 :: Login" ) );
    }

    @When ( "^I navigate to the View Obstetrics Records page.$" )
    public void navigateToView () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('viewObstetricsRecords').click();" );

        assertEquals( "iTrust2: View Obstetrics Records", driver.getTitle() );
        assertTextPresent( "Current Pregnancy" );
        assertTextPresent( "Previous Pregnancies" );

    }

    @Then ( "^(.+) is displayed on the page.$" )
    public void noObstetricsRecords ( final String text ) {
        waitForAngular();
        assertTextPresent( text );
    }

    @Then ( "^I can view the obstetrics record (.+), (.+), (\\d+).$" )
    public void viewEntryPatient ( final String lmp, final String dueDate, final int weeksPreg ) {
    	// Convert date from format 'MM/dd/yyyy' to LocalDate object
        final DateTimeFormatter lmpToIso = DateTimeFormatter.ofPattern( "MM/dd/yyyy" );

        final LocalDate now = LocalDate.now();
        final LocalDate convertedLmp = LocalDate.parse( lmp, lmpToIso );

        // Check for correct lmp on page
        assertTextPresent( "Last Menstrual Period" );
        assertEquals( convertedLmp.toString(), driver.findElement( By.id( "lmp" ) ).getText() );

         assertEquals( dueDate, Integer.parseInt( driver.findElement( By.id( "dueDate" ) ).getText() ) );
         assertEquals( weeksPreg, driver.findElement( By.id( "weeksPreg" ) ).getText() );
    }

    @Then ( "^I can view the previous obstetrics record (.+), (\\d+), (\\d+), (\\d+), (.+), (.+).$" )
    public void viewPreviousEntryPatient ( final String lmp, final int year, final int weeksPreg, final int labor,
            final String method, final String twins ) {
    	// Convert date from format 'MM/dd/yyyy' to LocalDate object
        final DateTimeFormatter lmpToIso = DateTimeFormatter.ofPattern( "MM/dd/yyyy" );

        final LocalDate now = LocalDate.now();
        final LocalDate convertedLmp = LocalDate.parse( lmp, lmpToIso );

        // Check for correct lmp on page
        assertTextPresent( "Last Menstrual Period" );
        assertEquals( convertedLmp.toString(), driver.findElement( By.id( "lmp-0" ) ).getText() );
    	
        assertEquals( year, Integer.parseInt( driver.findElement( By.id( "conception-0" ) ).getText() ) );
        assertEquals( weeksPreg, Integer.parseInt( driver.findElement( By.id( "weeksPreg-0" ) ).getText() ) );
        assertEquals( labor, Integer.parseInt( driver.findElement( By.id( "hoursInLabor-0" ) ).getText() ) );
        assertEquals( method, driver.findElement( By.id( "type-0" ) ).getText() );
        assertEquals( twins, driver.findElement( By.id( "twins-0" ) ).getText() );
    }

}
