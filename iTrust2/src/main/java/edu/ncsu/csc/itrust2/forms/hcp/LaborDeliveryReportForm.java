package edu.ncsu.csc.itrust2.forms.hcp;

import java.time.LocalDate;

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
    private ObstetricsRecord obstetricsRecord;

    /**
     * The delivery method of the labor delivery report
     */
    private DeliveryMethod   deliveryMethod;

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
        setDeliveryMethod( ov.getDeliveryMethod() );
        setObstetricsRecord( ov.getObstetricsRecord() );
        obstetricsRecord.setDeliveryMethod( ov.getDeliveryMethod() );
        obstetricsRecord.setCurrentRecord( false );
        if ( ov.getObstetricsRecord().isTwins() ) {
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

}
