package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;

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
        this.mothersHealthMetric = mothersHealthMetric;
    }

}
