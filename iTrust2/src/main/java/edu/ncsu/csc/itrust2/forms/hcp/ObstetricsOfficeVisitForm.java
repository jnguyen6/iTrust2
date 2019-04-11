package edu.ncsu.csc.itrust2.forms.hcp;

import javax.validation.constraints.Min;

import edu.ncsu.csc.itrust2.models.persistent.ObstetricsOfficeVisit;

/**
 * The abstraction of the form a doctor fills out for any obstetrics appointment
 *
 * @author Sanchit Razdan
 */
public abstract class ObstetricsOfficeVisitForm extends OfficeVisitForm {

    @Min ( 0 )
    private Integer weeksPregnant;

    @Min ( 0 )
    private Integer fetalHeartRate;

    @Min ( 0 )
    private Double  fundalHeight;
    private Boolean isTwins;
    private Boolean isLowLyingPlacenta;

    /** For Hibernate */
    public ObstetricsOfficeVisitForm () {
    }

    /**
     * Creates the form from the given office visit
     *
     * @param ov
     *            office visit to create form from
     */
    public ObstetricsOfficeVisitForm ( final ObstetricsOfficeVisit ov ) {
        super( ov );
        setWeeksPregnant( ov.getWeeksPregnant() );
        setFetalHeartRate( ov.getFetalHeartRate() );
        setFundalHeight( ov.getFundalHeight() );
        setIsTwins( ov.getIsTwins() );
        setIsLowLyingPlacenta( ov.getIsLowLyingPlacenta() );
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
}
