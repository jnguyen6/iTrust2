package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import edu.ncsu.csc.itrust2.models.enums.Role;
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

        // Convert MM/dd/yyyy to yyyy-MM-dd
        final String[] dateComponents = viewDate.split( "/" );
        final String dateValue = String.format( "%s-%s-%s", dateComponents[2], dateComponents[0], dateComponents[1] );

        for ( final WebElement element : radioList ) {
            if ( element.getAttribute( "value" ).equals( dateValue ) ) {
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

        final User patient = new User( patientString, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        patient.save();
    }

    // Add code for creating a labor and delivery report (with and without
    // twins) here:

    /**
     * The patient logs in and navigates to the View Patient Labor and Delivery
     * Report page
     */
    @Then ( "^  Then I log in as a patient and navigate to the View Patient Labor and Delivery Report page$" )
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
    @Then ( "^I select the date of the report (.+)$" )
    public void viewLaborDeliveryReport ( final String dateDelivery ) {
        waitForAngular();

        clickAndCheckDateButton( dateDelivery );
    }

    /**
     * The Patient views the labor and delivery report and validates that the
     * information is correct
     */
    @Then ( "^The patient's labor and delivery report is displayed$" )
    public void validateLaborAndDeliveryReport () {
        waitForAngular();
        // Add validation code for labor and delivery report here:
        // assertEquals( "iTrust Test Hospital 2", driver.findElement( By.name(
        // "hospitalName" ) ).getText() );
        // assertEquals( "150", driver.findElement( By.name( "diastolic" )
        // ).getText() );
        // assertEquals( patientString, driver.findElement( By.name(
        // "patientName" ) ).getText() );
        // assertEquals( "75", driver.findElement( By.name( "hdl" ) ).getText()
        // );
        // assertEquals( "75", driver.findElement( By.name( "ldl" ) ).getText()
        // );
        // assertEquals( "75", driver.findElement( By.name( "height" )
        // ).getText() );
        // assertEquals( "130", driver.findElement( By.name( "weight" )
        // ).getText() );
        // assertEquals( "300", driver.findElement( By.name( "tri" ) ).getText()
        // );
        // assertEquals( "150", driver.findElement( By.name( "systolic" )
        // ).getText() );
    }

}
