package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.GeneralOphthalmology;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step defs that define the tests for editing an office visit
 *
 * @author jltalare
 *
 */
public class HCPEditsOfficeVisitStepDefs extends CucumberTest {

    private final String baseUrl       = "http://localhost:8080/iTrust2";
    private final String ophHcpString  = "bobbyOPH";
    private final String patientString = "bobby";

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
     * Logins in as an HCP and navigates to the Edit Office Visit page
     */
    @Then ( "^The HCP logs in and navigates to the Edit Office Visit page$" )
    public void editPageLogin () {
        attemptLogout();

        driver.get( baseUrl );

        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( ophHcpString );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        assertEquals( "iTrust2: HCP Home", driver.getTitle() );

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('editOfficeVisit').click();" );

        assertEquals( "iTrust2: Edit Office Visit", driver.getTitle() );
    }

    /**
     * Creates ophthalmology office visit for the patient
     *
     * @throws ParseException
     */
    @And ( "^The patient has a documented ophthalmology surgery office visit$" )
    public void createOphthalmologyOfficeVisit () throws ParseException {
        DomainObject.deleteAll( GeneralOphthalmology.class );
        final GeneralOphthalmology genOph = getOphOfficeVisit();
        if ( genOph != null ) {
            genOph.save();
        }
    }

    /**
     * Generates a general ophthalmology office visit with mock data
     */
    private GeneralOphthalmology getOphOfficeVisit () {
        final Hospital hosp = new Hospital( "Dr. Jenkins' Insane Asylum", "123 Main St", "12345", "NC" );
        hosp.save();

        final GeneralOphthalmology visit = new GeneralOphthalmology();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setHcp( User.getByName( "bobbyOD" ) );
        bhm.setPatient( User.getByName( patientString ) );
        bhm.setHdl( 75 );
        bhm.setLdl( 75 );
        bhm.setHeight( 75f );
        bhm.setWeight( 130f );
        bhm.setTri( 300 );
        bhm.setSystolic( 150 );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        bhm.setPatientSmokingStatus( PatientSmokingStatus.NEVER );

        bhm.save();
        visit.setBasicHealthMetrics( bhm );
        visit.setType( AppointmentType.GENERAL_OPHTHALMOLOGY );
        visit.setHospital( hosp );
        visit.setPatient( User.getByName( patientString ) );
        visit.setHcp( User.getByName( "bobbyOD" ) );
        visit.setDate( ZonedDateTime.now() );
        visit.save();

        visit.setVisualAcuityOD( 20 );
        visit.setVisualAcuityOS( 40 );
        visit.setSphereOD( 1.5 );
        visit.setSphereOS( -1.5 );
        visit.setCylinderOD( 1.0 );
        visit.setCylinderOS( -1.0 );
        visit.setAxisOD( 45 );
        visit.setAxisOS( 90 );
        visit.save();

        final List<String> diagnoses = new ArrayList<String>();
        diagnoses.add( "Cataracts" );
        diagnoses.add( "Glaucoma" );
        visit.setDiagnosis( diagnoses );

        visit.save();

        return visit;
    }

    /**
     * Selects an office visit on the editing office visit page
     */
    @When ( "^The HCP selects the existing office visit$" )
    public void hcpSelectOfficeVisit () {
        final List<OfficeVisit> visits = OfficeVisit.getOfficeVisits();
        long targetId = 0;

        for ( int i = 0; i < visits.size(); i++ ) {
            if ( visits.get( i ).getType().equals( AppointmentType.GENERAL_OPHTHALMOLOGY )
                    && visits.get( i ).getPatient().getUsername().equals( patientString ) ) {
                targetId = visits.get( i ).getId();
            }
        }

        final WebElement elem = driver.findElement( By.cssSelector( "input[value=\"" + targetId + "\"]" ) );
        elem.click();
    }

    /**
     * Modifies the date and the height of the office visit
     * 
     * @param date
     *            the new date of the visit
     * @param height
     *            the new height of the patient
     */
    @And ( "^The HCP modifies the date to be (.+), height (.+), and the left eye visual acuity (.+)$" )
    public void modifyingTheDate ( final String date, final String height, final String visualAcuityOS ) {
        waitForAngular();

        final WebElement dateElement = driver.findElement( By.name( "date" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );

        driver.findElement( By.name( "height" ) ).clear();
        driver.findElement( By.name( "height" ) ).sendKeys( height );

        driver.findElement( By.name( "VAL" ) ).clear();
        driver.findElement( By.name( "VAL" ) ).sendKeys( visualAcuityOS );
    }

    /**
     * Simulates clicking the submit button on the edit office visit page
     */
    @And ( "^The HCP saves the office visit$" )
    public void hcpSavesOfficeVisit () {
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Checks if the changes were allowed to be made
     */
    @Then ( "^The ophthalmology office visit is updated successfully$" )
    public void hcpSuccessfulOfficeVisit () {
        // confirm that the message is displayed
        try {
            driver.findElement( By.name( "success" ) ).getText().contains( "Office visit edited successfully" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Checks if the changes were not allowed to be made
     */
    @Then ( "^The ophthalmology office visit is not updated successfully$" )
    public void hcpUnsuccessfulOfficeVisit () {
        // confirm that the error message is displayed
        try {
            final String temp = driver.findElement( By.name( "errorMsg" ) ).getText();
            if ( temp.equals( "" ) ) {
                fail();
            }
        }
        catch ( final Exception e ) {
        }
    }
}
