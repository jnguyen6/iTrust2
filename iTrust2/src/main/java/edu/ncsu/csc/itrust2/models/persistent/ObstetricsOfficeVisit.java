package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;

import org.hibernate.criterion.Criterion;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsOfficeVisitForm;

/**
 * Abstracts any type of obstetrics appointment
 *
 * @author Sanchit Razdan
 */
@MappedSuperclass
public class ObstetricsOfficeVisit extends OfficeVisit {

    @Min ( 0 )
    private Integer            weeksPregnant;

    @Min ( 0 )
    private Integer            fetalHeartRate;

    @Min ( 0 )
    private Double             fundalHeight;
    private Boolean            isTwins;
    private Boolean            isLowLyingPlacenta;
    private BasicHealthMetrics mothersHealthMetric;

    /**
     * For Hibernate/Thymeleaf
     */
    public ObstetricsOfficeVisit () {
    }

    /**
     * Creates a visit from the provided form
     *
     * @param ov
     *            Visit form to create the appointment from
     */
    public ObstetricsOfficeVisit ( final ObstetricsOfficeVisitForm ov ) throws ParseException {
        super( ov );
        setWeeksPregnant( ov.getWeeksPregnant() );
        setFetalHeartRate( ov.getFetalHeartRate() );
        setFundalHeight( ov.getFundalHeight() );
        setIsTwins( ov.getIsTwins() );
        setIsLowLyingPlacenta( ov.getIsLowLyingPlacenta() );
        setMothersHealthMetric( ov.getMothersHealthMetric() );
    }

    /**
     * Get a list of Obstetric Office Visits by patient.
     *
     * @param patient
     *            the username of the patient whose entries are being searched
     *            for
     * @return a list of Obstetric Office Visits for the given patient
     */
    @SuppressWarnings ( "unchecked" )
    public static List<ObstetricsOfficeVisit> getByPatient ( final String patient ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );

        return (List<ObstetricsOfficeVisit>) getWhere( ObstetricsOfficeVisit.class, criteria );
    }

    /**
     * Gets the weeks pregnant number for the patient
     *
     * @return weeks pregnant number for the patient
     */
    public Integer getWeeksPregnant () {
        return weeksPregnant;
    }

    /**
     * Sets the weeks pregnant for the patient
     *
     * @param weeksPregnant
     *            weeks pregnant for the patient
     */
    public void setWeeksPregnant ( final Integer weeksPregnant ) {
        if ( weeksPregnant < 0 ) {
            throw new IllegalArgumentException( "The number of weeks pregnant must be a nonnegative integer" );
        }
        this.weeksPregnant = weeksPregnant;
    }

    /**
     * Gets the fetal heart rate of the patient
     *
     * @return fetal heart rate of the patient
     */
    public Integer getFetalHeartRate () {
        return fetalHeartRate;
    }

    /**
     * Sets the fetal heart rate of the patient
     *
     * @param fetalHeartRate
     *            fetal heart rate of the patient
     */
    public void setFetalHeartRate ( final Integer fetalHeartRate ) {
        if ( fetalHeartRate < 0 ) {
            throw new IllegalArgumentException( "The fetal heart rate must be a nonnegative integer" );
        }
        this.fetalHeartRate = fetalHeartRate;
    }

    /**
     * Gets the fundal height of the child
     *
     * @return fundal height of the child
     */
    public Double getFundalHeight () {
        return fundalHeight;
    }

    /**
     * Sets the fundal height of the patient
     *
     * @param fundalHeight
     *            fundal height of the patient
     */
    public void setFundalHeight ( final Double fundalHeight ) {
        if ( fundalHeight < 0 ) {
            throw new IllegalArgumentException( "The fundal height must be a nonnegative number" );
        }
        this.fundalHeight = fundalHeight;
    }

    /**
     * Gets the boolean of whether the patient is giving birth to twins or not
     *
     * @return boolean of whether the patient is giving birth to twins or not
     */
    public Boolean getIsTwins () {
        return isTwins;
    }

    /**
     * Sets the isTwins boolean
     *
     * @param isTwins
     *            whether the patient is giving birth to twins or not
     */
    public void setIsTwins ( final Boolean isTwins ) {
        this.isTwins = isTwins;
    }

    /**
     * Gets the boolean of whether the patient has a low lying placenta
     *
     * @return whether the patient has a low lying placenta
     */
    public Boolean getIsLowLyingPlacenta () {
        return isLowLyingPlacenta;
    }

    /**
     * Sets the isLowLyingPlacenta
     *
     * @param isLowLyingPlacenta
     *            whether the patient has a low lying placenta
     */
    public void setIsLowLyingPlacenta ( final Boolean isLowLyingPlacenta ) {
        this.isLowLyingPlacenta = isLowLyingPlacenta;
    }

    /**
     * Gets the basic health metrics of the patient
     *
     * @return basic health metrics of the patient
     */
    public BasicHealthMetrics getMothersHealthMetric () {
        return mothersHealthMetric;
    }

    /**
     * Sets the basic health metric of the patient
     *
     * @param mothersHealthMetric
     *            basic health metric of the patient
     */
    public void setMothersHealthMetric ( final BasicHealthMetrics mothersHealthMetric ) {
        if ( mothersHealthMetric == null ) {
            throw new IllegalArgumentException( "The basic " );
        }
        this.mothersHealthMetric = mothersHealthMetric;
    }
    
    /**
     * Get a specific office visit by the database ID
     *
     * @param id
     *            the database ID
     * @return the specific office visit with the desired ID
     */
    public static ObstetricsOfficeVisit getById ( final Long id ) {
        return (ObstetricsOfficeVisit) OfficeVisit.getById( id );
    }

}
