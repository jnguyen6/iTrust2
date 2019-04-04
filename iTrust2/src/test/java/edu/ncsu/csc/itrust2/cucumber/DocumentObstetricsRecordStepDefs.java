package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
 * Step definitions for DocumentObstetricsRecord feature.
 *
 * @author Sanchit Razdan
 * @author Tyler Outlaw
 */
public class DocumentObstetricsRecordStepDefs extends CucumberTest {

    private final String baseUrl = "http://localhost:8080/iTrust2";

    @Before
    public void setUp () {
        DomainObject.deleteAll( ObstetricsRecord.class );
    }

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

    /**
     * Asserts that the text is not on the page
     *
     * @param text
     *            text to check
     */
    public void assertTextNotPresent ( final String text ) {
        try {
            assertFalse( driver.getPageSource().contains( text ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    @Given ( "^There exists a patient in the system who can have an obstetrics record.$" )
    public void patientExistsInSystem () {
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
        assertNotNull( driver.findElement( By.name( "patient" ) ) );

        driver.findElement( By.id( "logout" ) ).click();
    }

    @Then ( "^I log in as an OB/GYN HCP.$" )
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

    @When ( "^I navigate to the document obstetrics record page.$" )
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

    @And ( "^I select a patient using her (.+).$" )
    public void selectPatient ( final String username ) {
        driver.findElement( By.id( username ) ).click();
    }

    @And ( "^I enter (.+) for a current obstetrics record.$" )
    public void enterLMP ( final String date ) {
        final WebElement dateElement = driver.findElement( By.name( "currentlmp" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    @And ( "^I click create obstetrics record.$" )
    public void clickToCreateObstetricsRecord () {
        waitForAngular();
        driver.findElement( By.name( "submit" ) );
    }

    @Then ( "^the patient with this (.+) has her current pregnancy is updated.$" )
    public void everythingPresent ( final String username ) {
        final WebDriverWait wait2 = new WebDriverWait( driver, 20 );
        wait2.until( ExpectedConditions.titleContains( "iTrust2: View Patient Obstetrics Records" ) );
        driver.findElement( By.id( username ) ).click();
        assertTextPresent( "Last Menstrual Period" );
        assertTextPresent( "Estimated Due Date" );
        assertTextPresent( "Weeks Pregnant" );
    }

    @And ( "^I enter (.+), (\\d+), (\\d+), (\\d+), (.+), (.+) for a previous obstetrics record.$" )
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

    @And ( "^I click add.$" )
    public void addPreviousPregnancy () {
        driver.findElement( By.name( "add" ) ).click();
        try {
            Thread.sleep( 5000 );
        }
        catch ( final InterruptedException e ) {
            e.printStackTrace();
        }
    }

    @And ( "^I enter (.+) for a current obstetrics record, where the LMP is invalid.$" )
    public void enterInvalidLMP ( final String date ) {
        final WebElement dateElement = driver.findElement( By.name( "currentlmp" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    @Then ( "^the obstetrics record is not made.$" )
    public void currentRecordNotAdded () {
        assertTextPresent( "Could not add Obstetrics Record." );
    }

    @And ( "^I enter (.+), (.+), (.+), (.+), (.+) and (.+) for a previous obstetrics record, where one input is incorrect.$" )
    public void invalidPreviousPregnancyData ( final String date, final String conception, final String weeks,
            final String hours, final String delivery, final String twins ) {
        waitForAngular();
        driver.findElement( By.name( "addPreviousPregnancy" ) ).click();
        final WebElement dateElement = driver.findElement( By.name( "previouslmp" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );

        final WebElement conception_year = driver.findElement( By.name( "conception" ) );
        conception_year.clear();
        conception_year.sendKeys( conception );

        final WebElement weeks_pregnant = driver.findElement( By.name( "weeksPreg" ) );
        weeks_pregnant.clear();
        weeks_pregnant.sendKeys( weeks );

        final WebElement hours_in_labor = driver.findElement( By.name( "hoursInLabor" ) );
        hours_in_labor.clear();
        hours_in_labor.sendKeys( hours );

        final Select delivery_dropdown = new Select( driver.findElement( By.id( "type" ) ) );
        delivery_dropdown.selectByVisibleText( delivery );

        final Select twins_dropdown = new Select( driver.findElement( By.id( "twins" ) ) );
        twins_dropdown.selectByVisibleText( twins );

    }
}
