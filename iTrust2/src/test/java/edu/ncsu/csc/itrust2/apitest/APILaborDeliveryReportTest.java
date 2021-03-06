package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryReportForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.LaborDeliveryReport;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test for the API functionality for interacting with labor and delivery
 * reports
 *
 * @author Madhura Waghmare (mswaghma)
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APILaborDeliveryReportTest {

    /** MockMvc object used for integration testing. */
    private MockMvc               mvc;

    /** Java API used to convert java objects to JSON. */
    private Gson                  gson;

    /** WebApplicationContext object used for web-related components. */
    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        final Patient p = Patient.getByName( "patient" );
        if ( p != null ) {
            p.delete();
        }

        final User u = User.getByName( "obgyn" );
        if ( u != null ) {
            u.delete();
        }

        DomainObject.deleteAll( LaborDeliveryReport.class );

        gson = new GsonBuilder().create();
    }

    /**
     * Tests APILaborDeliveryReportController's createLaborDeliveryReport
     * endpoints with an invaild and valid reports
     *
     * @throws Exception
     *             the exception if an error has occurred while using MVC
     */
    @Test
    @WithMockUser ( username = "obgyn", roles = { "OBGYN" } )
    public void testLaborDeliveryReportCreateReport () throws Exception {
        // First, initialize the patient
        final UserForm patientForm = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patientForm ) ) );

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "patient@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Patient" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "patient" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        mvc.perform( post( "/api/v1/patients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        // Then initialize the OB/GYN HCP
        final UserForm userForm = new UserForm( "obgyn", "123456", Role.ROLE_OBGYN, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userForm ) ) );

        final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( ZonedDateTime.now().toString() );
        form.setDatetimeOfDelivery( ZonedDateTime.now().toString() );
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
        record.setTwins( true );
        record.setPatient( "patient" );
        record.save();
        form.setObstetricsRecord( record );

        form.setSecondDatetimeOfDelivery( ZonedDateTime.now().toString() );
        form.setSecondWeight( 2.3 );
        form.setSecondLength( 10.4 );
        form.setSecondHeartRate( 75 );
        form.setSecondBloodPressure( 75 );
        form.setSecondFirstName( "Nishad" );
        form.setSecondLastName( "Waghmare" );
        form.setDeliveryMethod( record.getDeliveryMethod() );
        form.setSecondDeliveryMethod( record.getDeliveryMethod() );

        final LaborDeliveryReportForm test = new LaborDeliveryReportForm();
        test.setDatetimeOfLabor( form.getDatetimeOfLabor() );
        test.setDatetimeOfDelivery( form.getDatetimeOfDelivery() );
        test.setWeight( form.getWeight() );
        test.setLength( form.getLength() );
        test.setHeartRate( form.getHeartRate() );
        test.setBloodPressure( form.getBloodPressure() );
        test.setFirstName( form.getFirstName() );
        test.setLastName( form.getLastName() );
        test.setSecondDatetimeOfDelivery( form.getSecondDatetimeOfDelivery() );
        test.setSecondWeight( form.getSecondWeight() );
        test.setSecondLength( form.getSecondLength() );
        test.setSecondHeartRate( form.getSecondHeartRate() );
        test.setSecondBloodPressure( form.getSecondBloodPressure() );
        test.setSecondFirstName( form.getSecondFirstName() );
        test.setSecondLastName( form.getSecondLastName() );
        test.setObstetricsRecord( form.getObstetricsRecord() );
        test.setDeliveryMethod( form.getDeliveryMethod() );
        test.setSecondDeliveryMethod( form.getDeliveryMethod() );

        // Make invalid post request - male patient
        mvc.perform( post( "/api/v1/laborDeliveryReports/" + patient.getSelf() )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isBadRequest() );
        assertEquals( 0, LaborDeliveryReport.getByPatient( "patient" ).size() );

        // Delete all the patients in the system before starting next tests
        DomainObject.deleteAll( Patient.class );

        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "patient@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Patient" );
        patient.setGender( Gender.Female.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "patient" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        mvc.perform( post( "/api/v1/patients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        // Then, create new labor and delivery report
        final String newRecString = mvc
                .perform( post( "/api/v1/laborDeliveryReports/" + patient.getSelf() )
                        .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final LaborDeliveryReport newRep = gson.fromJson( newRecString, LaborDeliveryReport.class );
        assertEquals( 1, LaborDeliveryReport.getByPatient( "patient" ).size() );

        record.setCurrentRecord( false );
        final LaborDeliveryReportForm form2 = new LaborDeliveryReportForm();
        form2.setDatetimeOfLabor( ZonedDateTime.now().toString() );
        form2.setDatetimeOfDelivery( ZonedDateTime.now().toString() );
        form2.setWeight( 3.4 );
        form2.setLength( 12.34 );
        form2.setHeartRate( 70 );
        form2.setBloodPressure( 70 );
        form2.setFirstName( "Gabe" );
        form2.setLastName( "Walker" );
        form2.setObstetricsRecord( record );
        form2.setSecondDatetimeOfDelivery( ZonedDateTime.now().toString() );
        form2.setSecondWeight( 2.3 );
        form2.setSecondLength( 10.4 );
        form2.setSecondHeartRate( 75 );
        form2.setSecondBloodPressure( 75 );
        form2.setSecondFirstName( "Asia" );
        form2.setSecondLastName( "Walker" );
        form2.setDeliveryMethod( record.getDeliveryMethod() );
        form2.setSecondDeliveryMethod( record.getDeliveryMethod() );

        final LaborDeliveryReportForm test2 = new LaborDeliveryReportForm();
        test2.setDatetimeOfLabor( form2.getDatetimeOfLabor() );
        test2.setDatetimeOfDelivery( form2.getDatetimeOfDelivery() );
        test2.setWeight( form2.getWeight() );
        test2.setLength( form2.getLength() );
        test2.setHeartRate( form2.getHeartRate() );
        test2.setBloodPressure( form2.getBloodPressure() );
        test2.setFirstName( form2.getFirstName() );
        test2.setLastName( form2.getLastName() );
        test2.setSecondDatetimeOfDelivery( form2.getSecondDatetimeOfDelivery() );
        test2.setSecondWeight( form2.getSecondWeight() );
        test2.setSecondLength( form2.getSecondLength() );
        test2.setSecondHeartRate( form2.getSecondHeartRate() );
        test2.setSecondBloodPressure( form2.getSecondBloodPressure() );
        test2.setSecondFirstName( form2.getSecondFirstName() );
        test2.setSecondLastName( form2.getSecondLastName() );
        test2.setObstetricsRecord( form2.getObstetricsRecord() );
        test2.setDeliveryMethod( form2.getDeliveryMethod() );
        test2.setSecondDeliveryMethod( form2.getDeliveryMethod() );

        // We're going to assume the obstetrics record is false (not current)
        // // Attempt to add another new labor delivery reports
        // mvc.perform( post( "/api/v1/laborDeliveryReports/" +
        // patient.getSelf() )
        // .contentType( MediaType.APPLICATION_JSON ).content(
        // TestUtils.asJsonString( form2 ) ) )
        // .andExpect( status().isBadRequest() );
        // assertEquals( 1, LaborDeliveryReport.getByPatient( "patient" ).size()
        // );
    }

    /**
     * Tests APILaborDeliveryReportController's editLaborDeliveryReport
     * endpoints with an invaild and valid input
     *
     * @throws Exception
     *             the exception if an error has occurred while using MVC
     */
    @Test
    @WithMockUser ( username = "obgyn", roles = { "OBGYN" } )
    public void testLaborDeliveryReportEditReport () throws Exception {
        // First, initialize the patient
        final UserForm patientForm = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patientForm ) ) );

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Holland" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "patient@itrust.fi" );
        patient.setEthnicity( Ethnicity.Asian.toString() );
        patient.setFirstName( "Patient" );
        patient.setGender( Gender.Female.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "patient" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        mvc.perform( post( "/api/v1/patients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        // Then initialize the OB/GYN HCP
        final UserForm userForm = new UserForm( "obgyn", "123456", Role.ROLE_OBGYN, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userForm ) ) );

        final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( ZonedDateTime.now().toString() );
        form.setDatetimeOfDelivery( ZonedDateTime.now().toString() );
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
        record.setTwins( true );
        record.setPatient( "patient" );
        record.save();
        form.setObstetricsRecord( record );

        form.setSecondDatetimeOfDelivery( ZonedDateTime.now().toString() );
        form.setSecondWeight( 2.3 );
        form.setSecondLength( 10.4 );
        form.setSecondHeartRate( 75 );
        form.setSecondBloodPressure( 75 );
        form.setSecondFirstName( "Nishad" );
        form.setSecondLastName( "Waghmare" );
        form.setDeliveryMethod( record.getDeliveryMethod() );
        form.setSecondDeliveryMethod( record.getDeliveryMethod() );

        final LaborDeliveryReportForm form2 = new LaborDeliveryReportForm();
        form2.setDatetimeOfLabor( ZonedDateTime.now().toString() );
        form2.setDatetimeOfDelivery( ZonedDateTime.now().toString() );
        form2.setWeight( 3.4 );
        form2.setLength( 12.34 );
        form2.setHeartRate( 70 );
        form2.setBloodPressure( 70 );
        form2.setFirstName( "Madhura" );
        form2.setLastName( "Waghmare" );
        form2.setObstetricsRecord( record );
        form2.setSecondDatetimeOfDelivery( ZonedDateTime.now().toString() );
        form2.setSecondWeight( 2.3 );
        form2.setSecondLength( 10.4 );
        form2.setSecondHeartRate( 75 );
        form2.setSecondBloodPressure( 75 );
        form2.setSecondFirstName( "Nishad" );
        form2.setSecondLastName( "Waghmare" );
        form2.setDeliveryMethod( record.getDeliveryMethod() );
        form2.setSecondDeliveryMethod( record.getDeliveryMethod() );

        // Then, create new labor and delivery report
        final String newRecString = mvc
                .perform( post( "/api/v1/laborDeliveryReports/" + patient.getSelf() )
                        .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final LaborDeliveryReport newRep = gson.fromJson( newRecString, LaborDeliveryReport.class );
        assertEquals( 1, LaborDeliveryReport.getByPatient( "patient" ).size() );

        // Then update report
        mvc.perform( put( "/api/v1/laborDeliveryReports/" + newRep.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form2 ) ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();
        assertEquals( 1, LaborDeliveryReport.getByPatient( "patient" ).size() );
    }

    /**
     * Tests APILaborDeliveryReportController's getLaborDeliveryReportsPatient
     * endpoint as a patient.
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    public void testLaborDeliveryReportAPIAsPatient () throws Exception {
        // Initialize the patient
        final UserForm patientForm = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patientForm ) ) );

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "patient@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Patient" );
        patient.setGender( Gender.Female.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "patient" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        mvc.perform( post( "/api/v1/patients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        // Then initialize the OB/GYN HCP
        final UserForm userForm = new UserForm( "obgyn", "123456", Role.ROLE_OBGYN, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userForm ) ) );

        mvc.perform( get( "/api/v1/laborDeliveryReports" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        assertEquals( 0, LaborDeliveryReport.getByPatient( "patient" ).size() );

        final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( ZonedDateTime.now().toString() );
        form.setDatetimeOfDelivery( ZonedDateTime.now().toString() );
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
        record.setTwins( true );
        record.setPatient( "patient" );
        record.save();
        form.setObstetricsRecord( record );

        form.setSecondDatetimeOfDelivery( ZonedDateTime.now().toString() );
        form.setSecondWeight( 2.3 );
        form.setSecondLength( 10.4 );
        form.setSecondHeartRate( 75 );
        form.setSecondBloodPressure( 75 );
        form.setSecondFirstName( "Nishad" );
        form.setSecondLastName( "Waghmare" );
        form.setDeliveryMethod( record.getDeliveryMethod() );
        form.setSecondDeliveryMethod( record.getDeliveryMethod() );

        final LaborDeliveryReport report = new LaborDeliveryReport( form );
        report.setPatient( "patient" );
        report.save();

        // Retrieve the reports as a patient - one record
        mvc.perform( get( "/api/v1/laborDeliveryReports" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        assertEquals( 1, LaborDeliveryReport.getByPatient( "patient" ).size() );

    }

    /**
     * Tests APILaborDeliveryReportController's getLaborDeliveryReportsPatient
     * endpoint as a HCP or OBGYN
     */
    @Test
    @WithMockUser ( username = "obgyn", roles = { "OBGYN", "HCP" } )
    public void testLaborDeliveryReportAPIAsHCP () throws Exception {
        // First, initialize the patient
        final UserForm patientForm = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patientForm ) ) );

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "patient@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Patient" );
        patient.setGender( Gender.Female.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "patient" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        mvc.perform( post( "/api/v1/patients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        // Then initialize the OB/GYN HCP
        final UserForm userForm = new UserForm( "obgyn", "123456", Role.ROLE_OBGYN, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userForm ) ) );

        final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( ZonedDateTime.now().toString() );
        form.setDatetimeOfDelivery( ZonedDateTime.now().toString() );
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
        record.setTwins( true );
        record.setPatient( "patient" );
        record.save();
        form.setObstetricsRecord( record );

        form.setSecondDatetimeOfDelivery( ZonedDateTime.now().toString() );
        form.setSecondWeight( 2.3 );
        form.setSecondLength( 10.4 );
        form.setSecondHeartRate( 75 );
        form.setSecondBloodPressure( 75 );
        form.setSecondFirstName( "Nishad" );
        form.setSecondLastName( "Waghmare" );
        form.setDeliveryMethod( record.getDeliveryMethod() );
        form.setSecondDeliveryMethod( record.getDeliveryMethod() );

        // Then, create new labor and delivery report
        final String newRecString = mvc
                .perform( post( "/api/v1/laborDeliveryReports/" + patient.getSelf() )
                        .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final LaborDeliveryReport newRep = gson.fromJson( newRecString, LaborDeliveryReport.class );
        assertEquals( 1, LaborDeliveryReport.getByPatient( "patient" ).size() );

        // Retrieve the reports as a HCP - one record
        mvc.perform( get( "/api/v1/laborDeliveryReports/" + patient.getSelf() ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        assertEquals( 1, LaborDeliveryReport.getByPatient( "patient" ).size() );

    }

}
