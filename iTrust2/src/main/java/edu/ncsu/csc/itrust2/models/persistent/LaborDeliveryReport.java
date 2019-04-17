package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryReportForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;

/**
 * LaborDeliveryReport Class.
 *
 * @author srazdan
 *
 */
@Entity
@Table ( name = "LaborDeliveryReport" )
public class LaborDeliveryReport extends DomainObject<LaborDeliveryReport> {

    /** Date representing the date in labor */
    private LocalDate        dateOfLabor;

    /** String representing the time in labor */
    private String           timeOfLabor;

    /** Date representing the date of delivery of baby */
    private LocalDate        dateOfDelivery;

    /** String representing the time of delivery of baby */
    private String           timeOfDelivery;

    /** Double representing the weight of the baby */
    private Double           weight;

    /** Double representing the length of the baby */
    private Double           length;

    /** Integer representing the heart rate of the baby */
    private Integer          heartRate;

    /** Integer representing the blood pressure of the baby */
    private Integer          bloodPressure;

    /** String representing the first name of the baby */
    private String           firstName;

    /** String representing the last name of the baby */
    private String           lastName;

    /**
     * Date representing the date of the delivery of the second baby, if twins
     * are born
     */
    private LocalDate        secondDateOfDelivery;

    /**
     * String representing the time of the delivery of the second baby, if twins
     * are born
     */
    private String           secondTimeOfDelivery;

    /** Double representing the weight of the second baby, if twins are born */
    private Double           secondWeight;

    /** Double representing the length of the second baby, if twins are born */
    private Double           secondLength;

    /**
     * Integer representing the heart rate of the second baby, if twins are born
     */
    private Integer          secondHeartRate;

    /**
     * Integer representing the blood pressure of the second baby, if twins are
     * born
     */
    private Integer          secondBloodPressure;

    /**
     * String representing the first name of the second baby, if twins are born
     */
    private String           secondFirstName;

    /**
     * String representing the last name of the second baby, if twins are born
     */
    private String           secondLastName;

    /**
     * Obstetrics Record representing the Obstetrics Record
     */
    @OneToOne
    @JoinColumn ( name = "obstetricsrecord_id" )
    private ObstetricsRecord obstetricsRecord;

    /** The id of this obstetrics record */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long             id;

    /**
     * The username of the patient for this ObstetricsRecord
     */
    @Length ( max = 20 )
    private String           patient;

    /**
     * The delivery method of the labor delivery report
     */
    private DeliveryMethod   deliveryMethod;

    /**
     * Default Constructor for LaborDeliveryReport
     */
    public LaborDeliveryReport () {
    }

    /**
     * Creates the form from the given labor delivery report
     *
     * @param ov
     *            labor delivery report to create form from
     */
    public LaborDeliveryReport ( final LaborDeliveryReportForm ov ) throws ParseException {
        setDateOfLabor( ov.getDateOfLabor() );
        setTimeOfLabor( ov.getTimeOfLabor() );
        setDateOfDelivery( ov.getDateOfDelivery() );
        setTimeOfDelivery( ov.getTimeOfDelivery() );
        setWeight( ov.getWeight() );
        setLength( ov.getLength() );
        setHeartRate( ov.getHeartRate() );
        setBloodPressure( ov.getBloodPressure() );
        setFirstName( ov.getFirstName() );
        setLastName( ov.getLastName() );
        setObstetricsRecord( ov.getObstetricsRecord() );
        setDeliveryMethod( ov.getDeliveryMethod() );
        obstetricsRecord.setDeliveryMethod( ov.getDeliveryMethod() );
        obstetricsRecord.setCurrentRecord( false );
        if ( obstetricsRecord.isTwins() ) {
            setSecondDateOfDelivery( ov.getSecondDateOfDelivery() );
            setSecondTimeOfDelivery( ov.getSecondTimeOfDelivery() );
            setSecondWeight( ov.getSecondWeight() );
            setSecondLength( ov.getSecondLength() );
            setSecondHeartRate( ov.getSecondHeartRate() );
            setSecondBloodPressure( ov.getSecondBloodPressure() );
            setSecondFirstName( ov.getSecondFirstName() );
            setSecondLastName( ov.getSecondLastName() );
        }
        else {
            setSecondDateOfDelivery( LocalDate.MIN );
            setSecondTimeOfDelivery( "12:00" );
            setSecondWeight( 20.9 );
            setSecondLength( 1.3 );
            setSecondHeartRate( 160 );
            setSecondBloodPressure( 70 );
            setSecondFirstName( "Bob" );
            setSecondLastName( "Marley" );
        }

    }

