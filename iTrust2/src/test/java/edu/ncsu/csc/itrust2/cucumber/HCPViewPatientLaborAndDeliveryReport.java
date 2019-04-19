package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        

        for ( final WebElement element : radioList ) {
        	String v = element.getAttribute( "value" );
        	final ZonedDateTime datetimeLabor = ZonedDateTime.parse( v );
        	String g = "";
        	if(datetimeLabor.getMonthValue() < 10) {
        		g += "0" + datetimeLabor.getMonthValue() + "/";
        	} else {
        		g += datetimeLabor.getMonthValue() + "/";
        	}
        	if(datetimeLabor.getDayOfMonth() < 10) {
        		g += "0" + datetimeLabor.getDayOfMonth() + "/";
        	} else {
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
    
    @Given ( "^there exists an obstetrics patient in the system$" )
    public void patientExistsInSystem () {
        attemptLogout();

        // Create the test User
        final User user = new User( "AliceThirteen", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        user.save();

        // The User must also be created as a Patient
        // to show up in the list of Patients
        final Patient patient = new Patient( user.getUsername() );
        patient.setGender( Gender.Female );
        patient.setEthnicity(Ethnicity.Caucasian);
        patient.setAddress1("140 George Rd.");
        patient.setCity("Raleigh");
        patient.setState(State.NC);
        patient.setZip("27606");
        patient.setPhone("123-456-7891");
        patient.save();
        final ObstetricsRecordForm form = new ObstetricsRecordForm( DeliveryMethod.Vaginal, LocalDate.now().toString(), 2019,
                0, 1, false, true );
        final ObstetricsRecord record = new ObstetricsRecord(form);
        record.setPatient("AliceThirteen");
        record.save();
        attemptLogout();

        // Create the test User
        final User user1 = new User( "tylerOBGYN", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_OBGYN, 1 );
        user1.save();
    }

    /**
     * Creates an HCP of the given type
     *
     * @param hcpType
     *            type of the HCP
     */
    @And ( "^there exists an (.+) HCP in the iTrust2 system$" )
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
    @Then ( "^I log in as an (.+) HCP and navigate to the View Patient Labor and Delivery Report page$" )
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
                	.executeScript( "document.getElementById('HCPViewLaborAndDeliveryReports').click();" );
        	assertEquals( "iTrust2: View Patient Labor and Delivery Reports", driver.getTitle() );
        
        
    }
    
    @Then("^unselected patient text is displayed when no patient is selected$")
    public void unselectedPatient(){
    	waitForAngular();
    	
    	assertTextPresent("Please select a patient to view their labor and delivery reports.");
    	/*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	assertFalse(driver.findElement( By.id( "AliceThirteen" ) ).isSelected());
    }
    
    @Then("^I select to view (.+)'s labor and delivery reports$")
    public void viewPatientReports( final String username ) {
    	waitForAngular();
    	
    	driver.findElement( By.id( username ) ).click();
    	assertTrue(driver.findElement( By.id( "AliceThirteen" ) ).isSelected());
    }
    
    @And("^The patient does have an existing labor and delivery report without twins$")
    public void existingReportNoTwins() {
    	waitForAngular();
    	
    	final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( ZonedDateTime.of(2019, 3, 22, 5, 2, 20, 2, ZoneId.of("-05:00")).toString() );
        form.setDatetimeOfDelivery( ZonedDateTime.of(2019, 3, 22, 10, 2, 20, 2, ZoneId.of("-05:00")).toString() );
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
        record.setPatient( "patient" );
        record.save();
        form.setObstetricsRecord( record );
        form.setDeliveryMethod( record.getDeliveryMethod() );
        LaborDeliveryReport ldRecord;
		try {
			ldRecord = new LaborDeliveryReport(form);
			ldRecord.setPatient("AliceThirteen");
	        ldRecord.save();
		} catch (ParseException e) {
			fail();
		}
    }
    
    @Then("^no current report text is displayed when the patient is selected$")
    public void noReportsClicked() {
    	waitForAngular();
    	
    	assertTextPresent("There are no labor and delivery reports for this patient.");
    	assertTextPresent("Please select a delivery report.");
    }
    
    @And("^The patient has an existing labor and delivery report with twins$")
    public void existingReportWithTwins(){
    	waitForAngular();
    	
    	final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( ZonedDateTime.of(2019, 3, 22, 5, 2, 20, 2, ZoneId.of("-05:00")).toString() );
        form.setDatetimeOfDelivery( ZonedDateTime.of(2019, 3, 22, 10, 2, 20, 2, ZoneId.of("-05:00")).toString() );
        form.setWeight( 3.4 );
        form.setLength( 12.34 );
        form.setHeartRate( 70 );
        form.setBloodPressure( 70 );
        form.setFirstName( "Madhura" );
        form.setLastName( "Waghmare" );
        
        form.setSecondDatetimeOfDelivery( ZonedDateTime.of(2019, 3, 22, 10, 32, 20, 2, ZoneId.of("-05:00")).toString() );
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
        record.setPatient( "patient" );
        record.save();
        form.setObstetricsRecord( record );
        form.setDeliveryMethod( record.getDeliveryMethod() );
        form.setSecondDeliveryMethod( record.getDeliveryMethod() );
        LaborDeliveryReport ldRecord;
		try {
			ldRecord = new LaborDeliveryReport(form);
			ldRecord.setPatient("AliceThirteen");
	        ldRecord.save();
		} catch (ParseException e) {
			fail();
		}
    }

    /**
     * Method to view the patient's labor and delivery report
     *
     * @param username
     *            the patient's username
     * @param dateDelivery
     *            the date of the labor and delivery report
     */
    @And ( "^I select to view (.+)'s labor and delivery reports and the date of the report (.+)$" )
    public void viewPatientLaborDeliveryReport ( final String username, final String dateDelivery ) {
        waitForAngular();
        driver.findElement( By.id( username ) ).click();

        waitForAngular();
        /*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        clickAndCheckDateButton( dateDelivery );
    }

    /**
     * The HCP views the labor and delivery report and validates the information
     * shown
     */
    @Then ( "^The patient's labor and delivery report is displayed when the patient is selected$" )
    public void hcpValidatesLaborDeliveryReport () {
        waitForAngular();
        /*try {
			Thread.sleep(90000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        assertEquals(driver.findElement( By.name( "datel" ) ).getText(), "03/22/2019" );
        assertEquals(driver.findElement( By.name( "timel" ) ).getText(), "6:02 AM" );
        assertEquals(driver.findElement( By.name( "date1" ) ).getText(), "03/22/2019" );
        assertEquals(driver.findElement( By.name( "time1" ) ).getText(), "11:02 AM" );
        assertEquals(driver.findElement( By.name( "type" ) ).getText(), "Cesarean" );
        assertEquals(driver.findElement( By.name( "lbs" ) ).getText(), "3" );
        assertEquals(driver.findElement( By.name( "oz" ) ).getText(), "6" );
        assertEquals(driver.findElement( By.name( "length" ) ).getText(), "12.34" );
        assertEquals(driver.findElement( By.name( "heartRate" ) ).getText(), "70" );
        assertEquals(driver.findElement( By.name( "bp" ) ).getText(), "70" );
        assertEquals(driver.findElement( By.name( "firstName" ) ).getText(), "Madhura" );
        assertEquals(driver.findElement( By.name( "lastName" ) ).getText(), "Waghmare" );
    }
    
    @Then("^The patient's labor and delivery report is displayed with twins when the patient is selected$")
    public void hcpValidatesLaborDeliveryReportWithTwins() {
    	waitForAngular();
    	assertEquals(driver.findElement( By.name( "datel" ) ).getText(), "03/22/2019" );
        assertEquals(driver.findElement( By.name( "timel" ) ).getText(), "6:02 AM" );
        
        assertEquals(driver.findElement( By.name( "date1" ) ).getText(), "03/22/2019" );
        assertEquals(driver.findElement( By.name( "time1" ) ).getText(), "11:02 AM" );
        assertEquals(driver.findElement( By.name( "type" ) ).getText(), "Cesarean" );
        assertEquals(driver.findElement( By.name( "lbs" ) ).getText(), "3" );
        assertEquals(driver.findElement( By.name( "oz" ) ).getText(), "6" );
        assertEquals(driver.findElement( By.name( "length" ) ).getText(), "12.34" );
        assertEquals(driver.findElement( By.name( "heartRate" ) ).getText(), "70" );
        assertEquals(driver.findElement( By.name( "bp" ) ).getText(), "70" );
        assertEquals(driver.findElement( By.name( "firstName" ) ).getText(), "Madhura" );
        assertEquals(driver.findElement( By.name( "lastName" ) ).getText(), "Waghmare" );
        
        assertEquals(driver.findElement( By.name( "dateDel2" ) ).getText(), "03/22/2019" );
        assertEquals(driver.findElement( By.name( "timeDel2" ) ).getText(), "11:32 AM" );
        assertEquals(driver.findElement( By.name( "type2" ) ).getText(), "Cesarean" );
        assertEquals(driver.findElement( By.name( "lbs2" ) ).getText(), "2" );
        assertEquals(driver.findElement( By.name( "oz2" ) ).getText(), "4" );
        assertEquals(driver.findElement( By.name( "length2" ) ).getText(), "10.4" );
        assertEquals(driver.findElement( By.name( "heartRate2" ) ).getText(), "75" );
        assertEquals(driver.findElement( By.name( "bp2" ) ).getText(), "75" );
        assertEquals(driver.findElement( By.name( "firstName2" ) ).getText(), "Nishad" );
        assertEquals(driver.findElement( By.name( "lastName2" ) ).getText(), "Waghmare" );
    }

}