package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryReportForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.LaborDeliveryReport;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step definitions for the Labor and Delivery Report feature. This includes the
 * documentation and editing of the report.
 *
 * @author Jimmy Nguyen (jnguyen6)
 */
public class LaborAndDeliveryReportStepDefs extends CucumberTest {

    /** The base url for iTrust2 */
    private final String baseUrl        = "http://localhost:8080/iTrust2";
    /** The obstetrics patient to use */
    private final String patientString  = "AliceThirteen";
    /** The OB/GYN HCP to use */
    private final String obgynHcpString = "tylerOBGYN";

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
     * Fills in the date and time fields with the specified date and time.
     *
     * @param date
     *            The date to enter.
     * @param time
     *            The time to enter.
     */
    private void fillInDateTime ( final String dateField, final String date, final String timeField,
            final String time ) {
        waitForAngular();
        fillInDate( dateField, date );
        fillInTime( timeField, time );
    }

    /**
     * Fills in the date field with the specified date.
     *
     * @param date
     *            The date to enter.
     */
    private void fillInDate ( final String dateField, final String date ) {
        waitForAngular();
        driver.findElement( By.name( dateField ) ).clear();
        final WebElement dateElement = driver.findElement( By.name( dateField ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    /**
     * Fills in the time field with the specified time.
     *
     * @param time
     *            The time to enter.
     */
    private void fillInTime ( final String timeField, String time ) {
        waitForAngular();
        // Zero-pad the time for entry
        if ( time.length() == 7 ) {
            time = "0" + time;
        }

        driver.findElement( By.name( timeField ) ).clear();
        final WebElement timeElement = driver.findElement( By.name( timeField ) );
        timeElement.sendKeys( time.replace( ":", "" ).replace( " ", "" ) );
    }

    /**
     * Creates a new OB/GYN HCP User
     */
    @Given ( "^There exists an obstetrics HCP in the iTrust2 system$" )
    public void obgynHCPExists () {
        waitForAngular();
        attemptLogout();

        final User obgynHcp = new User( obgynHcpString, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_OBGYN, 1 );
        obgynHcp.save();

        // All tests can safely assume the existence of the 'obstetrics
        // hcp',
        // 'admin', and
        // 'patient' users
    }

    /**
     * Creates current obstetrics record for the patient
     */
    @And ( "^The obstetrics patient has a current obstetrics record in the iTrust2 system$" )
    public void currentObstetricsRecordExists () {
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
    }

    // Add code for creating a labor and delivery report here:
    @And ( "^The obstetrics patient has a documented labor and delivery report$" )
    public void createLaborDeliveryReport () {
        DomainObject.deleteAll( LaborDeliveryReport.class );
        final LaborDeliveryReport report = getLaborDeliveryReport();
        if ( report != null ) {
            report.save();
        }
    }

    /**
     * Generates a labor and delivery report with mock data
     */
    private LaborDeliveryReport getLaborDeliveryReport () {
        final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( "2019-03-11T10:00:00.000-04:00" );
        form.setDatetimeOfDelivery( "2019-03-22T10:00:00.000-04:00" );
        form.setWeight( 3.4 );
        form.setLength( 12.34 );
        form.setHeartRate( 70 );
        form.setBloodPressure( 70 );
        form.setFirstName( "Sanchit" );
        form.setLastName( "Razdan" );
        form.setSecondDatetimeOfDelivery( "2019-03-22T10:00:00.000-04:00" );
        form.setSecondWeight( 2.3 );
        form.setSecondLength( 10.4 );
        form.setSecondHeartRate( 75 );
        form.setSecondBloodPressure( 75 );
        form.setSecondFirstName( "Swarnim" );
        form.setSecondLastName( "Razdan" );

        try {
            return new LaborDeliveryReport( form );
        }
        catch ( final Exception e ) {
            // Do nothing
            return null;
        }
    }

    /**
     * Logs in HCP and navigates them to the document Labor and Deliver Report
     * page
     */
    @Then ( "^The OB/GYN HCP logs in and navigates to the Document Patient Labor and Delivery Reports page$" )
    public void loginObgynDocumentsLaborDeliveryReport () {
        waitForAngular();
        attemptLogout();

        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( obgynHcpString );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        assertEquals( "iTrust2: HCP Home", driver.getTitle() );

        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('OBGYNHCPDocumentLaborAndDeliveryReports').click();" );

        assertEquals( "iTrust2: Document Labor And Delivery Reports", driver.getTitle() );
    }

    // /**
    // * Method to navigate to the edit labor and delivery report page
    // *
    // * @throws Throwable
    // */
    // @Then ( "^The OB/GYN HCP logs in and navigates to the Edit Labor and
    // Delivery Reports page$" )
    // public void navigateToEditLaborDeliveryReportPage () {
    // attemptLogout();
    // driver.get( baseUrl );
    // final WebElement username = driver.findElement( By.name( "username" ) );
    // username.clear();
    // username.sendKeys( obgynHcpString );
    // final WebElement password = driver.findElement( By.name( "password" ) );
    // password.clear();
    // password.sendKeys( "123456" );
    // final WebElement submit = driver.findElement( By.className( "btn" ) );
    // submit.click();
    //
    // assertEquals( "iTrust2: HCP Home", driver.getTitle() );
    //
    // ( (JavascriptExecutor) driver )
    // .executeScript(
    // "document.getElementById('OBGYNHCPDocumentLaborAndDeliveryReports').click();"
    // );
    //
    // assertEquals( "iTrust2: Edit Labor and Delivery Report",
    // driver.getTitle() );
    // }

    /**
     * Selects the patient and the date of the report
     *
     * @param patient
     *            the patient to select
     * @param dateReport
     *            the date of the report to select
     */
    @When ( "^The OB/GYN HCP selects the patient <patient> and enters the date of the report <dateReport>$" )
    public void selectPatientAndDateOfReport ( final String patient, final String dateReport ) {
        waitForAngular();

        final WebElement patientElement = driver.findElement( By.cssSelector( "input[value=\"" + patient + "\"]" ) );
        patientElement.click();

        final WebElement dateOfReportElement = driver
                .findElement( By.cssSelector( "input[value=\"" + dateReport + "\"]" ) );
        dateOfReportElement.click();
    }

    /**
     * Edits the fields in the existing labor and delivery report
     *
     * @param
     */
    @And ( "^The OB/GYN HCP modifies the date of labor (.+), length (.+), and the first name (.+) " )
    public void editLaborAndDeliveryReport ( final String newDateLabor, final String newLength,
            final String newFirstName ) {
        waitForAngular();

        driver.findElement( By.name( "selected-dateOfLabor" ) ).clear();
        driver.findElement( By.name( "selected-dateOfLabor" ) ).sendKeys( newDateLabor );

        driver.findElement( By.name( "length" ) ).clear();
        driver.findElement( By.name( "length" ) ).sendKeys( newLength );

        driver.findElement( By.name( "firstName" ) ).clear();
        driver.findElement( By.name( "firstName" ) ).sendKeys( newFirstName );
    }

    /**
     * Adds basic information about the labor and delivery report into the page
     *
     * @param patientt
     *            the patient to select for the labor and delivery report
     * @param dateLabor
     *            the date when the patient is in labor
     * @param timeLabor
     *            the time when the patient is in labor
     * @param dateDelivery
     *            the date of the delivery
     * @param timeDelivery
     *            the time of the delivery
     * @param hospital
     *            the hospital that the office visit is scheduled at
     * @param deliveryType
     *            the delivery method
     */
    @When ( "^The OB/GYN HCP selects the patient (.+) and enters the date of labor (.+), time of labor (.+), date of delivery (.+), time of delivery (.+), delivery method (.+), weight in pounds (.+) and ounces (.+), length (.+), heart rate (.+), blood pressure (.+), first name (.+), and last name (.+)$" )
    public void addLaborandDeliveryReportInfo ( final String patient, final String dateLabor, final String timeLabor,
            final String dateDelivery, final String timeDelivery, final String deliveryType, final String lbs,
            final String oz, final String length, final String heartRate, final String bloodPres,
            final String firstName, final String lastName ) {
        waitForAngular();

        final WebElement patientElement = driver.findElement( By.cssSelector( "input[value=\"" + patient + "\"]" ) );
        patientElement.click();

        waitForAngular();

        fillInDateTime( "selected-dateOfLabor", dateLabor, "selected-timeOfLabor", timeLabor );
        fillInDateTime( "selected-dateOfDelivery", dateDelivery, "selected-timeOfDelivery", timeDelivery );

        driver.findElement( By.name( "deliveryMethod" ) ).sendKeys( deliveryType );

        driver.findElement( By.name( "weightlbs" ) ).clear();
        driver.findElement( By.name( "weightlbs" ) ).sendKeys( lbs );

        driver.findElement( By.name( "weightoz" ) ).clear();
        driver.findElement( By.name( "weightoz" ) ).sendKeys( oz );

        driver.findElement( By.name( "length" ) ).clear();
        driver.findElement( By.name( "length" ) ).sendKeys( length );

        driver.findElement( By.name( "heartRate" ) ).clear();
        driver.findElement( By.name( "heartRate" ) ).sendKeys( heartRate );

        driver.findElement( By.name( "bloodPressure" ) ).clear();
        driver.findElement( By.name( "bloodPressure" ) ).sendKeys( bloodPres );

        driver.findElement( By.name( "firstName" ) ).clear();
        driver.findElement( By.name( "firstName" ) ).sendKeys( firstName );

        driver.findElement( By.name( "lastName" ) ).clear();
        driver.findElement( By.name( "lastName" ) ).sendKeys( lastName );

    }

    /**
     * Adds basic information about the labor and delivery report into the page,
     * but for twins
     *
     * @param patientt
     *            the patient to select for the labor and delivery report
     * @param dateLabor
     *            the date when the patient is in labor
     * @param timeLabor
     *            the time when the patient is in labor
     * @param dateDelivery
     *            the date of the delivery
     * @param timeDelivery
     *            the time of the delivery
     * @param hospital
     *            the hospital that the office visit is scheduled at
     * @param deliveryType
     *            the delivery method
     */
    @When ( "^The OB/GYN HCP selects the patient <patient> and enters the date of labor <dateLabor>, time of labor <timeLabor>, date of delivery <dateDelivery>, time of delivery <timeDelivery>, delivery method <deliveryType>, weight in pounds <lbs> and ounces <oz>, length <length>, heart rate <heartRate>, blood pressure <bloodPres>, first name <firstName>, and last name <lastName> for twins$" )
    public void addLaborandDeliveryReportInfoForTwins ( final String patient, final String dateLabor,
            final String timeLabor, final String dateDelivery, final String timeDelivery, final String deliveryType,
            final String lbs, final String oz, final String length, final String heartRate, final String bloodPres,
            final String firstName, final String lastName ) {
        waitForAngular();

        final WebElement patientElement = driver.findElement( By.cssSelector( "input[value=\"" + patient + "\"]" ) );
        patientElement.click();

        waitForAngular();

        fillInDateTime( "dateOfLabor", dateLabor, "timeOfLabor", timeLabor );
        fillInDateTime( "dateOfDelivery", dateDelivery, "timeOfDelivery", timeDelivery );

        driver.findElement( By.name( "deliveryMethod" ) ).sendKeys( deliveryType );

        driver.findElement( By.name( "weightlbs" ) ).clear();
        driver.findElement( By.name( "weightlbs" ) ).sendKeys( lbs );

        driver.findElement( By.name( "weightoz" ) ).clear();
        driver.findElement( By.name( "weightoz" ) ).sendKeys( oz );

        driver.findElement( By.name( "length" ) ).clear();
        driver.findElement( By.name( "length" ) ).sendKeys( length );

        driver.findElement( By.name( "heartRate" ) ).clear();
        driver.findElement( By.name( "heartRate" ) ).sendKeys( heartRate );

        driver.findElement( By.name( "bloodPressure" ) ).clear();
        driver.findElement( By.name( "bloodPressure" ) ).sendKeys( bloodPres );

        driver.findElement( By.name( "firstName" ) ).clear();
        driver.findElement( By.name( "firstName" ) ).sendKeys( firstName );

        driver.findElement( By.name( "lastName" ) ).clear();
        driver.findElement( By.name( "lastName" ) ).sendKeys( lastName );

        // The same procedure, but for the second baby patient
        waitForAngular();

        fillInDateTime( "selected-secondDateOfDelivery", dateDelivery, "selected-secondTimeOfDelivery", timeDelivery );

        driver.findElement( By.name( "secondDeliveryMethod" ) ).sendKeys( deliveryType );

        driver.findElement( By.name( "secondWeightlbs" ) ).clear();
        driver.findElement( By.name( "secondWeightlbs" ) ).sendKeys( lbs );

        driver.findElement( By.name( "secondWeightoz" ) ).clear();
        driver.findElement( By.name( "secondWeightoz" ) ).sendKeys( oz );

        driver.findElement( By.name( "secondLength" ) ).clear();
        driver.findElement( By.name( "secondLength" ) ).sendKeys( length );

        driver.findElement( By.name( "secondHeartRate" ) ).clear();
        driver.findElement( By.name( "secondHeartRate" ) ).sendKeys( heartRate );

        driver.findElement( By.name( "secondBloodPressure" ) ).clear();
        driver.findElement( By.name( "secondBloodPressure" ) ).sendKeys( bloodPres );

        driver.findElement( By.name( "secondFirstName" ) ).clear();
        driver.findElement( By.name( "secondFirstName" ) ).sendKeys( firstName );

        driver.findElement( By.name( "secondLastName" ) ).clear();
        driver.findElement( By.name( "secondLastName" ) ).sendKeys( lastName );

    }

    /**
     * Method to submit the labor and delivery report
     */
    @And ( "^The OB/GYN HCP adds the labor and delivery report$" )
    public void submitLaborDeliveryReport () {
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Method to check if the documentation was successful
     */
    @Then ( "^The labor and delivery report is documented successfully$" )
    public void documentedSuccessfully () {
        waitForAngular();

        // confirm that the message is displayed
        try {
            driver.findElement( By.name( "success" ) ).getText().contains( "Office visit created successfully" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Method to check if the documentation failed
     */
    @Then ( "^The labor and delivery report is not documented successfully$" )
    public void documentedUnsuccessfully () {
        waitForAngular();

        // confirm that the error message is displayed
        try {
            if ( driver.findElement( By.name( "success" ) ).getText()
                    .contains( "Labor and Delivery Report created successfully" ) ) {
                fail();
            }
        }
        catch ( final Exception e ) {
        }

    }

    /**
     * Method to save the labor and delivery report
     *
     * @throws Throwable
     */
    @And ( "^The OB/GYN HCP saves the labor and delivery report$" )
    public void saveLaborDeliveryReport () {
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Method to check if update is successful
     *
     * @throws Throwable
     */
    @Then ( "^The labor and delivery report is updated successfully$" )
    public void successfulEdit () {
        waitForAngular();
        try {
            driver.findElement( By.name( "success" ) ).getText()
                    .contains( "Labor and Delivery Report edited successfully" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Method to check if attempt to update failed
     *
     * @throws Throwable
     */
    @Then ( "^The labor and delivery report is not updated successfully$" )
    public void unsuccessfulEdit () {
        // confirm that the error message is displayed
        waitForAngular();
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
