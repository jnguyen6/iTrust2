package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryReportForm;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.LaborDeliveryReport;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step definitions for a Patient to view all of their labor and delivery
 * reports.
 *
 * @author Jimmy Nguyen (jnguyen6)
 */
public class PatientViewLaborAndDeliveryReport extends CucumberTest {

    private final String baseUrl       = "http://localhost:8080/iTrust2";
    private final String patientString = "AliceThirteen";

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
     * Helper method to check if the given date appears in the list of dates
     * shown
     *
     * @param viewDate
     *            the date to check for
     */
    private void clickAndCheckDateButton ( final String viewDate ) {
        final List<WebElement> radioList = driver.findElements( By.name( "date" ) );

        for ( final WebElement element : radioList ) {
            final String v = element.getAttribute( "value" );
            final ZonedDateTime datetimeLabor = ZonedDateTime.parse( v );
            String g = "";
            if ( datetimeLabor.getMonthValue() < 10 ) {
                g += "0" + datetimeLabor.getMonthValue() + "/";
            }
            else {
                g += datetimeLabor.getMonthValue() + "/";
            }
            if ( datetimeLabor.getDayOfMonth() < 10 ) {
                g += "0" + datetimeLabor.getDayOfMonth() + "/";
            }
            else {
                g += datetimeLabor.getDayOfMonth() + "/";
            }
            g += datetimeLabor.getYear();
            if ( g.equals( viewDate ) ) {
                element.click();
                // assertTextPresent( "Labor and Delivery Reports for: " +
                // viewDate );
                return;
            }
        }

        fail( "The date isn't in the radio list." );
    }