    /**
     * Get a list of Labor Delivery Reports by patient.
     *
     * @param patient
     *            the username of the patient whose entries are being searched
     *            for
     * @return a list of LaborDeliveryReports for the given patient
     */
    @SuppressWarnings ( "unchecked" )
    public static List<LaborDeliveryReport> getByPatient ( final String patient ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );

        return (List<LaborDeliveryReport>) getWhere( LaborDeliveryReport.class, criteria );
    }

    /**
     * Get a specific labor delivery report by the database ID
     *
     * @param id
     *            the database ID
     * @return the specific labor delivery report with the desired ID
     */
    public static LaborDeliveryReport getById ( final Long id ) {
        try {
            return (LaborDeliveryReport) getWhere( LaborDeliveryReport.class, eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * returns the date of labor
     *
     * @return dateOfLabor
     */
    public LocalDate getDateOfLabor () {
        return dateOfLabor;
    }

    /**
     * sets the date of labor
     *
     * @param dateOfLabor
     *            date of labor to be set for the labor delivery report
     */
    public void setDateOfLabor ( final LocalDate dateOfLabor ) {
        if ( dateOfLabor.isAfter( LocalDate.now() ) ) {
            throw new IllegalArgumentException( "The date of labor must be before the current date" );
        }
        this.dateOfLabor = dateOfLabor;
    }

    /**
     * returns the time of labor
     *
     * @return timeOfLabor
     */
    public String getTimeOfLabor () {
        return timeOfLabor;
    }

    /**
     * sets the time of the labor
     *
     * @param timeOfLabor
     *            time of labor to be set for the labor delivery report
     */
    public void setTimeOfLabor ( final String timeOfLabor ) {
        if ( timeOfLabor == null ) {
            throw new IllegalArgumentException( "The time of labor cannot be null" );
        }
        this.timeOfLabor = timeOfLabor;
    }

    /**
     * returns the date of delivery of baby
     *
     * @return dateOfDelivery
     */
    public LocalDate getDateOfDelivery () {
        return dateOfDelivery;
    }

    /**
     * sets the date of the delivery for the baby
     *
     * @param dateOfDelivery
     *            date of delivery to be set for the baby.
     */
    public void setDateOfDelivery ( final LocalDate dateOfDelivery ) {
        if ( dateOfDelivery.isAfter( LocalDate.now() ) ) {
            throw new IllegalArgumentException( "The date of delivery must be before the current date" );
        }
        this.dateOfDelivery = dateOfDelivery;
    }

    /**
     * returns the time of delivery of baby
     *
     * @return timeOfDelivery
     */
    public String getTimeOfDelivery () {
        return timeOfDelivery;
    }

    /**
     * sets the time of delivery for the baby
     *
     * @param timeOfDelivery
     *            time of delivery to be set for the baby
     */
    public void setTimeOfDelivery ( final String timeOfDelivery ) {
        if ( timeOfDelivery == null ) {
            throw new IllegalArgumentException( "The time of delivery cannot be null" );
        }
        this.timeOfDelivery = timeOfDelivery;
    }

    /**
     * returns the weight of the baby
     *
     * @return weight
     */
    public Double getWeight () {
        return weight;
    }

    /**
     * sets the weight of the baby
     *
     * @param weight
     *            weight to be set for the baby
     */
    public void setWeight ( final Double weight ) {
        if ( weight < 0 ) {
            throw new IllegalArgumentException( "The weight must be a nonnegative double" );
        }
        this.weight = weight;
    }

    /**
     * returns the length of the baby
     *
     * @return length
     */
    public Double getLength () {
        return length;
    }

    /**
     * sets the length of the baby
     *
     * @param length
     *            length to be set for the baby
     */
    public void setLength ( final Double length ) {
        if ( length < 0 ) {
            throw new IllegalArgumentException( "The length must be a nonnegative double" );
        }
        this.length = length;
    }

    /**
     * returns the heart rate of the baby
     *
     * @return heartRate
     */
    public Integer getHeartRate () {
        return heartRate;
    }

    /**
     * sets the heart rate for the baby
     *
     * @param heartRate
     *            heart rate to be set for the baby
     */
    public void setHeartRate ( final Integer heartRate ) {
        if ( heartRate < 0 ) {
            throw new IllegalArgumentException( "The heart rate must be a nonnegative integer" );
        }
        this.heartRate = heartRate;
    }

    /**
     * returns the blood pressure of the baby
     *
     * @return bloodPressure
     */
    public Integer getBloodPressure () {
        return bloodPressure;
    }

    /**
     * sets the blood pressure for the baby
     *
     * @param bloodPressure
     *            blood pressure to be set for the baby
     */
    public void setBloodPressure ( final Integer bloodPressure ) {
        if ( bloodPressure < 0 ) {
            throw new IllegalArgumentException( "The blood pressure must be a nonnegative integer" );
        }
        this.bloodPressure = bloodPressure;
    }

    /**
     * returns the first name of the baby
     *
     * @return firstName
     */
    public String getFirstName () {
        return firstName;
    }

    /**
     * sets the first name of the baby
     *
     * @param firstName
     *            first name to be set for the baby
     */
    public void setFirstName ( final String firstName ) {
        if ( firstName == null ) {
            throw new IllegalArgumentException( "The first name cannot be null" );
        }
        this.firstName = firstName;
    }

    /**
     * returns the last name of the baby
     *
     * @return lastName
     */
    public String getLastName () {
        return lastName;
    }

    /**
     * sets the last name of the baby
     *
     * @param lastName
     *            last name to be set for the baby
     */
    public void setLastName ( final String lastName ) {
        if ( lastName == null || lastName.equals( "" ) ) {
            final String username = obstetricsRecord.getPatient();
            this.lastName = Patient.getByName( username ).getLastName();
        }
        else {
            this.lastName = lastName;
        }
    }

    /**
     * returns the date of delivery of the second baby, if twins are born
     *
     * @return secondDateOfDelivery
     */
    public LocalDate getSecondDateOfDelivery () {
        return secondDateOfDelivery;
    }

    /**
     * sets the date of delivery for the second baby, if twins are born
     *
     * @param secondDateOfDelivery
     *            date of delivery for the second baby to be set, if twins are
     *            born
     */
    public void setSecondDateOfDelivery ( final LocalDate secondDateOfDelivery ) {
        if ( secondDateOfDelivery.isAfter( LocalDate.now() ) ) {
            throw new IllegalArgumentException( "The date of delivery must be before the current date" );
        }
        this.secondDateOfDelivery = secondDateOfDelivery;
    }

    /**
     * returns the time of delivery of the second baby, if twins are born
     *
     * @return secondTimeOfDelivery
     */
    public String getSecondTimeOfDelivery () {
        return secondTimeOfDelivery;
    }

    /**
     * sets the time of delivery for the second baby, if twins are born
     *
     * @param secondTimeOfDelivery
     *            time of delivery for the second baby to be set, if twins are
     *            born
     */
    public void setSecondTimeOfDelivery ( final String secondTimeOfDelivery ) {
        if ( secondTimeOfDelivery == null ) {
            throw new IllegalArgumentException( "The time of delivery cannot be null" );
        }
        this.secondTimeOfDelivery = secondTimeOfDelivery;
    }

    /**
     * returns the weight of the second baby, if twins are born
     *
     * @return secondWeight
     */
    public Double getSecondWeight () {
        return secondWeight;
    }

    /**
     * sets the weight of the second baby, if twins are born
     *
     * @param secondWeight
     *            weight of the second baby to be set, if twins are born
     */
    public void setSecondWeight ( final Double secondWeight ) {
        if ( secondWeight < 0 ) {
            throw new IllegalArgumentException( "The weight cannot be a nonnegative double" );
        }
        this.secondWeight = secondWeight;
    }

    /**
     * returns the length of the second baby, if twins are born
     *
     * @return secondLength
     */
    public Double getSecondLength () {
        return secondLength;
    }

    /**
     * sets the length of the second baby, if twins are born
     *
     * @param secondLength
     *            length of the seconed baby to be set, if twins are born
     */
    public void setSecondLength ( final Double secondLength ) {
        if ( secondLength < 0 ) {
            throw new IllegalArgumentException( "The length cannot be a nonnegative double" );
        }
        this.secondLength = secondLength;
    }

    /**
     * returns the heart rate of the second baby, if twins are born
     *
     * @return secondHeartRate
     */
    public Integer getSecondHeartRate () {
        return secondHeartRate;
    }

    /**
     * sets the heart rate of the second baby, if twins are born
     *
     * @param secondHeartRate
     *            heart rate of the second baby, if twins are born
     */
    public void setSecondHeartRate ( final Integer secondHeartRate ) {
        if ( secondHeartRate < 0 ) {
            throw new IllegalArgumentException( "The heart rate cannot be a nonnegative intger" );
        }
        this.secondHeartRate = secondHeartRate;
    }

    /**
     * returns the blood pressure of the second baby, if twins are born
     *
     * @return secondBloodPressure
     */
    public Integer getSecondBloodPressure () {
        return secondBloodPressure;
    }

    /**
     * sets the blood pressure for the second baby, it twins are born
     *
     * @param secondBloodPressure
     *            blood pressure for the second baby, if twins are born
     */
    public void setSecondBloodPressure ( final Integer secondBloodPressure ) {
        if ( secondBloodPressure < 0 ) {
            throw new IllegalArgumentException( "The blood pressure cannot be a nonnegative integer" );
        }
        this.secondBloodPressure = secondBloodPressure;
    }

    /**
     * returns the first name of the second baby, if twins are born
     *
     * @return secondFirstName
     */
    public String getSecondFirstName () {
        return secondFirstName;
    }

    /**
     * sets the first name of the second baby, if twins are born
     *
     * @param secondFirstName
     *            first name to be set for the second baby, if twins are born
     */
    public void setSecondFirstName ( final String secondFirstName ) {
        if ( secondFirstName == null ) {
            throw new IllegalArgumentException( "The first name cannot be null" );
        }
        this.secondFirstName = secondFirstName;
    }

    /**
     * returns the last name of the second baby, if twins are born
     *
     * @return secondLastName
     */
    public String getSecondLastName () {
        return secondLastName;
    }

    /**
     * sets the last name for the second baby, if twins are born
     *
     * @param secondLastName
     *            last name of the second baby to be set, if twins are born
     */
    public void setSecondLastName ( final String secondLastName ) {
        if ( secondLastName == null || secondLastName.equals( "" ) ) {
            final String username = obstetricsRecord.getPatient();
            this.secondLastName = Patient.getByName( username ).getLastName();
        }
        else {
            this.secondLastName = secondLastName;
        }
    }

    /**
     * returns the Id of the labor delivery report
     *
     * @return id of the labor delivery report
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the id of the labor delivery report
     *
     * @param id
     *            the id of the labor delivery report
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * returns the Obstetrics Record corresponding to the Labor Delivery Report
     *
     * @return obstetricsRecord corresponding to the Labor Delivery Report
     */
    public ObstetricsRecord getObstetricsRecord () {
        return obstetricsRecord;
    }

    /**
     * Sets the obstetrics record of the labor delivery report
     *
     * @param obstetricsRecord
     *            the obstetrics record of the labor delivery report
     */
    public void setObstetricsRecord ( final ObstetricsRecord obstetricsRecord ) {
        if ( obstetricsRecord == null ) {
            throw new IllegalArgumentException( "The obstetrics record cannot be null" );
        }
        this.obstetricsRecord = obstetricsRecord;
    }

    /**
     * Returns the patient for this labor delivery report
     *
     * @return the patient for this labor delivery report
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Initializes the patient for this labor delivery report
     *
     * @param patient
     *            the patient to set for this labor delivery report
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Returns the delivery method for this labor delivery report
     *
     * @return the patient for this labor delivery report
     */
    public DeliveryMethod getDeliveryMethod () {
        return deliveryMethod;
    }

    /**
     * Sets the delivery method for the labor delivery report
     *
     * @param deliveryMethod
     *            the deliveryMethod to set for this labor delivery report
     */
    public void setDeliveryMethod ( final DeliveryMethod deliveryMethod ) {
        this.deliveryMethod = deliveryMethod;
    }
}
