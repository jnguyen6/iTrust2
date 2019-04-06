package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Tests the ObtetricsRecord class
 *
 * @author Jimmy Nguyen (jnguyen6)
 *
 */
public class ObstetricsRecordTest {

    /** Patient to be used for testing */
    final User patient = new User( "patient", "123456", Role.ROLE_PATIENT, 1 );

    /**
     * Delete all instances of ObstetricsRecords before running the tests
     */
    @Before
    public void setUp () {
        DomainObject.deleteAll( ObstetricsRecord.class );
    }

    /**
     * Testing valid ObstetricsRecord objects with a default constructor and
     * with an ObstetricsRecordForm passed into the constructor. All setter
     * methods and the getById method are used and tested.
     */
    @Test
    public void testObstetricsRecordValid () {
        final ObstetricsRecord record = new ObstetricsRecord();
        final LocalDate lmp = LocalDate.parse( "2019-03-02" );

        record.setLmp( lmp );
        record.setConception( 2019 );
        record.setWeeksPreg( 1 );
        record.setHoursInLabor( 25 );
        record.setDeliveryMethod( DeliveryMethod.Cesarean );
        record.setCurrentRecord( false );
        record.setTwins( false );
        record.setPatient( "patient" );
        record.save();

        final ObstetricsRecord copy = ObstetricsRecord.getById( record.getId() );
        assertEquals( record.getLmp(), copy.getLmp() );
        assertEquals( record.getConception(), copy.getConception() );
        assertEquals( record.getWeeksPreg(), copy.getWeeksPreg() );
        assertEquals( record.getHoursInLabor(), copy.getHoursInLabor() );
        assertEquals( record.getDeliveryMethod(), copy.getDeliveryMethod() );
        assertEquals( record.isCurrentRecord(), copy.isCurrentRecord() );
        assertEquals( record.isTwins(), copy.isTwins() );
        assertEquals( record.getPatient(), copy.getPatient() );
        assertEquals( record.getId(), copy.getId() );

        final ObstetricsRecordForm form = new ObstetricsRecordForm();
        form.setConception( 2019 );
        form.setCurrentRecord( false );
        form.setHoursInLabor( 25 );
        final String lmpString = "2019-03-02";
        form.setLmp( lmpString );
        form.setTwins( false );
        form.setWeeksPreg( 1 );
        final DeliveryMethod type = DeliveryMethod.Cesarean;
        form.setType( type );

        final ObstetricsRecord record2 = new ObstetricsRecord( form );
        record2.setPatient( "patient" );
        record2.save();

        assertEquals( record.getLmp(), record2.getLmp() );
        assertEquals( record.getConception(), record2.getConception() );
        assertEquals( record.getWeeksPreg(), record2.getWeeksPreg() );
        assertEquals( record.getHoursInLabor(), record2.getHoursInLabor() );
        assertEquals( record.getDeliveryMethod(), record2.getDeliveryMethod() );
        assertEquals( record.isCurrentRecord(), record2.isCurrentRecord() );
        assertEquals( record.isTwins(), record2.isTwins() );
        assertEquals( record.getPatient(), record2.getPatient() );
        assertNotEquals( record.getId(), record2.getId() );

    }

    /**
     * Test invalid ObstetricsRecord objects using invalid values
     */
    @Test
    public void testObstetricsRecordInvalid () {
        final ObstetricsRecord record = new ObstetricsRecord();
        // Test for invalid LMP
        try {
            record.setLmp( LocalDate.parse( "3000-09-03" ) );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "The LMP must be before the current date", e.getMessage() );
        }

        // Test for invalid year of conception
        try {
            record.setConception( 203 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "The year of conception must be four digits", e.getMessage() );
        }
        try {
            record.setConception( -2000 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "The year of conception must be a nonnegative integer", e.getMessage() );
        }

        // Test for invalid number of weeks pregnant
        try {
            record.setWeeksPreg( -1 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "The number of weeks pregnant must be a nonnegative integer", e.getMessage() );
        }

        // Test for the invalid number of hours in labor
        try {
            record.setHoursInLabor( -1 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "The number of hours in labor must be a nonnegative integer", e.getMessage() );
        }

        // Test for invalid delivery method
        try {
            record.setDeliveryMethod( null );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "The delivery method cannot be null", e.getMessage() );
        }

    }

    /**
     * Tests the getByPatient method for ObstetricsRecord
     */
    @Test
    public void testGetByPatient () {
        final ObstetricsRecord record = new ObstetricsRecord();
        final LocalDate lmp = LocalDate.parse( "2019-03-02" );

        record.setLmp( lmp );
        record.setConception( 2019 );
        record.setWeeksPreg( 1 );
        record.setHoursInLabor( 25 );
        record.setDeliveryMethod( DeliveryMethod.Cesarean );
        record.setCurrentRecord( false );
        record.setTwins( false );
        record.setPatient( "patient" );
        record.save();

        final ObstetricsRecord record2 = new ObstetricsRecord();
        final LocalDate lmp2 = LocalDate.parse( "2019-03-04" );

        record2.setLmp( lmp2 );
        record2.setConception( 2019 );
        record2.setWeeksPreg( 4 );
        record2.setHoursInLabor( 14 );
        record2.setDeliveryMethod( DeliveryMethod.Vaginal );
        record2.setCurrentRecord( false );
        record2.setTwins( true );
        record2.setPatient( "patient" );
        record2.save();

        assertEquals( 2, ObstetricsRecord.getByPatient( "patient" ).size() );

    }
}
