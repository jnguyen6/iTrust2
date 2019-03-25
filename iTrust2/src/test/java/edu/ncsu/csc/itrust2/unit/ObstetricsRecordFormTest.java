/**
 *
 */
package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;

/**
 * Tests the ObstetricRecordForm Class
 *
 * @author Shukri Qubain (scqubain@ncsu.edu)
 *
 */
public class ObstetricsRecordFormTest {

    /**
     * Tests Constructing the form and setting variables
     */
    @Test
    public void test () {
        final ObstetricsRecordForm form = new ObstetricsRecordForm();
        form.setConception( 1999 );
        form.setCurrentRecord( false );
        form.setHoursInLabor( 4 );
        final Long id = (long) 429;
        final Long lmp = (long) 329;
        form.setId( id );
        form.setLmp( lmp );
        form.setTwins( false );
        form.setWeeksPreg( 36 );
        final DeliveryMethod type = DeliveryMethod.Vaginal;
        form.setType( type );
        final ObstetricsRecordForm test = new ObstetricsRecordForm( form.getType(), form.getLmp(), form.getConception(),
                form.getWeeksPreg(), form.getHoursInLabor(), form.isTwins(), form.isCurrentRecord(), form.getId() );
        assertEquals( form.getConception(), test.getConception() );
        assertEquals( form.isCurrentRecord(), test.isCurrentRecord() );
        assertEquals( form.getHoursInLabor(), test.getHoursInLabor() );
        assertEquals( form.getId(), test.getId() );
        assertEquals( form.getLmp(), test.getLmp() );
        assertEquals( form.isTwins(), test.isTwins() );
        assertEquals( form.getWeeksPreg(), test.getWeeksPreg() );
        assertEquals( form.getType(), test.getType() );

    }

}
