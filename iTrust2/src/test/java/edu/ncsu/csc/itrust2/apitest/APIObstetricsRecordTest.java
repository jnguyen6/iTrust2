package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import edu.ncsu.csc.itrust2.models.persistent.LogEntry;
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

    private MockMvc               mvc;

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
    }

    /**
     * Tests APIObstetricsRecordController's createRecord endpoint with an
     * invaild record
     *
     * @throws Exception
     *             the exception if an error has occurred while using MVC
     */
    @Test
    @WithMockUser ( username = "obgyn", roles = { "USER", "HCP", "OB/GYN" } )
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

        mvc.perform( post( "/api/v1/obstetricsRecord/" + patient.getSelf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recordForm ) ) ).andExpect( status().isBadRequest() );

    }

    /**
     * Tests APIObstetricsRecordController's getRecordsPatient endpoint as a
     * patient
     *
     * @throws Exception
     *             the exception if an error has occurred while using MVC
     */
    @Test
    @WithMockUser ( username = "obgyn", roles = { "USER", "PATIENT", "OB/GYN" } )
    public void testObstetricsRecordAPIAsPatient () throws Exception {
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
        recordForm.setCurrentRecord( true );
        recordForm.setHoursInLabor( 5 );
        recordForm.setLmp( "2019-03-03" );
        recordForm.setTwins( false );
        recordForm.setType( DeliveryMethod.Vaginal );
        recordForm.setWeeksPreg( 7 );

        assertNotNull( Patient.getByName( "patient" ) );

        // Check for invalid REST API call
        // try {
        // mvc.perform( get( "/api/v1/obstetricsRecord/patient" ) ).andExpect(
        // status().isForbidden() );
        // fail();
        // }
        // catch ( final Exception e ) {
        // assertTrue( e.getCause() instanceof AccessDeniedException );
        // }

        // First, add the new obstetrics record to the database system
        mvc.perform( post( "/api/v1/obstetricsRecord/" + patient.getSelf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recordForm ) ) ).andExpect( status().isOk() );

        // Then, retrieve the log entry corresponding to the newly created
        // record
        List<LogEntry> entries = LoggerUtil.getAllForUser( "obgyn" );
        assertEquals( TransactionType.CREATE_NEW_OBSTETRICS_RECORD, entries.get( entries.size() - 1 ).getLogCode() );

        // Then, retrieve the record as a patient
        mvc.perform( get( "/api/v1/obstetricsRecord" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        // And check to see if the log info is correct
        entries = LoggerUtil.getAllForUser( "patient" );
        assertEquals( TransactionType.PATIENT_VIEW_OBSTETRICS_RECORD, entries.get( entries.size() - 1 ).getLogCode() );
    }

    // /**
    // * Tests APIFoodDiaryController's endpoints as a patient
    // *
    // * @throws Exception
    // */
    // @Test
    // @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    // public void testFoodDiaryAPIAsPatient () throws Exception {
    // final UserForm uForm = new UserForm( "patient", "123456",
    // Role.ROLE_PATIENT, 1 );
    // mvc.perform( post( "/api/v1/users" ).contentType(
    // MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( uForm ) ) );
    //
    // final PatientForm patient = new PatientForm();
    // patient.setAddress1( "1 Test Street" );
    // patient.setAddress2( "Some Location" );
    // patient.setBloodType( BloodType.APos.toString() );
    // patient.setCity( "Viipuri" );
    // patient.setDateOfBirth( "6/15/1977" );
    // patient.setEmail( "patient@itrust.fi" );
    // patient.setEthnicity( Ethnicity.Caucasian.toString() );
    // patient.setFirstName( "Patient" );
    // patient.setGender( Gender.Male.toString() );
    // patient.setLastName( "Walhelm" );
    // patient.setPhone( "123-456-7890" );
    // patient.setSelf( "patient" );
    // patient.setState( State.NC.toString() );
    // patient.setZip( "27514" );
    // mvc.perform( post( "/api/v1/patients" ).contentType(
    // MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( patient ) ) );
    //
    // final FoodDiaryEntryForm def = new FoodDiaryEntryForm();
    //
    // final Calendar cal = new GregorianCalendar( 2018, 9, 3 );
    // // def.setDate( cal.getTimeInMillis() );
    // def.setMealType( MealType.Lunch );
    // def.setFood( "Peanut Butter and Jelly Sandwich" );
    // def.setServings( 1 );
    // def.setCalories( 900 );
    // def.setFat( 30 );
    // def.setSodium( 60 );
    // def.setCarbs( 100 );
    // def.setSugars( 50 );
    // def.setFiber( 40 );
    // def.setProtein( 10 );
    //
    // try {
    // mvc.perform( get( "/api/v1/diary/patient" ) ).andExpect(
    // status().isForbidden() );
    // fail();
    // }
    // catch ( final Exception e ) {
    // assertTrue( e.getCause() instanceof AccessDeniedException );
    // }
    //
    // mvc.perform( post( "/api/v1/diary" ).contentType(
    // MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( def ) ) ).andExpect( status().isOk() );
    //
    // List<LogEntry> entries = LoggerUtil.getAllForUser( "patient" );
    // assertEquals( TransactionType.CREATE_FOOD_DIARY_ENTRY, entries.get(
    // entries.size() - 1 ).getLogCode() );
    //
    // mvc.perform( get( "/api/v1/diary" ) ).andExpect( status().isOk() )
    // .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE
    // ) );
    //
    // entries = LoggerUtil.getAllForUser( "patient" );
    // assertEquals( TransactionType.PATIENT_VIEW_FOOD_DIARY_ENTRY, entries.get(
    // entries.size() - 1 ).getLogCode() );
    //
    // }
    //
    // /**
    // * Tests APIFoodDiaryController's endpoints as an HCP.
    // *
    // * @throws Exception
    // */
    // @Test
    // @WithMockUser ( username = "hcp", roles = { "HCP" } )
    // public void testFoodDiaryAPIAsHCP () throws Exception {
    // final UserForm uForm = new UserForm( "patient", "123456",
    // Role.ROLE_PATIENT, 1 );
    // mvc.perform( post( "/api/v1/users" ).contentType(
    // MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( uForm ) ) );
    //
    // final PatientForm patient = new PatientForm();
    // patient.setAddress1( "1 Test Street" );
    // patient.setAddress2( "Some Location" );
    // patient.setBloodType( BloodType.APos.toString() );
    // patient.setCity( "Viipuri" );
    // patient.setDateOfBirth( "6/15/1977" );
    // patient.setEmail( "patient@itrust.fi" );
    // patient.setEthnicity( Ethnicity.Caucasian.toString() );
    // patient.setFirstName( "Patient" );
    // patient.setGender( Gender.Male.toString() );
    // patient.setLastName( "Walhelm" );
    // patient.setPhone( "123-456-7890" );
    // patient.setSelf( "patient" );
    // patient.setState( State.NC.toString() );
    // patient.setZip( "27514" );
    // mvc.perform( post( "/api/v1/patients" ).contentType(
    // MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( patient ) ) );
    //
    // try {
    // mvc.perform( get( "/api/v1/diary" ) ).andExpect( status().isForbidden()
    // );
    // fail();
    // }
    // catch ( final Exception e ) {
    // assertTrue( e.getCause() instanceof AccessDeniedException );
    // }
    //
    // mvc.perform( get( "/api/v1/diary/patientdne" ) ).andExpect(
    // status().isNotFound() );
    //
    // mvc.perform( get( "/api/v1/diary/patient" ) ).andExpect( status().isOk()
    // )
    // .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE
    // ) );
    //
    // final List<LogEntry> entries = LoggerUtil.getAllForUser( "patient" );
    // assertEquals( TransactionType.HCP_VIEW_FOOD_DIARY_ENTRY, entries.get(
    // entries.size() - 1 ).getLogCode() );
    // }

}
