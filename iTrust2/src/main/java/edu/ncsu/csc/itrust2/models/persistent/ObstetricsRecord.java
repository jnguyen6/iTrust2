package edu.ncsu.csc.itrust2.models.persistent;

import java.util.List;
import java.util.Vector;

import org.hibernate.criterion.Criterion;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;

/**
 * Class representing the Obstetric Record for a patient
 *
 * @author Shukri Qubain (scqubain@ncsu.edu)
 *
 */
public class ObstetricsRecord extends DomainObject<ObstetricsRecord> {

    /** randomly generated long value */
    private final Long serialVersionUID = -4051404539990169870L;

    /** long representing the date for the last menstrual period */
    private Long       lmp;

    /** integer representing the year of conception */
    private int        conception;

    /** integer representing the number of weeks pregnant */
    private int        weeksPreg;

    /** integer representing the number of hours in labor */
    private int        hoursInLabor;

    /** whether or not the pregnancy for this record resulted in twins */
    private boolean    twins;

    /**
     * whether or not this obstetric record is a current or previous pregnancy
     */
    private boolean    currentRecord;

    /** long representing the Obstetrics Record id */
    private Long       id;

    /**
     * Unused public constructor
     */
    public ObstetricsRecord () {

    }

    /**
     * Constructs the ObstetricsRecord object based on the ObstetricsRecord Form
     *
     * @param form
     *            the obstetrics record form
     */
    public ObstetricsRecord ( final ObstetricsRecordForm form ) {
        this.setConception( form.getConception() );
        this.setCurrentRecord( form.isCurrentRecord() );
        this.setHoursInLabor( form.getHoursInLabor() );
        this.setId( form.getId() );
        this.setLmp( form.getLmp() );
        this.setTwins( form.isTwins() );
        this.setWeeksPreg( form.getWeeksPreg() );
    }

    /**
     * Get a specific obstetrics record by the database ID
     *
     * @param id
     *            the database ID
     * @return the specific obstetrics record with the desired ID
     */
    public static ObstetricsRecord getById ( final Long id ) {
        try {
            return (ObstetricsRecord) getWhere( ObstetricsRecord.class, eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Get a list of Obstetric Records by patient.
     *
     * @param patient
     *            the username of the patient whose entries are being searched
     *            for
     * @return a list of ObstetricRecords for the given patient
     */
    @SuppressWarnings ( "unchecked" )
    public static List<ObstetricsRecord> getByPatient ( final String patient ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );

        return (List<ObstetricsRecord>) getWhere( ObstetricsRecord.class, criteria );
    }

    /**
     * Returns the lmp
     *
     * @return lmp the last menstrual period
     */
    public Long getLmp () {
        return lmp;
    }

    /**
     * Sets the lmp
     *
     * @param lmp
     *            the last menstrual period
     */
    public void setLmp ( final Long lmp ) {
        this.lmp = lmp;
    }

    /**
     * Returns the year of conception
     *
     * @return conception the year of conception
     */
    public int getConception () {
        return conception;
    }

    /**
     * Sets the conception
     *
     * @param conception
     *            the year of conception
     */
    public void setConception ( final int conception ) {
        this.conception = conception;
    }

    /**
     * Returns the weeksPreg
     *
     * @return weeksPreg the number of weeks pregnant
     */
    public int getWeeksPreg () {
        return weeksPreg;
    }

    /**
     * Sets the weeksPreg
     *
     * @param weeksPreg
     *            the number of weeks pregnant
     */
    public void setWeeksPreg ( final int weeksPreg ) {
        this.weeksPreg = weeksPreg;
    }

    /**
     * Returns the hoursInLabor
     *
     * @return hoursInLabor the number of hours in labor
     */
    public int getHoursInLabor () {
        return hoursInLabor;
    }

    /**
     * Sets the hoursInLabor
     *
     * @param hoursInLabor
     *            the number of hours in labor
     */
    public void setHoursInLabor ( final int hoursInLabor ) {
        this.hoursInLabor = hoursInLabor;
    }

    /**
     * Returns the twins boolean
     *
     * @return twins whether or not the pregnancy resulted in twins
     */
    public boolean isTwins () {
        return twins;
    }

    /**
     * Sets the twins
     *
     * @param twins
     *            whether or not the pregnancy resulted in twins
     */
    public void setTwins ( final boolean twins ) {
        this.twins = twins;
    }

    /**
     * Returns the currentRecord boolean
     *
     * @return currentRecord whether or not the record is for a prior or current
     *         pregnancy
     */
    public boolean isCurrentRecord () {
        return currentRecord;
    }

    /**
     * Sets the currentRecord
     *
     * @param currentRecord
     *            whether or not the record is for a prior or current
     */
    public void setCurrentRecord ( final boolean currentRecord ) {
        this.currentRecord = currentRecord;
    }

    /**
     * Returns the id
     *
     * @return id the id of the record
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the id of the record
     *
     * @param id
     *            the id of the record
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

}
