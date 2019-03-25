package edu.ncsu.csc.itrust2.forms.hcp;

import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;

/**
 * Class represents the Obstetric Record form, Used by HCP to create an
 * ObstetricRecord Object
 *
 *
 * @author Shukri Qubain (scqubain@ncsu.edu)
 *
 */
public class ObstetricsRecordForm {

    /** DeliveryMethod type for this record */
    private DeliveryMethod type;

    /** Long represents the date of the last menstrual cycle */
    private Long           lmp;

    /** Integer represents the year of conception */
    private int            conception;

    /** Integer represents the number of weeks the patient is pregnant */
    private int            weeksPreg;

    /** Intger represents the number of hours the patient was in labor */
    private int            hoursInLabor;

    /** Boolean represents whether or not the pregnancy resulted in twins */
    private boolean        twins;

    /**
     * Boolean represents whether or not the ObstetricRecord form is current or
     * previous pregnancy
     */
    private boolean        currentRecord;

    /** Long represents the id of this obstetric record form */
    private Long           id;

    /**
     * Empty default constructor
     */
    public ObstetricsRecordForm () {

    }

    /**
     * Parameterized Constructor
     *
     * @param lmp
     *            the last menstrual period
     * @param conception
     *            the year of conception
     * @param weeksPreg
     *            the number of weeks pregnant
     * @param hoursInLabor
     *            number of hours in labor
     * @param twins
     *            whether or not pregnancy resulted in twins
     * @param currentRecord
     *            whether or not this is a current record
     * @param id
     *            the id of the obsettrics record form
     */
    public ObstetricsRecordForm ( final DeliveryMethod type, final Long lmp, final int conception, final int weeksPreg,
            final int hoursInLabor, final boolean twins, final boolean currentRecord, final long id ) {
        this.setType( type );
        this.setConception( conception );
        this.setCurrentRecord( currentRecord );
        this.setHoursInLabor( hoursInLabor );
        this.setId( id );
        this.setLmp( lmp );
        this.setTwins( twins );
        this.setWeeksPreg( weeksPreg );
    }

    /**
     * Returns the delivery method type
     *
     * @return type the delivery method
     */
    public DeliveryMethod getType () {
        return type;
    }

    /**
     * Sets the delivery method type
     *
     * @param type
     *            the delivery method type
     */
    public void setType ( final DeliveryMethod type ) {
        this.type = type;
    }

    /**
     * Returns the lmp
     *
     * @return lmp the date of the last menstrual period
     */
    public Long getLmp () {
        return lmp;
    }

    /**
     * Sets the lmp
     *
     * @param lmp
     *            the date of the last menstrual period
     */
    public void setLmp ( final Long lmp ) {
        this.lmp = lmp;
    }

    /**
     * Return the conception
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
     * @return weeksPreg the number of weeks the patient was pregnant
     */
    public int getWeeksPreg () {
        return weeksPreg;
    }

    /**
     * Sets the weeksPreg
     *
     * @param weeksPreg
     *            the number of weeks the patient was pregnant
     */
    public void setWeeksPreg ( final int weeksPreg ) {
        this.weeksPreg = weeksPreg;
    }

    /**
     * Returns the hoursInLabor
     *
     * @return hoursInLabor the number of hours the patient was in labor
     */
    public int getHoursInLabor () {
        return hoursInLabor;
    }

    /**
     * Sets the hoursInLabor
     *
     * @param hoursInLabor
     *            the number of hours the patient was in labor
     */
    public void setHoursInLabor ( final int hoursInLabor ) {
        this.hoursInLabor = hoursInLabor;
    }

    /**
     * Returns the twins
     *
     * @return twins whether the pregnancy resulted in twins,
     */
    public boolean isTwins () {
        return twins;
    }

    /**
     * Sets the twins variable
     *
     * @param twins
     *            whether the pregnancy resulted in twins
     */
    public void setTwins ( final boolean twins ) {
        this.twins = twins;
    }

    /**
     * Returns currentRecord
     *
     * @return currentRecord whether this obstetric record form is current or
     *         previous
     */
    public boolean isCurrentRecord () {
        return currentRecord;
    }

    /**
     * Sets the currentRecord
     *
     * @param currentRecord
     *            whether this obstetric record form is current or previous
     */
    public void setCurrentRecord ( final boolean currentRecord ) {
        this.currentRecord = currentRecord;
    }

    /**
     * Returns the id
     *
     * @return id the id of the obstetrics record
     */
    public Long getId () {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id
     *            the id of the obsttrics record
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

}
