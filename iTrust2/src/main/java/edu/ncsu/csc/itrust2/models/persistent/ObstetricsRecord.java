package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.itrust2.adapters.LocalDateAdapter;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;

/**
 * Class representing the Obstetric Record for a patient
 *
 * @author Shukri Qubain (scqubain@ncsu.edu)
 * @author Jimmy Nguyen (jnguyen6)
 *
 */
@Entity
@Table ( name = "ObstetricsRecord" )
public class ObstetricsRecord extends DomainObject<ObstetricsRecord> implements Serializable {

    /** randomly generated long value */
    private static final long serialVersionUID = -4051404539990169870L;

    /**
     * The date as milliseconds since epoch of this DiaryEntry
     */
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate         lmp;
    // private Long lmp;

    /** integer representing the year of conception */
    private int               conception;

    /** integer representing the number of weeks pregnant */
    private int               weeksPreg;

    /** integer representing the number of hours in labor */
    private int               hoursInLabor;

    @Enumerated ( EnumType.STRING )
    private DeliveryMethod    type;

    /** Whether or not the pregnancy for this record resulted in twins */
    private boolean           twins;

    /**
     * Whether or not this obstetrics record is a current or previous pregnancy
     */
    private boolean           currentRecord;

    /**
     * The username of the patient for this ObstetricsRecord
     */
    @Length ( max = 20 )
    private String            patient;

    /** The id of this obstetrics record */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long              id;

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
        this.setLmp( LocalDate.parse( form.getLmp() ) );
        this.setCurrentRecord( form.isCurrentRecord() );
        // If the record is a current record, initialize the rest
        // of the fields to default values
        if ( isCurrentRecord() ) {
            this.setConception( 2000 );
            this.setWeeksPreg( 0 );
            this.setHoursInLabor( 0 );
            this.setDeliveryMethod( DeliveryMethod.Miscarriage );
            this.setTwins( false );
        }
        else {
            this.setConception( form.getConception() );
            this.setWeeksPreg( form.getWeeksPreg() );
            this.setHoursInLabor( form.getHoursInLabor() );
            this.setDeliveryMethod( form.getType() );
            this.setTwins( form.isTwins() );
        }
        // this.setLmp( form.getLmp() );
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
    public LocalDate getLmp () {
        return lmp;
    }

    /**
     * Sets the lmp
     *
     * @param lmp
     *            the last menstrual period
     * @throws IllegalArgumentException
     *             if the given lmp is after the current date
     */
    public void setLmp ( final LocalDate lmp ) {
        if ( lmp.isAfter( LocalDate.now() ) ) {
            throw new IllegalArgumentException( "The LMP must be before the current date" );
        }
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
     * @throws IllegalArgumentException
     *             if the year of conception is being set for current obstetrics
     *             record or the year of conception is not a four-digit integer
     */
    public void setConception ( final int conception ) {
        if ( conception < 0 ) {
            throw new IllegalArgumentException( "The year of conception must be a nonnegative integer" );
        }
        if ( String.valueOf( conception ).length() != 4 ) {
            throw new IllegalArgumentException( "The year of conception must be four digits" );
        }
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
     * @throws IllegalArgumentException
     *             if the number of weeks pregnant is a negative integer
     */
    public void setWeeksPreg ( final int weeksPreg ) {
        if ( weeksPreg < 0 ) {
            throw new IllegalArgumentException( "The number of weeks pregnant must be a nonnegative integer" );
        }
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
     * @throws IllegalArgumentException
     *             if the number of hours in labor is a negative integer
     */
    public void setHoursInLabor ( final int hoursInLabor ) {
        if ( hoursInLabor < 0 ) {
            throw new IllegalArgumentException( "The number of hours in labor must be a nonnegative integer" );
        }
        this.hoursInLabor = hoursInLabor;
    }

    /**
     * Returns the delivery method
     *
     * @return the delivery method
     */
    public DeliveryMethod getDeliveryMethod () {
        return type;
    }

    /**
     * Sets the delivery method
     *
     * @param type
     *            the delivery method to set
     * @throws NullPointerException
     *             if the given delivery method is null
     */
    public void setDeliveryMethod ( final DeliveryMethod type ) {
        if ( type == null ) {
            throw new IllegalArgumentException( "The delivery method cannot be null" );
        }
        this.type = type;
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
     * Returns the patient for this ObstetricsRecord
     *
     * @return the patient for this obstetrics record
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Initializes the patient for this ObstetricsRecord
     *
     * @param patient
     *            the patient to set for this obstetrics record
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Returns the id of the obstetrics record
     *
     * @return id the id of the obstetrics record
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the id of the obstetrics record
     *
     * @param id
     *            the id of the obstetrics record
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

}
