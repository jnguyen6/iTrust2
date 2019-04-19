package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    private final String patientString  = "JillBob";
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
    @And ( "^The obstetrics patient has a current obstetrics record in the iTrust2 system with no twins$" )
    public void currentObstetricsRecordExistsForNoTwins () {
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
     * Creates current obstetrics record for the patient
     */
    @And ( "^The obstetrics patient has a current obstetrics record in the iTrust2 system with twins$" )
    public void currentObstetricsRecordExistsForTwins () {
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
    @When ( "^The OB/GYN HCP selects the patient (.+) and the date of the report (.+)$" )
    public void selectPatientAndDateOfReport ( final String patient, final String dateReport ) {
        waitForAngular();

        final WebElement patientElement = driver.findElement( By.cssSelector( "input[value=\"" + patient + "\"]" ) );
        patientElement.click();

        waitForAngular();
        clickAndCheckDateButton( dateReport );
    }

    /**
     * Checks to see if the date is selectable.
     *
     * @param viewDate
     *            the date to check
     */
    private void clickAndCheckDateButton ( final String viewDate ) {
        final List<WebElement> radioList = driver.findElements( By.name( "date" ) );

        // Convert MM/dd/yyyy to yyyy-MM-dd
        // final String[] dateComponents = viewDate.split( "/" );
        // final String dateValue = String.format( "%s-%s-%s",
        // dateComponents[2], dateComponents[0], dateComponents[1] );

        for ( final WebElement element : radioList ) {
            if ( element.getAttribute( "value" ).equals( viewDate ) ) {
                element.click();
                assertTextPresent( "Reports: " + viewDate );
                return;
            }
        }

        fail( "The date isn't in the radio list." );
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

        fillInDateTime( "new-dateOfLabor", dateLabor, "new-timeOfLabor", timeLabor );
        fillInDateTime( "new-dateOfDelivery", dateDelivery, "new-timeOfDelivery", timeDelivery );

        driver.findElement( By.name( "new-deliveryMethod" ) ).sendKeys( deliveryType );

        if ( !deliveryType.equals( "Miscarriage" ) ) {

            // driver.findElement( By.name( "new-weightlbs" ) ).clear();
            // driver.findElement( By.name( "new-weightlbs" ) ).click();
            waitForAngular();
            final Actions actions = new Actions( driver );
            actions.moveToElement( driver.findElement( By.name( "new-weightlbs" ) ) );
            actions.click();
            actions.sendKeys( lbs );
            actions.build().perform();
            // driver.findElement( By.name( "new-weightlbs" ) ).sendKeys( lbs );

            // driver.findElement( By.name( "new-weightoz" ) ).clear();
            // driver.findElement( By.name( "new-weightoz" ) ).click();
            actions.moveToElement( driver.findElement( By.name( "new-weightoz" ) ) );
            actions.click();
            actions.sendKeys( oz );
            actions.build().perform();
            // driver.findElement( By.name( "new-weightoz" ) ).sendKeys( oz );

            // driver.findElement( By.name( "new-length" ) ).clear();
            // driver.findElement( By.name( "new-length" ) ).click();
            actions.moveToElement( driver.findElement( By.name( "new-length" ) ) );
            actions.click();
            actions.sendKeys( length );
            actions.build().perform();
            // driver.findElement( By.name( "new-length" ) ).sendKeys( length );

            // driver.findElement( By.name( "new-heartRate" ) ).clear();
            // driver.findElement( By.name( "new-heartRate" ) ).click();
            actions.moveToElement( driver.findElement( By.name( "new-heartRate" ) ) );
            actions.click();
            actions.sendKeys( heartRate );
            actions.build().perform();
            // driver.findElement( By.name( "new-heartRate" ) ).sendKeys(
            // heartRate
            // );

            // driver.findElement( By.name( "new-bloodPressure" ) ).clear();
            // driver.findElement( By.name( "new-bloodPressure" ) ).click();
            actions.moveToElement( driver.findElement( By.name( "new-bloodPressure" ) ) );
            actions.click();
            actions.sendKeys( bloodPres );
            actions.build().perform();
            // driver.findElement( By.name( "new-bloodPressure" ) ).sendKeys(
            // bloodPres );

            // driver.findElement( By.name( "new-firstName" ) ).clear();
            // driver.findElement( By.name( "new-firstName" ) ).click();
            actions.moveToElement( driver.findElement( By.name( "new-firstName" ) ) );
            actions.click();
            actions.sendKeys( firstName );
            actions.build().perform();
            // driver.findElement( By.name( "new-firstName" ) ).sendKeys(
            // firstName
            // );

            // driver.findElement( By.name( "new-lastName" ) ).click();
            actions.moveToElement( driver.findElement( By.name( "new-lastName" ) ) );
            actions.click();
            actions.sendKeys( lastName );
            actions.build().perform();
            // driver.findElement( By.name( "new-lastName" ) ).clear();
            // driver.findElement( By.name( "new-lastName" ) ).sendKeys(
            // lastName );
        }
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
    @When ( "^The OB/GYN HCP selects the patient (.+) and enters for twins the date of labor (.+), time of labor (.+), date of delivery (.+), time of delivery (.+), delivery method (.+), weight in pounds (.+) and ounces (.+), length (.+), heart rate (.+), blood pressure (.+), first name (.+), and last name (.+)$" )
    public void addLaborandDeliveryReportInfoForTwins ( final String patient, final String dateLabor,
            final String timeLabor, final String dateDelivery, final String timeDelivery, final String deliveryType,
            final String lbs, final String oz, final String length, final String heartRate, final String bloodPres,
            final String firstName, final String lastName ) {
        waitForAngular();

        final WebElement patientElement = driver.findElement( By.cssSelector( "input[value=\"" + patient + "\"]" ) );
        patientElement.click();

        waitForAngular();

        fillInDateTime( "new-dateOfLabor", dateLabor, "new-timeOfLabor", timeLabor );
        fillInDateTime( "new-dateOfDelivery", dateDelivery, "new-timeOfDelivery", timeDelivery );

        driver.findElement( By.name( "new-deliveryMethod" ) ).sendKeys( deliveryType );
        if ( !deliveryType.equals( "Miscarriage" ) ) {

            waitForAngular();
            final Actions actions = new Actions( driver );
            actions.moveToElement( driver.findElement( By.name( "new-weightlbs" ) ) );
            actions.click();
            actions.sendKeys( lbs );
            actions.build().perform();

            actions.moveToElement( driver.findElement( By.name( "new-weightoz" ) ) );
            actions.click();
            actions.sendKeys( oz );
            actions.build().perform();

            actions.moveToElement( driver.findElement( By.name( "new-length" ) ) );
            actions.click();
            actions.sendKeys( length );
            actions.build().perform();

            actions.moveToElement( driver.findElement( By.name( "new-heartRate" ) ) );
            actions.click();
            actions.sendKeys( heartRate );
            actions.build().perform();

            actions.moveToElement( driver.findElement( By.name( "new-bloodPressure" ) ) );
            actions.click();
            actions.sendKeys( bloodPres );
            actions.build().perform();

            actions.moveToElement( driver.findElement( By.name( "new-firstName" ) ) );
            actions.click();
            actions.sendKeys( firstName );
            actions.build().perform();

            actions.moveToElement( driver.findElement( By.name( "new-lastName" ) ) );
            actions.click();
            actions.sendKeys( lastName );
            actions.build().perform();

            // The same procedure, but for the second baby patient
            waitForAngular();

            fillInDateTime( "new-secondDateOfDelivery", dateDelivery, "new-secondTimeOfDelivery", timeDelivery );

            driver.findElement( By.name( "new-secondDeliveryMethod" ) ).sendKeys( deliveryType );
            if ( deliveryType.equals( "Miscarriage" ) ) {

                waitForAngular();
                actions.moveToElement( driver.findElement( By.name( "new-secondWeightlbs" ) ) );
                actions.click();
                actions.sendKeys( lbs );
                actions.build().perform();

                actions.moveToElement( driver.findElement( By.name( "new-secondWeightoz" ) ) );
                actions.click();
                actions.sendKeys( oz );
                actions.build().perform();

                actions.moveToElement( driver.findElement( By.name( "new-secondLength" ) ) );
                actions.click();
                actions.sendKeys( length );
                actions.build().perform();

                actions.moveToElement( driver.findElement( By.name( "new-secondHeartRate" ) ) );
                actions.click();
                actions.sendKeys( heartRate );
                actions.build().perform();

                actions.moveToElement( driver.findElement( By.name( "new-secondBloodPressure" ) ) );
                actions.click();
                actions.sendKeys( bloodPres );
                actions.build().perform();

                actions.moveToElement( driver.findElement( By.name( "new-secondFirstName" ) ) );
                actions.click();
                actions.sendKeys( firstName );
                actions.build().perform();

                actions.moveToElement( driver.findElement( By.name( "new-secondLastName" ) ) );
                actions.click();
                actions.sendKeys( lastName );
                actions.build().perform();
            }
        }

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
        driver.get( baseUrl );
        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        wait.until( ExpectedConditions.titleContains( "HCP Home" ) );
        assertEquals( "iTrust2: HCP Home", driver.getTitle() );
        wait.until( ExpectedConditions.elementToBeClickable( By.name( "transactionTypeCell" ) ) );
        assertTextPresent( "HCP views an OB/GYN Labor and Delivery Report" );
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