    /**
     * Creates an Patient and saves them to the iTrust system
     */
    @Given ( "^there exists a patient in the iTrust2 system$" )
    public void HCPExists () {
        attemptLogout();

        // Create the test User
        final User user = new User( patientString, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        user.save();

        // The User must also be created as a Patient
        // to show up in the list of Patients
        final Patient patient = new Patient( user.getUsername() );
        patient.setGender( Gender.Female );
        patient.setEthnicity( Ethnicity.Caucasian );
        patient.setAddress1( "140 George Rd." );
        patient.setCity( "Raleigh" );
        patient.setState( State.NC );
        patient.setZip( "27606" );
        patient.setPhone( "123-456-7891" );
        patient.save();
        final ObstetricsRecordForm form = new ObstetricsRecordForm( DeliveryMethod.Vaginal, LocalDate.now().toString(),
                2019, 0, 1, false, true );
        final ObstetricsRecord record = new ObstetricsRecord( form );
        record.setPatient( patientString );
        record.save();
        attemptLogout();
    }

    // Add code for creating a labor and delivery report (with and without
    // twins) here:
    @And ( "^the patient has an existing labor and delivery report without twins$" )
    public void createLaborDeliveryReportWithoutTwins () {
        waitForAngular();

        final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( ZonedDateTime.of( 2019, 3, 22, 5, 2, 20, 2, ZoneId.of( "-05:00" ) ).toString() );
        form.setDatetimeOfDelivery( ZonedDateTime.of( 2019, 3, 22, 10, 2, 20, 2, ZoneId.of( "-05:00" ) ).toString() );
        form.setWeight( 3.4 );
        form.setLength( 12.34 );
        form.setHeartRate( 70 );
        form.setBloodPressure( 70 );
        form.setFirstName( "Madhura" );
        form.setLastName( "Waghmare" );

        final ObstetricsRecord record = new ObstetricsRecord();
        final LocalDate lmp = LocalDate.parse( "2019-03-02" );
        record.setLmp( lmp );
        record.setConception( 2019 );
        record.setWeeksPreg( 1 );
        record.setHoursInLabor( 25 );
        record.setDeliveryMethod( DeliveryMethod.Cesarean );
        record.setCurrentRecord( true );
        record.setTwins( false );
        record.setPatient( patientString );
        record.save();
        form.setObstetricsRecord( record );
        form.setDeliveryMethod( record.getDeliveryMethod() );
        LaborDeliveryReport ldRecord;
        try {
            ldRecord = new LaborDeliveryReport( form );
            ldRecord.setPatient( patientString );
            ldRecord.save();
        }
        catch ( final ParseException e ) {
            fail();
        }
    }

    @And ( "^the patient has an existing labor and delivery report with twins$" )
    public void createLaborDeliveryReportWithTwins () {
        waitForAngular();

        final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( ZonedDateTime.of( 2019, 3, 22, 5, 2, 20, 2, ZoneId.of( "-05:00" ) ).toString() );
        form.setDatetimeOfDelivery( ZonedDateTime.of( 2019, 3, 22, 10, 2, 20, 2, ZoneId.of( "-05:00" ) ).toString() );
        form.setWeight( 3.4 );
        form.setLength( 12.34 );
        form.setHeartRate( 70 );
        form.setBloodPressure( 70 );
        form.setFirstName( "Madhura" );
        form.setLastName( "Waghmare" );

        form.setSecondDatetimeOfDelivery(
                ZonedDateTime.of( 2019, 3, 22, 10, 32, 20, 2, ZoneId.of( "-05:00" ) ).toString() );
        form.setSecondWeight( 2.3 );
        form.setSecondLength( 10.4 );
        form.setSecondHeartRate( 75 );
        form.setSecondBloodPressure( 75 );
        form.setSecondFirstName( "Nishad" );
        form.setSecondLastName( "Waghmare" );

        final ObstetricsRecord record = new ObstetricsRecord();
        final LocalDate lmp = LocalDate.parse( "2019-03-02" );
        record.setLmp( lmp );
        record.setConception( 2019 );
        record.setWeeksPreg( 1 );
        record.setHoursInLabor( 25 );
        record.setDeliveryMethod( DeliveryMethod.Cesarean );
        record.setCurrentRecord( true );
        record.setTwins( true );
        record.setPatient( patientString );
        record.save();
        form.setObstetricsRecord( record );
        form.setDeliveryMethod( record.getDeliveryMethod() );
        form.setSecondDeliveryMethod( record.getDeliveryMethod() );
        LaborDeliveryReport ldRecord;
        try {
            ldRecord = new LaborDeliveryReport( form );
            ldRecord.setPatient( patientString );
            ldRecord.save();
        }
        catch ( final ParseException e ) {
            fail();
        }
    }

    /**
     * The patient logs in and navigates to the View Patient Labor and Delivery
     * Report page
     */
    @Then ( "^the patient logs in and navigates to the View Patient Labor and Delivery Report page$" )
    public void patientLoginNavToView () {
        waitForAngular();
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

        waitForAngular();
        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('viewLaborAndDeliveryReports').click();" );
        assertEquals( "iTrust2: View Labor and Delivery Reports", driver.getTitle() );
    }

    /**
     * Method to view the labor and delivery report
     *
     * @param dateDelivery
     *            the date of the labor and delivery report
     */
    @Then ( "^the patient select the date of the report (.+)$" )
    public void viewLaborDeliveryReport ( final String dateDelivery ) {
        waitForAngular();
        clickAndCheckDateButton( dateDelivery );
    }

    /**
     * The Patient views the labor and delivery report and validates that the
     * information is correct
     */
    @Then ( "^the patient's labor and delivery report is displayed$" )
    public void validateLaborAndDeliveryReport () {
        waitForAngular();
        assertEquals( driver.findElement( By.name( "datel" ) ).getText(), "03/22/2019" );
        assertEquals( driver.findElement( By.name( "timel" ) ).getText(), "6:02 AM" );
        assertEquals( driver.findElement( By.name( "date1" ) ).getText(), "03/22/2019" );
        assertEquals( driver.findElement( By.name( "time1" ) ).getText(), "11:02 AM" );
        assertEquals( driver.findElement( By.name( "type" ) ).getText(), "Cesarean" );
        assertEquals( driver.findElement( By.name( "lbs" ) ).getText(), "3" );
        assertEquals( driver.findElement( By.name( "oz" ) ).getText(), "6" );
        assertEquals( driver.findElement( By.name( "length" ) ).getText(), "12.34" );
        assertEquals( driver.findElement( By.name( "heartRate" ) ).getText(), "70" );
        assertEquals( driver.findElement( By.name( "bp" ) ).getText(), "70" );
        assertEquals( driver.findElement( By.name( "firstName" ) ).getText(), "Madhura" );
        assertEquals( driver.findElement( By.name( "lastName" ) ).getText(), "Waghmare" );
    }

    /**
     * The Patient views the labor and delivery report and validates that the
     * information is correct
     */
    @Then ( "^the patient's labor and delivery report of twins is displayed$" )
    public void checkTwinsLaborDeliveryReport () {
        waitForAngular();
        assertEquals( driver.findElement( By.name( "datel" ) ).getText(), "03/22/2019" );
        assertEquals( driver.findElement( By.name( "timel" ) ).getText(), "6:02 AM" );

        assertEquals( driver.findElement( By.name( "date1" ) ).getText(), "03/22/2019" );
        assertEquals( driver.findElement( By.name( "time1" ) ).getText(), "11:02 AM" );
        assertEquals( driver.findElement( By.name( "type" ) ).getText(), "Cesarean" );
        assertEquals( driver.findElement( By.name( "lbs" ) ).getText(), "3" );
        assertEquals( driver.findElement( By.name( "oz" ) ).getText(), "6" );
        assertEquals( driver.findElement( By.name( "length" ) ).getText(), "12.34" );
        assertEquals( driver.findElement( By.name( "heartRate" ) ).getText(), "70" );
        assertEquals( driver.findElement( By.name( "bp" ) ).getText(), "70" );
        assertEquals( driver.findElement( By.name( "firstName" ) ).getText(), "Madhura" );
        assertEquals( driver.findElement( By.name( "lastName" ) ).getText(), "Waghmare" );

        assertEquals( driver.findElement( By.name( "dateDel2" ) ).getText(), "03/22/2019" );
        assertEquals( driver.findElement( By.name( "timeDel2" ) ).getText(), "11:32 AM" );
        assertEquals( driver.findElement( By.name( "type2" ) ).getText(), "Cesarean" );
        assertEquals( driver.findElement( By.name( "lbs2" ) ).getText(), "2" );
        assertEquals( driver.findElement( By.name( "oz2" ) ).getText(), "4" );
        assertEquals( driver.findElement( By.name( "length2" ) ).getText(), "10.4" );
        assertEquals( driver.findElement( By.name( "heartRate2" ) ).getText(), "75" );
        assertEquals( driver.findElement( By.name( "bp2" ) ).getText(), "75" );
        assertEquals( driver.findElement( By.name( "firstName2" ) ).getText(), "Nishad" );
        assertEquals( driver.findElement( By.name( "lastName2" ) ).getText(), "Waghmare" );
    }

}
