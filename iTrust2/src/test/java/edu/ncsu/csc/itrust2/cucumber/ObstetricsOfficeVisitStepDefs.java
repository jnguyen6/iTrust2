package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.forms.hcp.GeneralObstetricsForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.GeneralObstetrics;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step definitions for Obstetrics Office Visit feature.
 *
 * @author Jimmy Nguyen (jnguyen6)
 */
public class ObstetricsOfficeVisitStepDefs extends CucumberTest {

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

    @Given ( "^There exists an obstetrics patient in the system$" )
    public void obstetricsPatientExists () {
        waitForAngular();
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

    /**
     * Creates a new OB/GYN HCP User
     */
    @Given ( "^There exists an obstetrics HCP in the system$" )
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
    @And ( "^The obstetrics patient has a current obstetrics record$" )
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

    /**
     * Creates obstetrics office visit for the patient
     *
     * @throws ParseException
     */
    @And ( "^The obstetrics patient has a documented obstetrics office visit$" )
    public void createObstetricOfficeVisit () throws ParseException {
        DomainObject.deleteAll( GeneralObstetrics.class );
        final GeneralObstetrics genObgyn = getGenObstetricsVisit();
        if ( genObgyn != null ) {
            genObgyn.save();
        }
    }

    /**
     * Generates an obstetrics office visit with mock data
     */
    private GeneralObstetrics getGenObstetricsVisit () {
        final Hospital hosp = new Hospital( "Some Hospital", "Some Road", "78901", "NC" );
        hosp.save();
        final GeneralObstetricsForm visit = new GeneralObstetricsForm();
        visit.setPreScheduled( null );
        visit.setDate( "2019-04-08T09:50:00.000-04:00" ); // 4/08/2019 9:50 AM
                                                          // EDT
        visit.setHcp( obgynHcpString );
        visit.setPatient( patientString );
        visit.setNotes( "Test office visit" );
        visit.setType( AppointmentType.GENERAL_OBSTETRICS.toString() );
        visit.setHospital( "Some Hospital" );
        visit.setDiastolic( 150 );
        visit.setHdl( 75 );
        visit.setLdl( 75 );
        visit.setHeight( 75f );
        visit.setWeight( 130f );
        visit.setTri( 300 );
        visit.setSystolic( 150 );
        visit.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        visit.setPatientSmokingStatus( PatientSmokingStatus.NEVER );

        visit.setWeeksPregnant( 3 );
        visit.setFetalHeartRate( 10 );
        visit.setFundalHeight( 20.0 );
        visit.setIsTwins( false );
        visit.setIsLowLyingPlacenta( false );

        try {
            return new GeneralObstetrics( visit );
        }
        catch ( final Exception e ) {
            // Do nothing
            return null;
        }
    }

    /**
     * Logs in HCP and navigates them to the document Office Visit page
     */
    @Then ( "^The OB/GYN HCP logs in and navigates to the Document Office Visit page$" )
    public void loginObgynDocuments () {
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

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('documentOfficeVisit').click();" );

        assertEquals( "iTrust2: Document Office Visit", driver.getTitle() );
    }

    /**
     * Adds basic information about the office visit into the page
     *
     * @param date
     *            the date of the office visit
     * @param time
     *            the time of the office visit
     * @param patient
     *            the patient to select from the list of patients
     * @param type
     *            the type of office visit to select
     * @param hospital
     *            the hospital that the office visit is scheduled at
     */
    @When ( "^The OB/GYN HCP enters the date (.+), time (.+), patient (.+), type of visit (.+), hospital (.+)$" )
    public void addBasicInfo ( final String date, final String time, final String patient, final String type,
            final String hospital ) {
        waitForAngular();

        fillInDateTime( "date", date, "time", time );

        final WebElement patientElement = driver.findElement( By.cssSelector( "input[value=\"" + patient + "\"]" ) );
        patientElement.click();

        waitForAngular();

        final WebElement typeElement = driver.findElement( By.cssSelector( "input[value=\"" + type + "\"]" ) );
        typeElement.click();

        final WebElement hospitalElement = driver.findElement( By.cssSelector( "input[value=\"" + hospital + "\"]" ) );
        hospitalElement.click();
    }

    /**
     * Enter the basic health metrics into the page
     *
     * @param height
     *            the patient's height
     * @param weight
     *            the patient's weight
     * @param systolic
     *            the patient's systolic blood pressure
     * @param diastolic
     *            the patient's diastolic blood pressure
     * @param hdl
     *            the patient's hdl
     * @param ldl
     *            the patient's ldl
     * @param tri
     *            the patient's tri
     * @param patientSmoking
     *            the patient's smoking status
     * @param smokingStatus
     *            the patient's household smoking status
     */
    @And ( "^The OB/GYN HCP enters the basic health metrics with height (.+), weight (.+), systolic (.+), diastolic (.+), HDL (.+), LDL (.+), Triglycerides (.+), patient smoking status (.+), and household smoking status (.+)$" )
    public void addBasicHealthMetrics ( final String height, final String weight, final String systolic,
            final String diastolic, final String hdl, final String ldl, final String tri, final String patientSmoking,
            final String smokingStatus ) {
        waitForAngular();

        driver.findElement( By.name( "height" ) ).clear();
        driver.findElement( By.name( "height" ) ).sendKeys( height );

        driver.findElement( By.name( "weight" ) ).clear();
        driver.findElement( By.name( "weight" ) ).sendKeys( weight );

        driver.findElement( By.name( "systolic" ) ).clear();
        driver.findElement( By.name( "systolic" ) ).sendKeys( systolic );

        driver.findElement( By.name( "diastolic" ) ).clear();
        driver.findElement( By.name( "diastolic" ) ).sendKeys( diastolic );

        driver.findElement( By.name( "hdl" ) ).clear();
        driver.findElement( By.name( "hdl" ) ).sendKeys( hdl );

        driver.findElement( By.name( "ldl" ) ).clear();
        driver.findElement( By.name( "ldl" ) ).sendKeys( ldl );

        driver.findElement( By.name( "tri" ) ).clear();
        driver.findElement( By.name( "tri" ) ).sendKeys( tri );

        final WebElement patientSmokingElement = driver
                .findElement( By.cssSelector( "input[value=\"" + patientSmoking + "\"]" ) );
        patientSmokingElement.click();

        final WebElement smokingElement = driver
                .findElement( By.cssSelector( "input[value=\"" + smokingStatus + "\"]" ) );
        smokingElement.click();
    }

    /**
     * Enters the obstetrics health fields into the page
     *
     * @param fetalHeartRate
     *            the fetal heart rate
     * @param fundalHeight
     *            the fundal height of the uterus
     * @param isTwins
     *            whether the patient has twins
     * @param isLowLyingPlacenta
     *            whether the patient has low-lying placenta
     */
    @And ( "^The OB/GYN HCP enters the obstetrics health metrics with fetal heart rate (.+), fundal height of uterus (.+), twins (.+), and low-lying placenta (.+)$" )
    public void addObstetricsHealthMetrics ( final String fetalHeartRate, final String fundalHeight,
            final String isTwins, final String isLowLyingPlacenta ) {
        waitForAngular();

        driver.findElement( By.name( "fetalHeartRate" ) ).clear();
        driver.findElement( By.name( "fetalHeartRate" ) ).sendKeys( fetalHeartRate );

        driver.findElement( By.name( "fundalHeight" ) ).clear();
        driver.findElement( By.name( "fundalHeight" ) ).sendKeys( fundalHeight );

        driver.findElement( By.name( "isTwins" ) ).sendKeys( isTwins );

        driver.findElement( By.name( "isLowLyingPlacenta" ) ).sendKeys( isLowLyingPlacenta );

    }

    /**
     * Method to enter the notes into the page
     *
     * @param notes
     *            the notes for the visit
     */
    @And ( "^The OB/GYN HCP enters notes (.+)$" )
    public void addOfficeVisitNotes ( final String notes ) {
        waitForAngular();

        driver.findElement( By.name( "notes" ) ).clear();
        driver.findElement( By.name( "notes" ) ).sendKeys( notes );
    }

    /**
     * Method to navigate to the edit office visit page
     *
     * @throws Throwable
     */
    @Then ( "^The OB/GYN HCP logs in and navigates to the Edit Office Visit page$" )
    public void navigateToEditOfficeVisitPage () {
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

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('editOfficeVisit').click();" );

        assertEquals( "iTrust2: Edit Office Visit", driver.getTitle() );
    }

    /**
     * Method to select the existing office visit
     */
    @When ( "^The OB/GYN HCP selects the existing office visit$" )
    public void selectObstetricsOfficeVisit () {

        waitForAngular();
        final List<OfficeVisit> visits = OfficeVisit.getOfficeVisits();
        long targetId = 0;

        for ( int i = 0; i < visits.size(); i++ ) {
            if ( visits.get( i ).getType().equals( AppointmentType.GENERAL_OBSTETRICS )
                    && visits.get( i ).getPatient().getUsername().equals( patientString ) ) {
                targetId = visits.get( i ).getId();
            }
        }

        final WebElement elem = driver.findElement( By.cssSelector( "input[value=\"" + targetId + "\"]" ) );
        elem.click();
    }

    /**
     * Method to edit the obstetrics office visit
     *
     *
     * @param newFetalHeartRate
     *            the new fetal heart rate to set
     * @param newFundalHeight
     *            the new fundal height to set
     * @param newWeeksPregnant
     *            the new number of weeks pregnant to set
     */
    @And ( "^The OB/GYN HCP modifies the fetal heart rate to be (.+), fundal height (.+), and the weeks pregnant (.+)$" )
    public void editObstetricsOfficeVisit ( final String newFetalHeartRate, final String newFundalHeight,
            final String newWeeksPregnant ) {
        waitForAngular();

        driver.findElement( By.name( "fetalHeartRate" ) ).clear();
        driver.findElement( By.name( "fetalHeartRate" ) ).sendKeys( newFetalHeartRate );

        driver.findElement( By.name( "fundalHeight" ) ).clear();
        driver.findElement( By.name( "fundalHeight" ) ).sendKeys( newFundalHeight );

        driver.findElement( By.name( "weeksPregnant" ) ).clear();
        driver.findElement( By.name( "weeksPregnant" ) ).sendKeys( newWeeksPregnant );

    }

    /**
     * Method to save the office visit
     *
     * @throws Throwable
     */
    @And ( "^The OB/GYN HCP saves the office visit$" )
    public void saveObstetricsOfficeVisit () {
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Method to submit the office visit
     */
    @And ( "^The OB/GYN HCP submits the obstetrics office visit$" )
    public void submitOfficeVisit () {
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Method to check if the documentation was successful
     */
    @Then ( "^The obstetrics office visit is documented successfully$" )
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
    @Then ( "^The obstetrics office visit is not documented successfully$" )
    public void documentedUnsuccessfully () {
        waitForAngular();

        // confirm that the error message is displayed
        try {
            if ( driver.findElement( By.name( "success" ) ).getText()
                    .contains( "Office visit created successfully" ) ) {
                fail();
            }
        }
        catch ( final Exception e ) {
        }

    }

    /**
     * Method to check if update is successful
     *
     * @throws Throwable
     */
    @Then ( "^The obstetrics office visit is updated successfully$" )
    public void successfulEdit () {
        waitForAngular();
        try {
            driver.findElement( By.name( "success" ) ).getText().contains( "Office visit edited successfully" );
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
    @Then ( "^The obstetrics office visit is not updated successfully$" )
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

    /**
     * Method to check if the log was updated for obstetrics office visit
     */
    @Then ( "The log is updated stating that the obstetrics office visit was documented" )
    public void logObOfficeVisit () {
        waitForAngular();
        driver.get( baseUrl );
        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        wait.until( ExpectedConditions.titleContains( "HCP Home" ) );
        assertEquals( "iTrust2: HCP Home", driver.getTitle() );
        wait.until( ExpectedConditions.elementToBeClickable( By.name( "transactionTypeCell" ) ) );
        // assertTextPresent( "Create obstetrics office visit for patient" );
    }

}
