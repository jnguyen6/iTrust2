package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.time.ZonedDateTime;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.GeneralObstetricsForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.GeneralObstetrics;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Test for Obstetrics Office Visit objects.
 *
 * @author srazdan
 *
 */
public class GeneralObstetricsTest {

    /**
     * Tests the GeneralObstetrics office visit class fields.
     *
     * @throws ParseException
     *             if the form data cannot be parsed to a GeneralObstetrics
     *             office visit.
     */
    @Test
    public void testGeneralObstetrics () throws ParseException {
        DomainObject.deleteAll( GeneralObstetrics.class );

        final Hospital hosp = new Hospital( "Dr. Jenkins' Insane Asylum", "123 Main St", "12345", "NC" );
        hosp.save();

        final GeneralObstetrics visit = new GeneralObstetrics();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setHcp( User.getByName( "tylerOBGYN" ) );
        bhm.setPatient( User.getByName( "AliceThirteen" ) );
        bhm.setHdl( 75 );
        bhm.setLdl( 75 );
        bhm.setHeight( 75f );
        bhm.setWeight( 130f );
        bhm.setTri( 300 );
        bhm.setSystolic( 150 );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        bhm.setPatientSmokingStatus( PatientSmokingStatus.NEVER );
        bhm.save();

        visit.setBasicHealthMetrics( bhm );
        visit.setType( AppointmentType.GENERAL_OBSTETRICS );
        visit.setHospital( hosp );
        visit.setPatient( User.getByName( "AliceThirteen" ) );
        visit.setHcp( User.getByName( "tylerOBGYN" ) );
        visit.setDate( ZonedDateTime.now() );
        visit.setWeeksPregnant( 2 );
        visit.setFetalHeartRate( 130 );
        visit.setFundalHeight( 3.0 );
        visit.setIsTwins( false );
        visit.setIsLowLyingPlacenta( false );
        visit.setId( visit.getId() );
        visit.save();

        // Test the visit's persistence
        final GeneralObstetrics copy = GeneralObstetrics.getById( visit.getId() );
        assertEquals( visit.getId(), copy.getId() );
        assertEquals( visit.getAppointment(), copy.getAppointment() );
        assertEquals( visit.getBasicHealthMetrics(), copy.getBasicHealthMetrics() );
        assertEquals( visit.getHcp(), copy.getHcp() );
        assertEquals( visit.getHospital().getName(), copy.getHospital().getName() );
        assertEquals( visit.getPatient(), copy.getPatient() );
        assertEquals( visit.getWeeksPregnant(), copy.getWeeksPregnant() );
        assertEquals( visit.getFetalHeartRate(), copy.getFetalHeartRate() );
        assertEquals( visit.getFundalHeight(), copy.getFundalHeight() );
        assertEquals( visit.getIsTwins(), copy.getIsTwins() );
        assertEquals( visit.getIsLowLyingPlacenta(), copy.getIsLowLyingPlacenta() );

        // Test the form object
        final GeneralObstetricsForm form = new GeneralObstetricsForm( visit );
        form.setPreScheduled( null );
        assertEquals( visit.getId().toString(), form.getId() );
        assertEquals( visit.getHcp().getUsername(), form.getHcp() );
        assertEquals( visit.getHospital().getName(), form.getHospital() );
        assertEquals( visit.getPatient().getUsername(), form.getPatient() );

        final GeneralObstetrics clone = new GeneralObstetrics( form );
        assertEquals( visit.getId(), clone.getId() );
        assertEquals( visit.getAppointment(), clone.getAppointment() );
        assertEquals( visit.getBasicHealthMetrics().getDiastolic(), clone.getBasicHealthMetrics().getDiastolic() );
        assertEquals( visit.getHcp(), clone.getHcp() );
        assertEquals( visit.getHospital().getName(), clone.getHospital().getName() );
        assertEquals( visit.getPatient(), clone.getPatient() );

        visit.save();

        visit.delete();

    }

