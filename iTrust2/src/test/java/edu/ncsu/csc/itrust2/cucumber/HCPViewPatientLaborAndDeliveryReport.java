package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step definitions for an HCP to view all of the patients' labor and delivery
 * reports.
 *
 * @author Jimmy Nguyen (jnguyen6)
 */
public class HCPViewPatientLaborAndDeliveryReport extends CucumberTest {

    private final String OPH_HCP_TYPE   = "ophthalmologist";
    private final String OD_HCP_TYPE    = "optometrist";
    private final String OBGYN_HCP_TYPE = "obstetrics";

    private final String baseUrl        = "http://localhost:8080/iTrust2";
    private final String hcpString      = "patrickHCP";
    private final String ophHcpString   = "bobbyOPH";
    private final String odHcpString    = "masonOD";
    private final String obgynHcpString = "tylerOBGYN";
    private final String patientString  = "AliceThirteen";

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
     * Creates an HCP of the given type
     *
     * @param hcpType
     *            type of the HCP
     */
    @Given ( "^there exists an (.+) HCP in the iTrust2 system$" )
    public void HCPExists ( final String hcpType ) {
        attemptLogout();

        final User hcp;

        switch ( hcpType ) {
            case OPH_HCP_TYPE:
                hcp = new User( ophHcpString, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                        Role.ROLE_OPH, 1 );
                break;
            case OD_HCP_TYPE:
                hcp = new User( odHcpString, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                        Role.ROLE_OD, 1 );
                break;
            case OBGYN_HCP_TYPE:
                hcp = new User( obgynHcpString, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                        Role.ROLE_OBGYN, 1 );
                break;
            default:
                hcp = new User( hcpString, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                        Role.ROLE_HCP, 1 );
        }
        hcp.save();
    }

    /**
     * The given type of HCP logs in and navigates to the View Patient Labor and
     * Delivery Report page
     *
     * @param hcpType
     *            type of the HCP
     */
    @Then ( "^Then I log in as an (.+) HCP and navigate to the View Patient Labor and Delivery Report page$" )
    public void hcpLoginNavToView ( final String hcpType ) {
        waitForAngular();
        attemptLogout();

        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();

        switch ( hcpType ) {
            case OPH_HCP_TYPE:
                username.sendKeys( ophHcpString );
                break;
            case OD_HCP_TYPE:
                username.sendKeys( odHcpString );
                break;
            case OBGYN_HCP_TYPE:
                username.sendKeys( obgynHcpString );
                break;
            default:
                username.sendKeys( hcpString );
        }

        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        assertEquals( "iTrust2: HCP Home", driver.getTitle() );

        waitForAngular();
        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('HCPViewLaborAndDeliveryReport').click();" );
        assertEquals( "iTrust2: View Patient Labor and Delivery Reports", driver.getTitle() );
    }

    /**
     * Method to view the patient's labor and delivery report
     *
     * @param username
     *            the patient's username
     * @param dateDelivery
     *            the date of the labor and delivery report
     */
    @And ( "^I select to view <username>'s labor and delivery reports and the date of the report (.+)$" )
    public void viewPatientLaborDeliveryReport ( final String username, final String dateDelivery ) {
        waitForAngular();
        driver.findElement( By.id( username ) ).click();

        waitForAngular();

        clickAndCheckDateButton( dateDelivery );
    }

    /**
     * The HCP views the labor and delivery report and validates the information
     * shown
     */
    @Then ( "^The patient's labor and delivery report is displayed when the patient is selected$" )
    public void hcpValidatesLaborDeliveryReport () {
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
