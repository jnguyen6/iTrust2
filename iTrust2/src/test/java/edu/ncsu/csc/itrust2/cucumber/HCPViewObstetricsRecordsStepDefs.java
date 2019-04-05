package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step definitions for HCPViewObstetricsRecords feature.
 *
 * @author Kevin Haas (kjhaas)
 *
 */
public class HCPViewObstetricsRecordsStepDefs extends CucumberTest {

    private final String baseUrl   = "http://localhost:8080/iTrust2";
    private final String hcpString = "bobbyDoctor";

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

    /**
     *
     */
    @Given ( "^We have added an HCP into the system.$" )
    public void hcpExistsDiaries () {
        attemptLogout();

        final User hcp = new User( hcpString, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_HCP, 1 );
        hcp.save();

        // All tests can safely assume the existence of the 'hcp', 'admin', and
        // 'patient' users
    }

    /**
     *
     */
    @Given ( "^A female patient exists in the system.$" )
    public void femalePatientExistsInSystem () {
        attemptLogout();

        // Create the test User
        final User user1 = new User( "AliceThirteen", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        user1.save();

        // The User must also be created as a Patient
        // to show up in the list of Patients
        final Patient patient1 = new Patient( user1.getUsername() );
        patient1.setGender( Gender.Female );
        patient1.save();

        // All tests can safely assume the existence of the 'hcp', 'admin', and
        // 'patient' users

        attemptLogout();

        // Create the test User
        final User user = new User( "JillBob", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        user.save();

        // The User must also be created as a Patient
        // to show up in the list of Patients
        final Patient patient = new Patient( user.getUsername() );
        patient.setGender( Gender.Female );
        patient.save();

        // All tests can safely assume the existence of the 'hcp', 'admin', and
        // 'patient' users
    }

    /**
     * Logs in as an HCP and navigates to View Obstetrics Records page.
     */
    @Then ( "^I log in as an HCP and navigate to the View Obstetrics Records page.$" )
    public void navigateToObstetricsRecordsHCP () {
        attemptLogout();

        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( hcpString );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        assertEquals( "iTrust2: HCP Home", driver.getTitle() );

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('HCPViewObstetricsRecords').click();" );

        assertEquals( "iTrust2: View Patient Obstetrics Records", driver.getTitle() );
        assertTextPresent( "Current Pregnancy" );
        assertTextPresent( "Previous Pregnancies" );
    }

    /**
     *
     */
    @And ( "^I select to view (.+)'s obstetrics records.$" )
    public void selectPatient ( final String username ) {
        waitForAngular();
        driver.findElement( By.id( username ) ).click();
    }

    /**
     * Asserts text for unselected patient
     */
    @Then ( "^Unselected patient text is displayed when no patient is selected.$" )
    public void noPatientSelectedText () {
        waitForAngular();
        assertTextPresent( "Please select a patient for which to view Obstetrics Records." );
    }

    /**
     * Asserts text for no current pregnancies
     */
    @Then ( "^No current pregnancies text is displayed when the patient is selected and has no current pregnancies.$" )
    public void noCurrentPregnanciesText () {
        waitForAngular();
        assertTextPresent( "There are no current Obstetrics records for this Patient." );
    }

    /**
     * Asserts text for no previous pregnancies
     */
    @And ( "^No previous pregnancies text is displayed when the patient is selected and has no previous pregnancies.$" )
    public void noPreviousPregnanciesText () {
        waitForAngular();
        assertTextPresent( "There are no previous pregnancies." );
    }

    /**
     * Login as an OBGYN and go to obstetrics records
     */
    public void obgynLogin () {
        // Login as OBGYN
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

        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        wait.until( ExpectedConditions.titleContains( "iTrust2: HCP Home" ) );
        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('OBGYNHCPDocumentObstetricsRecord').click();" );
        final WebDriverWait wait2 = new WebDriverWait( driver, 20 );
        wait2.until( ExpectedConditions.titleContains( "iTrust2: View Patient Obstetrics Records" ) );
    }

    /**
     * Add obstetrics record for username with lmp
     */
    @When ( "^As an OB/GYN HCP I add a current obstetrics record for (.+) with LMP (.+).$" )
    public void addCurrentRecord ( final String user, final String lmp ) {

        // Login as OBGYN
        obgynLogin();

        // Select patient
        selectPatient( user );

        // Create record
        waitForAngular();
        final WebElement dateElement = driver.findElement( By.name( "currentlmp" ) );
        dateElement.sendKeys( lmp.replace( "/", "" ) );
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Check for patient's correct LMP, Estimated Due Date, and Weeks pregnant
     */
    @Then ( "^The patient's correct LMP (.+), Estimated Due Date, and Weeks Pregnant are displayed.$" )
    public void checkCurrentRecord ( final String lmp ) {
        waitForAngular();

        // Convert date from format 'MM/dd/yyyy' to LocalDate object
        final DateTimeFormatter lmpToIso = DateTimeFormatter.ofPattern( "MM/dd/yyyy" );

        final LocalDate now = LocalDate.now();
        final LocalDate convertedLmp = LocalDate.parse( lmp, lmpToIso );

        // Check for correct lmp on page
        assertTextPresent( "Last Menstrual Period" );
        assertEquals( convertedLmp.toString(), driver.findElement( By.id( "lmp" ) ).getText() );

        // Calculate and check for correct Estimated Due Date
        final LocalDate estimatedDueDate = convertedLmp.plusDays( 280 );
        assertTextPresent( "Estimated Due Date" );
        assertEquals( estimatedDueDate.toString(), driver.findElement( By.id( "dueDate" ) ).getText() );

        // Calculate and check for correct Weeks Pregnant
        final long weeksPregnant = ChronoUnit.WEEKS.between( convertedLmp, now );
        assertTextPresent( "Weeks Pregnant" );
        assertEquals( weeksPregnant, Long.parseLong( ( driver.findElement( By.id( "weeksPreg" ) ).getText() ) ) );
    }

    /**
     * Assert that given user has no records
     */
    @And ( "^The other user (.+) still has no records.$" )
    public void assertUserNoRecords ( final String user ) {
        selectPatient( user );
        noCurrentPregnanciesText();
        noPreviousPregnanciesText();
    }

    /**
     * Add previous record for given user
     */
    @When ( "^As an OB/GYN HCP I add a previous obstetrics record for (.+) with conception year (\\d+), (\\d+) weeks pregnant, (\\d+) hours in labor, (.+), twins: (.+) for a previous obstetrics record.$" )
    public void previousRecordData ( final String user, final int conception, final int weeks, final int hours,
            final String delivery, final String twins ) {

        // Login as OBGYN
        obgynLogin();

        // Select patient
        selectPatient( user );

        // Add record
        waitForAngular();
        driver.findElement( By.name( "addPreviousPregnancy" ) ).click();

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

        waitForAngular();
        driver.findElement( By.name( "add" ) ).click();

    }

    /**
     * Check that previous record can be viewed
     */
    @Then ( "^The patient's conception year (\\d+), (\\d+) weeks pregnant, (\\d+) hours in labor, (.+), twins: (.+) for a previous obstetrics record are displayed for record (\\d+).$" )
    public void checkPreviousRecordData ( final int conception, final int weeks, final int hours, final String delivery,
            final String twins, final int recordNo ) {
        waitForAngular();

        // Check for correct conception year
        assertTextPresent( "Conception Year" );
        assertEquals( conception,
                Integer.parseInt( driver.findElement( By.id( "conception-" + recordNo ) ).getText() ) );

        // Check for correct weeks pregnant
        assertTextPresent( "# Weeks Pregnant" );
        assertEquals( weeks, Integer.parseInt( driver.findElement( By.id( "weeksPreg-" + recordNo ) ).getText() ) );

        // Check for correct hours labor
        assertTextPresent( "# Hours in Labor" );
        assertEquals( hours, Integer.parseInt( driver.findElement( By.id( "hoursInLabor-" + recordNo ) ).getText() ) );

        // Check for correct Delivery method
        assertTextPresent( "Delivery Method" );
        assertEquals( delivery, driver.findElement( By.id( "type-" + recordNo ) ).getText() );

        // Check for correct twins
        assertTextPresent( "Twins" );
        assertEquals( twins, driver.findElement( By.id( "twins-" + recordNo ) ).getText() );

    }
}
