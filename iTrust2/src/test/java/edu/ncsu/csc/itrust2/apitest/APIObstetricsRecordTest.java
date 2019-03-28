package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.LogEntry;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Test for the API functionality for interacting with obstetrics records
 *
 * @author Shukri Qubain (scqubain@ncsu.edu)
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIObstetricsRecordTest {

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

        DomainObject.deleteAll( ObstetricsRecord.class );

        gson = new GsonBuilder().create();
    }

    /**
     * Tests APIObstetricsRecordController's createRecord and editRecord
     * endpoints with an invaild record
     *
     * @throws Exception
     *             the exception if an error has occurred while using MVC
     */
    @Test
    @WithMockUser ( username = "obgyn", roles = { "OBGYN" } )
    public void testObstetricRecordCreateInvalidRecord () throws Exception {
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

        final ObstetricsRecordForm recordForm = new ObstetricsRecordForm();
        recordForm.setConception( 2019 );
        recordForm.setCurrentRecord( true );
        recordForm.setHoursInLabor( 5 );
        recordForm.setLmp( "2019-03-09" );
        recordForm.setTwins( false );
        recordForm.setType( DeliveryMethod.Vaginal );
        recordForm.setWeeksPreg( 7 );

        // Make invalid post request - male patient
        mvc.perform( post( "/api/v1/obstetricsRecord/" + patient.getSelf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recordForm ) ) ).andExpect( status().isBadRequest() );
        assertEquals( 0, ObstetricsRecord.getByPatient( "patient" ).size() );

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

        recordForm.setConception( 2019 );
        recordForm.setCurrentRecord( true );
        recordForm.setHoursInLabor( 5 );
        recordForm.setLmp( "2019-03-09" );
        recordForm.setTwins( false );
        recordForm.setType( DeliveryMethod.Vaginal );
        recordForm.setWeeksPreg( 7 );

        // Then, create new obstetrics record
        final String newRecString = mvc
                .perform( post( "/api/v1/obstetricsRecord/" + patient.getSelf() )
                        .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( recordForm ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final ObstetricsRecord newRec = gson.fromJson( newRecString, ObstetricsRecord.class );
        assertEquals( 1, ObstetricsRecord.getByPatient( "patient" ).size() );

        recordForm.setConception( 2020 );
        recordForm.setCurrentRecord( true );
        recordForm.setHoursInLabor( 5 );
        recordForm.setLmp( "2019-03-03" );
        recordForm.setTwins( false );
        recordForm.setType( DeliveryMethod.Vaginal );
        recordForm.setWeeksPreg( 7 );

        // Attempt to add another new obstetrics record
        mvc.perform( post( "/api/v1/obstetricsRecord/" + patient.getSelf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recordForm ) ) ).andExpect( status().isBadRequest() );
        assertEquals( 1, ObstetricsRecord.getByPatient( "patient" ).size() );

        // Test updating an obstetrics record - invalid
        recordForm.setConception( 2019 );
        recordForm.setCurrentRecord( false );
        recordForm.setHoursInLabor( 5 );
        recordForm.setLmp( "2019-03-03" );
        recordForm.setTwins( false );
        recordForm.setType( DeliveryMethod.Vaginal );
        recordForm.setWeeksPreg( -1 );

        // Then attempt to update record
        mvc.perform( put( "/api/v1/obstetricsRecord/" + newRec.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recordForm ) ) ).andExpect( status().isBadRequest() );

    }

    /**
     * Tests APIObstetricsRecordController's endpoints as an OB/GYN HCP. This
     * includes creating a valid obstetrics record, updating the record, and
     * retrieving the record as an OB/GYN HCP
     *
     * @throws Exception
     *             the exception if an error has occurred while using MVC
     */
    @Test
    @WithMockUser ( username = "obgyn", roles = { "OBGYN", "HCP" } )
    public void testObstetricsRecordAPIAsOBGYN () throws Exception {
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

        final ObstetricsRecordForm recordForm = new ObstetricsRecordForm();
        recordForm.setConception( 2019 );
        recordForm.setCurrentRecord( false );
        recordForm.setHoursInLabor( 5 );
        recordForm.setLmp( "2019-03-03" );
        recordForm.setTwins( false );
        recordForm.setType( DeliveryMethod.Vaginal );
        recordForm.setWeeksPreg( 7 );

        assertNotNull( Patient.getByName( "patient" ) );

        // First, add the new obstetrics record to the database system
        final String newRecString = mvc
                .perform( post( "/api/v1/obstetricsRecord/" + patient.getSelf() )
                        .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( recordForm ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final ObstetricsRecord newRec = gson.fromJson( newRecString, ObstetricsRecord.class );

        assertEquals( 1, ObstetricsRecord.getByPatient( "patient" ).size() );
        assertEquals( 2019, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).getConception() );
        assertEquals( false, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).isCurrentRecord() );
        assertEquals( 5, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).getHoursInLabor() );
        assertEquals( false, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).isTwins() );
        assertEquals( "Vaginal", ObstetricsRecord.getByPatient( "patient" ).get( 0 ).getDeliveryMethod().toString() );
        assertEquals( 7, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).getWeeksPreg() );

        // Then, retrieve the log entry corresponding to the newly created
        // record
        List<LogEntry> entries = LoggerUtil.getAllForUser( "obgyn" );
        assertEquals( TransactionType.CREATE_NEW_OBSTETRICS_RECORD, entries.get( entries.size() - 1 ).getLogCode() );

        // Then, retrieve the record as a patient
        mvc.perform( get( "/api/v1/obstetricsRecord/" + patient.getSelf() ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        // And check to see if the log info is correct
        entries = LoggerUtil.getAllForUser( "obgyn" );
        assertEquals( TransactionType.HCP_VIEW_OBSTETRICS_RECORD, entries.get( entries.size() - 1 ).getLogCode() );

        // Now update existing obstetrics record
        recordForm.setConception( 2019 );
        recordForm.setCurrentRecord( false );
        recordForm.setHoursInLabor( 8 );
        recordForm.setLmp( "2019-03-03" );
        recordForm.setTwins( true );
        recordForm.setType( DeliveryMethod.Cesarean );
        recordForm.setWeeksPreg( 9 );

        mvc.perform( put( "/api/v1/obstetricsRecord/" + newRec.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recordForm ) ) ).andExpect( status().isOk() );

        assertEquals( 1, ObstetricsRecord.getByPatient( "patient" ).size() );
        assertEquals( 2019, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).getConception() );
        assertEquals( false, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).isCurrentRecord() );
        assertEquals( 8, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).getHoursInLabor() );
        assertEquals( true, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).isTwins() );
        assertEquals( "Cesarean", ObstetricsRecord.getByPatient( "patient" ).get( 0 ).getDeliveryMethod().toString() );
        assertEquals( 9, ObstetricsRecord.getByPatient( "patient" ).get( 0 ).getWeeksPreg() );

    }

    /**
     * Tests APIObstetricsRecordController's getRecordsPatient endpoint as a
     * patient.
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    public void testObstetricsRecordAPIAsPatient () throws Exception {
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

        // Check for invalid REST API call
        // try {
        // mvc.perform( get( "/api/v1/obstetricsRecord/patient" ) ).andExpect(
        // status().isForbidden() );
        // fail();
        // }
        // catch ( final Exception e ) {
        // assertTrue( e.getCause() instanceof AccessDeniedException );
        // }

        // Retrieve the records as a patient - empty
        mvc.perform( get( "/api/v1/obstetricsRecord" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        assertEquals( 0, ObstetricsRecord.getByPatient( "patient" ).size() );

        final ObstetricsRecordForm form = new ObstetricsRecordForm();
        form.setConception( 2019 );
        form.setCurrentRecord( false );
        form.setHoursInLabor( 5 );
        form.setLmp( "2019-03-03" );
        form.setTwins( false );
        form.setType( DeliveryMethod.Vaginal );
        form.setWeeksPreg( 7 );

        final ObstetricsRecord record = new ObstetricsRecord( form );
        record.setPatient( "patient" );
        record.save();

        // Retrieve the records as a patient - one record
        mvc.perform( get( "/api/v1/obstetricsRecord" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        assertEquals( 1, ObstetricsRecord.getByPatient( "patient" ).size() );
    }

}
