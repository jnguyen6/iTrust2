package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Step definitions for DocumentObstetricsRecord feature.
 *
 * @author Sanchit Razdan
 * @author Tyler Outlaw
 */
public class DocumentObstetricsRecordStepDefs extends CucumberTest {

    private final String baseUrl = "http://localhost:8080/iTrust2";

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

    @Given ( "^There exists a patient in the system who can have an obstetrics record.$" )
    public void pateintExistsInSystem () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "tylerOBGYN" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('OBGYNHCPDocumentObstetricsRecord').click();" );
        assertNotNull( driver.findElement( By.name( "patient" ) ) );

        driver.findElement( By.id( "logout" ) ).click();
    }

    @Then ( "^I log in as an OB/GYN HCP.$" )
    public void obgynLogin () {
        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        wait.until( ExpectedConditions.titleContains( "Login to iTrust2" ) );
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
    }

    @When ( "^I navigate to the document obstetrics record page.$" )
    public void navigateToPage () {
        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        wait.until( ExpectedConditions.titleContains( "Welcome to iTrust2 - HCP" ) );
        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('documentObstetricsRecords').click();" );
        driver.manage().timeouts().implicitlyWait( 1, TimeUnit.SECONDS );
        assertTextPresent( "Current Pregnancy" );
        assertTextPresent( "Previous Pregnancies" );
    }

    @And ( "^I select a patient using her (.+).$" )
    public void selectPatient ( final String username ) {
        driver.findElement( By.id( username ) ).click();
    }

    @And ( "^I enter (.+) for a current obstetrics record.$" )
    public void enterLMP ( final String date ) {
        final WebElement dateElement = driver.findElement( By.id( "lmp" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    @And ( "^I click create obstetrics record.$" )
    public void clickToCreateObstetricsRecord () {
        driver.findElement( By.id( "submit" ) );
    }

    @Then ( "^the patient with this (.+) has her current pregnancy is updated.$" )
    public void everythingPresent ( final String username ) {
        driver.findElement( By.id( username ) ).click();
        assertTextNotPresent( "There are no current Obstetrics records for this Patient." );
        assertTextPresent( "Last Menstrual Period" );
        assertTextPresent( "Estimated Due Date" );
        assertTextPresent( "Weeks Pregnant" );
    }

    @And ( "^I enter (.+), (\\d+), (\\d+), (\\d+), (.+), (.+) for a previous obstetrics record.$" )
    public void previousRecordData ( final String date, final int conception, final int weeks, final int hours,
            final String delivery, final String twins ) {
        driver.findElement( By.id( "addEmpty" ) );
        final WebElement dateElement = driver.findElement( By.xpath(
                "//*[@id=\"wrap\"]/div[1]/div/div/div[3]/div/div[2]/div/div[2]/div/div/table/tbody[1]/tr/td[1]/div/input" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );

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
    }

    @And ( "^I click add for the patient's previous pregnancy data with (.+).$" )
    public void addPreviousPregnancy ( final String username ) {
        driver.findElement( By.id( "add" ) ).click();
        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        driver.findElement( By.id( username ) ).click();
        assertTextNotPresent( "There are no previous pregnancies." );
        assertTextPresent( "LMP" );
        assertTextPresent( "Conception Year" );
        assertTextPresent( "# Weeks Pregnant" );
        assertTextPresent( "# Hours in Labor" );
        assertTextPresent( "Delivery Method" );
        assertTextPresent( "Twins" );
    }

    @And ( "^I enter  for a current obstetrics record, where the LMP is invalid$." )
    public void enterInvalidLMP ( final String date ) {
        final WebElement dateElement = driver.findElement( By.id( "lmp" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    @Then ( "^the obstetrics record is not made.$" )
    public void currentRecordNotAdded () {
        assertTextPresent( "Could not add Obstetrics Record." );
    }

    @And ( "^I enter (.+), (.+), (.+), (.+), (.+) and (.+) for a previous obstetrics record, where one input is incorrect." )
    public void invalidPreviousPregnancyData ( final String date, final String conception, final String weeks,
            final String hours, final String delivery, final String twins ) {
        driver.findElement( By.id( "addEmpty" ) );
        final WebElement dateElement = driver.findElement( By.xpath(
                "//*[@id=\"wrap\"]/div[1]/div/div/div[3]/div/div[2]/div/div[2]/div/div/table/tbody[1]/tr/td[1]/div/input" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );

        final WebElement conception_year = driver.findElement( By.name( "conception" ) );
        conception_year.clear();
        conception_year.sendKeys( conception );

        final WebElement weeks_pregnant = driver.findElement( By.name( "weeksPreg" ) );
        weeks_pregnant.clear();
        weeks_pregnant.sendKeys( weeks );

        final WebElement hours_in_labor = driver.findElement( By.name( "hoursInLabor" ) );
        hours_in_labor.clear();
        hours_in_labor.sendKeys( hours );

        final Select delivery_dropdown = new Select( driver.findElement( By.id( "type" ) ) );
        delivery_dropdown.selectByVisibleText( delivery );

        final Select twins_dropdown = new Select( driver.findElement( By.id( "twins" ) ) );
        twins_dropdown.selectByVisibleText( twins );

    }
}
