package edu.ncsu.csc.itrust2.forms.hcp;

import java.time.ZonedDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.persistent.LaborDeliveryReport;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;

/**
 * LaborDeliveryReportForm Class
 *
 * @author srazdan
 *
 */
public class LaborDeliveryReportForm {

    /** Date representing the date in labor */
    private String           datetimeOfLabor;

    /** Date representing the date of delivery of baby */
    private String           datetimeOfDelivery;

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
    private String           secondDatetimeOfDelivery;

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
     * The delivery method of the labor delivery report of the second baby, if
     * twins are born
     */
    private DeliveryMethod   secondDeliveryMethod;

    /**
     * Default Constructor for the Labor Delivery Report Form
     */
    public LaborDeliveryReportForm () {
    }

    /**
     * Creates the form from the given labor delivery report
     *
     * @param ov
     *            labor delivery report to create form from
     */
    public LaborDeliveryReportForm ( final LaborDeliveryReport ov ) {
        setDatetimeOfLabor( ov.getDatetimeOfLabor().toString() );
        setDatetimeOfDelivery( ov.getDatetimeOfDelivery().toString() );
        setWeight( ov.getWeight() );
        setLength( ov.getLength() );
        setHeartRate( ov.getHeartRate() );
        setBloodPressure( ov.getBloodPressure() );
        setFirstName( ov.getFirstName() );
        setLastName( ov.getLastName() );
        setDeliveryMethod( ov.getDeliveryMethod() );
        setObstetricsRecord( ov.getObstetricsRecord() );
        if ( ov.getObstetricsRecord().isTwins() ) {
            setSecondDatetimeOfDelivery( ov.getSecondDatetimeOfDelivery().toString() );
            setSecondWeight( ov.getSecondWeight() );
            setSecondLength( ov.getSecondLength() );
            setSecondHeartRate( ov.getSecondHeartRate() );
            setSecondBloodPressure( ov.getSecondBloodPressure() );
            setSecondFirstName( ov.getSecondFirstName() );
            setSecondLastName( ov.getSecondLastName() );
            setSecondDeliveryMethod( ov.getSecondDeliveryMethod() );
        }
        else {
            setSecondDatetimeOfDelivery( ZonedDateTime.now().toString() );
            setSecondWeight( 20.9 );
            setSecondLength( 1.3 );
            setSecondHeartRate( 160 );
            setSecondBloodPressure( 70 );
            setSecondFirstName( "Bob" );
            setSecondLastName( "Marley" );
            setSecondDeliveryMethod( DeliveryMethod.Miscarriage );
        }
    }

    /**
     * returns the date time of labor
     *
     * @return date time of labor
     */
    public String getDatetimeOfLabor () {
        return datetimeOfLabor;
    }

    /**
     * sets the date and time of labor
     *
     * @param datetimeOfLabor
     *            date and time of labor to be set
     */
    public void setDatetimeOfLabor ( final String datetimeOfLabor ) {
        this.datetimeOfLabor = datetimeOfLabor;
    }

    /**
     * returns the date and time of delivery
     *
     * @return date and time of delivery
     */
    public String getDatetimeOfDelivery () {
        return datetimeOfDelivery;
    }

    /**
     * sets the date and time of delivery
     *
     * @param datetimeOfDelivery
     *            date and time of delivery to be set
     */
    public void setDatetimeOfDelivery ( final String datetimeOfDelivery ) {
        this.datetimeOfDelivery = datetimeOfDelivery;
    }

    /**
     * returns the date and time of the second delivery
     *
     * @return date and time of second delivery
     */
    public String getSecondDatetimeOfDelivery () {
        return secondDatetimeOfDelivery;
    }

    /**
     * sets the date and time of the second delivery
     *
     * @param secondDatetimeOfDelivery
     *            date and time of the second delivery
     */
    public void setSecondDatetimeOfDelivery ( final String secondDatetimeOfDelivery ) {
        this.secondDatetimeOfDelivery = secondDatetimeOfDelivery;
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
        this.lastName = lastName;
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
        this.secondLastName = secondLastName;
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
        this.obstetricsRecord = obstetricsRecord;
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

    /**
     * Returns the delivery method for this labor delivery report
     *
     * @return the patient for this labor delivery report
     */
    public DeliveryMethod getSecondDeliveryMethod () {
        return deliveryMethod;
    }

    /**
     * Sets the delivery method for the labor delivery report
     *
     * @param secondDeliveryMethod
     *            the deliveryMethod to set for this labor delivery report
     */
    public void setSecondDeliveryMethod ( final DeliveryMethod secondDeliveryMethod ) {
        this.secondDeliveryMethod = secondDeliveryMethod;
    }

}
