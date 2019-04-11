package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.criterion.Criterion;

import edu.ncsu.csc.itrust2.forms.hcp.GeneralObstetricsForm;

/**
 * Abstracts any type of obstetrics appointment
 *
 * @author Sanchit Razdan
 */
@Entity
@Table ( name = "ObstetricsOfficeVisit" )
public class GeneralObstetrics extends OfficeVisit {

    @Min ( 0 )
    private Integer weeksPregnant;

    @Min ( 0 )
    private Integer fetalHeartRate;

    @Min ( 0 )
    private Double  fundalHeight;
    private Boolean isTwins;
    private Boolean isLowLyingPlacenta;

    /**
     * For Hibernate/Thymeleaf
     */
    public GeneralObstetrics () {
    }

    /**
     * Creates a visit from the provided form
     *
     * @param ov
     *            Visit form to create the appointment from
     */
    public GeneralObstetrics ( final GeneralObstetricsForm ov ) throws ParseException {
        super( ov );
        setWeeksPregnant( ov.getWeeksPregnant() );
        setFetalHeartRate( ov.getFetalHeartRate() );
        setFundalHeight( ov.getFundalHeight() );
        setIsTwins( ov.getIsTwins() );
        setIsLowLyingPlacenta( ov.getIsLowLyingPlacenta() );
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
    public static List<GeneralObstetrics> getByPatient ( final String patient ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );

        return (List<GeneralObstetrics>) getWhere( GeneralObstetrics.class, criteria );
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
     * Get a specific office visit by the database ID
     *
     * @param id
     *            the database ID
     * @return the specific office visit with the desired ID
     */
    public static GeneralObstetrics getById ( final Long id ) {
        return (GeneralObstetrics) OfficeVisit.getById( id );
    }

}