    /**
     * Tests the GeneralObstetrics office visit form.
     *
     * @throws NumberFormatException
     *             if the form String can't be converted to an integer
     * @throws ParseException
     *             if the form data cannot be parsed to a GeneralOphthalmology
     *             office visit.
     */
    @Test
    public void testGeneralObstetricsForm () throws NumberFormatException, ParseException {
        final GeneralObstetricsForm visit = new GeneralObstetricsForm();

        visit.setDate( "2048-04-16T09:50:00.000-04:00" ); // 4/16/2048 9:50 AM
        visit.setHcp( "hcp" );
        visit.setPatient( "patient" );
        visit.setNotes( "Test office visit" );
        visit.setType( AppointmentType.GENERAL_OBSTETRICS.toString() );
        visit.setHospital( "iTrust Test Hospital 2" );
        visit.setDiastolic( 150 );
        visit.setHdl( 75 );
        visit.setLdl( 75 );
        visit.setHeight( 75f );
        visit.setWeight( 130f );
        visit.setTri( 300 );
        visit.setSystolic( 150 );
        visit.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        visit.setPatientSmokingStatus( PatientSmokingStatus.NEVER );
        visit.setWeeksPregnant( 2 );
        visit.setFetalHeartRate( 130 );
        visit.setFundalHeight( 3.0 );
        visit.setIsTwins( false );
        visit.setIsLowLyingPlacenta( false );
        visit.setId( visit.getId() );

        final GeneralObstetrics ov = new GeneralObstetrics( visit );

        assertEquals( visit.getHcp(), ov.getHcp().getUsername() );
        assertEquals( visit.getPatient(), ov.getPatient().getUsername() );
    }

    /**
     * Tests optional Basic Health metrics.
     */
    @Test
    public void testMissingBHM () {
        DomainObject.deleteAll( GeneralObstetrics.class );

        final Hospital hosp = new Hospital( "Dr. Jenkins' Insane Asylum", "123 Main St", "12345", "NC" );
        hosp.save();

        final GeneralObstetrics visit = new GeneralObstetrics();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setHcp( User.getByName( "tylerOBGYN" ) );
        bhm.setPatient( User.getByName( "AliceThirteen" ) );
        bhm.setHeight( 75f );
        bhm.setWeight( 130f );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        bhm.save();

        visit.setBasicHealthMetrics( BasicHealthMetrics.getById( bhm.getId() ) );
        visit.setType( AppointmentType.GENERAL_OBSTETRICS );
        visit.setHospital( hosp );
        visit.setPatient( User.getByName( "AliceThirteen" ) );
        visit.setHcp( User.getByName( "tylerOBGYN" ) );
        visit.setDate( ZonedDateTime.now() );
        visit.setWeeksPregnant( 2 );
        visit.setFetalHeartRate( 130 );
        visit.setFundalHeight( 3.0 );
        visit.setIsTwins( false );
        visit.setIsLowLyingPlacenta( false );
        visit.setId( visit.getId() );
        visit.save();

        // Test the visit's persistence
        final GeneralObstetrics copy = GeneralObstetrics.getById( visit.getId() );
        assertEquals( visit.getId(), copy.getId() );
        assertEquals( visit.getAppointment(), copy.getAppointment() );
        assertEquals( visit.getBasicHealthMetrics(), copy.getBasicHealthMetrics() );
        assertEquals( visit.getHcp(), copy.getHcp() );
        assertEquals( visit.getHospital().getName(), copy.getHospital().getName() );
        assertEquals( visit.getPatient(), copy.getPatient() );
        assertEquals( visit.getWeeksPregnant(), copy.getWeeksPregnant() );
        assertEquals( visit.getFetalHeartRate(), copy.getFetalHeartRate() );
        assertEquals( visit.getFundalHeight(), copy.getFundalHeight() );
        assertEquals( visit.getIsTwins(), copy.getIsTwins() );
        assertEquals( visit.getIsLowLyingPlacenta(), copy.getIsLowLyingPlacenta() );

        visit.delete();
    }

}
