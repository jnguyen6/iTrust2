package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.criterion.Criterion;

import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryReportForm;

/**
 * LaborDeliveryReport Class.
 *
 * @author srazdan
 *
 */
@Entity
@Table ( name = "LaborDeliveryReport" )
public class LaborDeliveryReport extends DomainObject<LaborDeliveryReport> {

    private LocalDate        dateOfLabor;
    private String           timeOfLabor;
    private LocalDate        dateOfDelivery;
    private String           timeOfDelivery;
    private Double           weight;
    private Double           length;
    private Integer          heartRate;
    private Integer          bloodPressure;
    private String           firstName;
    private String           lastName;
    private LocalDate        secondDateOfDelivery;
    private String           secondTimeOfDelivery;
    private Double           secondWeight;
    private Double           secondLength;
    private Integer          secondHeartRate;
    private Integer          secondBloodPressure;
    private String           secondFirstName;
    private String           secondLastName;
    private ObstetricsRecord obstetricsRecord;

    /** The id of this obstetrics record */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long             id;

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
        setSecondDateOfDelivery( ov.getSecondDateOfDelivery() );
        setSecondTimeOfDelivery( ov.getSecondTimeOfDelivery() );
        setSecondWeight( ov.getSecondWeight() );
        setSecondLength( ov.getSecondLength() );
        setSecondHeartRate( ov.getSecondHeartRate() );
        setSecondBloodPressure( ov.getSecondBloodPressure() );
        setSecondFirstName( ov.getSecondFirstName() );
        setSecondLastName( ov.getSecondLastName() );
        setObstetricsRecord( ov.getObstetricsRecord() );
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
     * returns the Id of the labor delivery report
     *
     * @return id of the labor delivery report
     */
    @Override
    public Serializable getId () {
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
        this.obstetricsRecord = obstetricsRecord;
    }
}
