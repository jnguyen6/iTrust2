package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryReportForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;

/**
 * Tests the LaborDeliveryReportForm Class
 *
 * @author Sanchit Razdan (srazdan@ncsu.edu)
 *
 */
public class LaborDeliveryReportFormTest {

    /**
     * Tests Constructing the form and setting variables
     */
    @Test
    public void testLaborDeliveryReportForm () {
        final LaborDeliveryReportForm form = new LaborDeliveryReportForm();
        form.setDateOfLabor( LocalDate.now() );
        form.setTimeOfLabor( "12:00" );
        form.setDateOfDelivery( LocalDate.now() );
        form.setTimeOfDelivery( "15:00" );
        form.setWeight( 3.4 );
        form.setLength( 12.34 );
        form.setHeartRate( 70 );
        form.setBloodPressure( 70 );
        form.setFirstName( "Sanchit" );
        form.setLastName( "Razdan" );
        form.setSecondDateOfDelivery( LocalDate.now() );
        form.setSecondTimeOfDelivery( "15:03" );
        form.setSecondWeight( 2.3 );
        form.setSecondLength( 10.4 );
        form.setSecondHeartRate( 75 );
        form.setSecondBloodPressure( 75 );
        form.setSecondFirstName( "Swarnim" );
        form.setSecondLastName( "Razdan" );
        final ObstetricsRecord record = new ObstetricsRecord();
        final LocalDate lmp = LocalDate.parse( "2019-03-02" );
        record.setLmp( lmp );
        record.setConception( 2019 );
        record.setWeeksPreg( 1 );
        record.setHoursInLabor( 25 );
        record.setDeliveryMethod( DeliveryMethod.Cesarean );
        record.setCurrentRecord( false );
        record.setTwins( true );
        record.setPatient( "patient" );
        record.save();
        form.setObstetricsRecord( record );
        form.setDeliveryMethod( record.getDeliveryMethod() );
        form.setSecondDeliveryMethod( record.getDeliveryMethod() );

        final LaborDeliveryReportForm test = new LaborDeliveryReportForm();
        test.setDateOfLabor( form.getDateOfLabor() );
        test.setTimeOfLabor( form.getTimeOfLabor() );
        test.setDateOfDelivery( form.getDateOfDelivery() );
        test.setTimeOfDelivery( form.getTimeOfDelivery() );
        test.setWeight( form.getWeight() );
        test.setLength( form.getLength() );
        test.setHeartRate( form.getHeartRate() );
        test.setBloodPressure( form.getBloodPressure() );
        test.setFirstName( form.getFirstName() );
        test.setLastName( form.getLastName() );
        test.setSecondDateOfDelivery( form.getSecondDateOfDelivery() );
        test.setSecondTimeOfDelivery( form.getSecondTimeOfDelivery() );
        test.setSecondWeight( form.getSecondWeight() );
        test.setSecondLength( form.getSecondLength() );
        test.setSecondHeartRate( form.getSecondHeartRate() );
        test.setSecondBloodPressure( form.getSecondBloodPressure() );
        test.setSecondFirstName( form.getSecondFirstName() );
        test.setSecondLastName( form.getSecondLastName() );
        test.setObstetricsRecord( form.getObstetricsRecord() );
        test.setDeliveryMethod( record.getDeliveryMethod() );
        test.setSecondDeliveryMethod( form.getSecondDeliveryMethod() );

        assertEquals( form.getDateOfLabor(), test.getDateOfLabor() );
        assertEquals( form.getTimeOfLabor(), test.getTimeOfLabor() );
        assertEquals( form.getDateOfDelivery(), test.getDateOfDelivery() );
        assertEquals( form.getTimeOfDelivery(), test.getTimeOfDelivery() );
        assertEquals( form.getWeight(), test.getWeight() );
        assertEquals( form.getLength(), test.getLength() );
        assertEquals( form.getHeartRate(), test.getHeartRate() );
        assertEquals( form.getBloodPressure(), test.getBloodPressure() );
        assertEquals( form.getFirstName(), test.getFirstName() );
        assertEquals( form.getLastName(), test.getLastName() );
        assertEquals( form.getSecondDateOfDelivery(), test.getSecondDateOfDelivery() );
        assertEquals( form.getSecondTimeOfDelivery(), test.getSecondTimeOfDelivery() );
        assertEquals( form.getSecondWeight(), test.getSecondWeight() );
        assertEquals( form.getSecondLength(), test.getSecondLength() );
        assertEquals( form.getSecondHeartRate(), test.getSecondHeartRate() );
        assertEquals( form.getSecondBloodPressure(), test.getSecondBloodPressure() );
        assertEquals( form.getSecondFirstName(), test.getSecondFirstName() );
        assertEquals( form.getSecondLastName(), test.getSecondLastName() );
        assertEquals( form.getDeliveryMethod(), test.getDeliveryMethod() );
        assertEquals( form.getSecondDeliveryMethod(), test.getSecondDeliveryMethod() );

    }

}
