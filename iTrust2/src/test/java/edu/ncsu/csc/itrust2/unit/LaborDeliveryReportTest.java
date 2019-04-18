package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryReportForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.LaborDeliveryReport;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Tests the LaborDeliveryReport Class
 *
 * @author Sanchit Razdan srazdan
 *
 */
public class LaborDeliveryReportTest {

    /** Patient to be used for testing */
    final User patient = new User( "patient", "123456", Role.ROLE_PATIENT, 1 );

    /**
     * Delete all instances of ObstetricsRecords before running the tests
     */
    @Before
    public void setUp () {
        DomainObject.deleteAll( LaborDeliveryReport.class );
        DomainObject.deleteAll( ObstetricsRecord.class );
    }

    /**
     * Testing valid LaborDeliveryReport objects with a default constructor and
     * with an LaborDeliveryReportForm passed into the constructor. All setter
     * methods and the getById method are used and tested.
     *
     * @throws ParseException
     */
    @Test
    public void testLaborDeliveryReportValid () throws ParseException {
        final LaborDeliveryReport report = new LaborDeliveryReport();
        final ZonedDateTime datetimeLabor = ZonedDateTime.parse( "2018-04-16T09:50:00.000-04:00" );
        report.setDatetimeOfLabor( datetimeLabor );
        final ZonedDateTime datetimeDelivery = ZonedDateTime.parse( "2018-05-16T09:50:00.000-04:00" );
        report.setDatetimeOfDelivery( datetimeDelivery );
        report.setWeight( 3.4 );
        report.setLength( 12.34 );
        report.setHeartRate( 70 );
        report.setBloodPressure( 70 );
        report.setFirstName( "Sanchit" );
        report.setLastName( "Razdan" );

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
        report.setObstetricsRecord( record );

        final ZonedDateTime seconddatetimeDelivery = ZonedDateTime
                .parse( "2018-06-16T09:50:00.000-04:00[America/New_York]" );
        report.setSecondDatetimeOfDelivery( seconddatetimeDelivery );
        report.setSecondWeight( 2.3 );
        report.setSecondLength( 10.4 );
        report.setSecondHeartRate( 75 );
        report.setSecondBloodPressure( 75 );
        report.setSecondFirstName( "Swarnim" );
        report.setSecondLastName( "Razdan" );
        report.setPatient( "patient" );
        report.setDeliveryMethod( record.getDeliveryMethod() );
        report.setSecondDeliveryMethod( record.getDeliveryMethod() );
        report.save();

        final LaborDeliveryReport test = LaborDeliveryReport.getById( report.getId() );
        // assertEquals( report.getDatetimeOfLabor(), test.getDatetimeOfLabor()
        // );
        // assertEquals( report.getDatetimeOfDelivery(),
        // test.getDatetimeOfDelivery() );
        assertEquals( report.getWeight(), test.getWeight() );
        assertEquals( report.getLength(), test.getLength() );
        assertEquals( report.getHeartRate(), test.getHeartRate() );
        assertEquals( report.getBloodPressure(), test.getBloodPressure() );
        assertEquals( report.getFirstName(), test.getFirstName() );
        assertEquals( report.getLastName(), test.getLastName() );
        assertEquals( report.getSecondDatetimeOfDelivery(), test.getSecondDatetimeOfDelivery() );
        assertEquals( report.getSecondWeight(), test.getSecondWeight() );
        assertEquals( report.getSecondLength(), test.getSecondLength() );
        assertEquals( report.getSecondHeartRate(), test.getSecondHeartRate() );
        assertEquals( report.getSecondBloodPressure(), test.getSecondBloodPressure() );
        assertEquals( report.getSecondFirstName(), test.getSecondFirstName() );
        assertEquals( report.getSecondLastName(), test.getSecondLastName() );
        assertEquals( report.getPatient(), test.getPatient() );
        assertEquals( report.getId(), test.getId() );
        assertEquals( report.getDeliveryMethod(), test.getDeliveryMethod() );
        assertEquals( report.getSecondDeliveryMethod(), test.getSecondDeliveryMethod() );

        final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDatetimeOfLabor( report.getDatetimeOfLabor().toString() );
        form.setDatetimeOfDelivery( report.getDatetimeOfDelivery().toString() );
        form.setWeight( 3.4 );
        form.setLength( 12.34 );
        form.setHeartRate( 70 );
        form.setBloodPressure( 70 );
        form.setFirstName( "Sanchit" );
        form.setLastName( "Razdan" );

        final ObstetricsRecord record2 = new ObstetricsRecord();
        final LocalDate lmp2 = LocalDate.parse( "2019-03-02" );
        record2.setLmp( lmp2 );
        record2.setConception( 2019 );
        record2.setWeeksPreg( 1 );
        record2.setHoursInLabor( 25 );
        record2.setDeliveryMethod( DeliveryMethod.Cesarean );
        record2.setCurrentRecord( true );
        record2.setTwins( true );
        record2.setPatient( "patient" );
        record2.save();
        form.setObstetricsRecord( record2 );

        form.setSecondDatetimeOfDelivery( report.getSecondDatetimeOfDelivery().toString() );
        form.setSecondWeight( 2.3 );
        form.setSecondLength( 10.4 );
        form.setSecondHeartRate( 75 );
        form.setSecondBloodPressure( 75 );
        form.setSecondFirstName( "Swarnim" );
        form.setSecondLastName( "Razdan" );
        form.setDeliveryMethod( record2.getDeliveryMethod() );
        form.setSecondDeliveryMethod( record2.getDeliveryMethod() );

        final LaborDeliveryReport ldr = new LaborDeliveryReport( form );
        ldr.setPatient( "patient" );

        assertEquals( report.getDatetimeOfLabor(), ldr.getDatetimeOfLabor() );
        assertEquals( report.getDatetimeOfDelivery(), ldr.getDatetimeOfDelivery() );
        assertEquals( report.getWeight(), ldr.getWeight() );
        assertEquals( report.getLength(), ldr.getLength() );
        assertEquals( report.getHeartRate(), ldr.getHeartRate() );
        assertEquals( report.getBloodPressure(), ldr.getBloodPressure() );
        assertEquals( report.getFirstName(), ldr.getFirstName() );
        assertEquals( report.getLastName(), ldr.getLastName() );
        assertEquals( report.getSecondDatetimeOfDelivery(), ldr.getSecondDatetimeOfDelivery() );
        assertEquals( report.getSecondWeight(), ldr.getSecondWeight() );
        assertEquals( report.getSecondLength(), ldr.getSecondLength() );
        assertEquals( report.getSecondHeartRate(), ldr.getSecondHeartRate() );
        assertEquals( report.getSecondBloodPressure(), ldr.getSecondBloodPressure() );
        assertEquals( report.getSecondFirstName(), ldr.getSecondFirstName() );
        assertEquals( report.getSecondLastName(), ldr.getSecondLastName() );
        assertEquals( report.getDeliveryMethod(), ldr.getDeliveryMethod() );
        assertEquals( report.getSecondDeliveryMethod(), ldr.getSecondDeliveryMethod() );
        assertEquals( report.getPatient(), ldr.getPatient() );

    }

    /**
     * Tests the getByPatient method for LaborDeliveryReport
     */
    @Test
    public void testGetByPatient () {

        final LaborDeliveryReport report = new LaborDeliveryReport();
        report.setDatetimeOfLabor( ZonedDateTime.now() );
        report.setDatetimeOfDelivery( ZonedDateTime.now() );
        report.setWeight( 3.4 );
        report.setLength( 12.34 );
        report.setHeartRate( 70 );
        report.setBloodPressure( 70 );
        report.setFirstName( "Sanchit" );
        report.setLastName( "Razdan" );
        report.setSecondDatetimeOfDelivery( ZonedDateTime.now() );
        report.setSecondWeight( 2.3 );
        report.setSecondLength( 10.4 );
        report.setSecondHeartRate( 75 );
        report.setSecondBloodPressure( 75 );
        report.setSecondFirstName( "Swarnim" );
        report.setSecondLastName( "Razdan" );
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
        report.setObstetricsRecord( record );

        report.setPatient( "patient" );
        report.setDeliveryMethod( record.getDeliveryMethod() );
        report.setSecondDeliveryMethod( record.getDeliveryMethod() );
        report.save();

        assertEquals( 1, ObstetricsRecord.getByPatient( "patient" ).size() );
    }

}
