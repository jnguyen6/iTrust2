package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.forms.admin.ICDCodeForm;
import edu.ncsu.csc.itrust2.forms.hcp.GeneralCheckupForm;
import edu.ncsu.csc.itrust2.forms.hcp.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.EyeSurgeryType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.GeneralCheckup;
import edu.ncsu.csc.itrust2.models.persistent.GeneralOphthalmology;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.ICDCode;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step definitions for a patient to view their obstetrics records.
 * 
 * @author Madhura Waghmare (mswaghma)
 */
public class PatientViewObstetricsRecordsStepDefs extends CucumberTest {
	
	private final String baseUrl       = "http://localhost:8080/iTrust2";
    private final String patientString = "AliceThirteen";
    private final String obgynString = "tylerOBGYN";
	
    
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
    
    @Given ( "^The patient can have an obstetrics record.$" )
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
    
    @Given ( "^There exists an obstetrics patient in the system.$" )
    public void patientExistsObstetricsRecords () {
        attemptLogout();

        // Create the test User
        final User user = new User( patientString, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        user.save();

        // The User must also be created as a Patient 
        // to show up in the list of Patients
        final Patient patient = new Patient( user.getUsername() );
        patient.save();

        // All tests can safely assume the existence of the 'hcp', 'admin', and
        // 'patient' users
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
    
    @When ( "^I navigate to the View Obstetrics Records page.$" )
    public void navigateToView() {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('viewObstetricsRecords').click();" );

        assertEquals( "iTrust2: View Obstetrics Records", driver.getTitle() );
        
    }
    
    @Then ( "^(.+) is displayed on the page.$")
    public void noObstetricsRecords ( String text) {
        waitForAngular();
        assertTextPresent( text );
    }
    
    @Then ( "^I log out.$" )
    public void obgynLogout () {
        attemptLogout();
    }
    
    @Then( "^I can view the obstetrics record (.+), (.+), (\\d+).$")
    public void viewEntryPatient( String date, String dueDate, int weeksPreg ) {
        assertEquals( date, driver.findElement( By.id( "lmp" ) ).getText() );
        assertEquals( dueDate, driver.findElement( By.id( "dueDate" ) ).getText() );
        assertEquals( weeksPreg, Integer.parseInt(driver.findElement( By.id( "weeksPreg" ) ).getText() ) );
    }
	
    @Then( "^I can view the previous obstetrics record (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+).$")
    public void viewPreviousEntryPatient( int lmp, int year, int weeksPreg, int labor, String method, String twins ) {
    	assertEquals( lmp, Integer.parseInt(driver.findElement( By.id( "lmp-0" ) ).getText() ) );
    	assertEquals( year, Integer.parseInt(driver.findElement( By.id( "conception-0" ) ).getText() ) );
    	assertEquals( weeksPreg, Integer.parseInt(driver.findElement( By.id( "weeksPreg-0" ) ).getText() ) );
    	assertEquals( labor, Integer.parseInt(driver.findElement( By.id( "hoursInLabor-0" ) ).getText() ) );
    	assertEquals( method, driver.findElement( By.id( "type-0" ) ).getText() );
        assertEquals( twins, driver.findElement( By.id( "twins-0" ) ).getText() );
    }
	
    @Then( "^I can view both previous and current obstetrics records (\\d+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+).$")
    public void viewPreviousEntryPatient( int lmp, String dueDate, int weeksPreg, int p_lmp, int year, int weeks, int labor, String method, String twins ) {
    	assertEquals( lmp, driver.findElement( By.id( "lmp" ) ).getText());
    	assertEquals( dueDate, Integer.parseInt(driver.findElement( By.id( "dueDate" ) ).getText() ) );
    	assertEquals( weeksPreg, driver.findElement( By.id( "weeksPreg" ) ).getText());
    	assertEquals( p_lmp, driver.findElement( By.id( "lmp-0" ) ).getText());
    	assertEquals( year, Integer.parseInt(driver.findElement( By.id( "conception-0" ) ).getText() ) );
    	assertEquals( weeks, Integer.parseInt(driver.findElement( By.id( "weeksPreg-0" ) ).getText() ) );
    	assertEquals( labor, Integer.parseInt(driver.findElement( By.id( "hoursInLabor-0" ) ).getText() ) );
    	assertEquals( method, driver.findElement( By.id( "type-0" ) ).getText() );
        assertEquals( twins, driver.findElement( By.id( "twins-0" ) ).getText() );
    }
	
	
	
	
	
	
	
	
	
	
	
	

}
