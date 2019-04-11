package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;

import javax.persistence.Entity;
import javax.persistence.Table;

import edu.ncsu.csc.itrust2.forms.hcp.GeneralObstetricsForm;

/**
 * The general obstetrics class
 *
 * @author Sanchit Razdan
 */
@Entity
@Table ( name = "GeneralObstetrics" )
public class GeneralObstetrics extends ObstetricsOfficeVisit {

    /**
     * Empty constructor for Hibernate/Thymeleaf
     */
    public GeneralObstetrics () {
    }

    /**
     * Creates a GeneralObstetrics from the given form.
     *
     * @param visitF
     *            Form to create appointment from
     * @throws ParseException
     *             If there is a problem with the form
     */
    public GeneralObstetrics ( final GeneralObstetricsForm visitF ) throws ParseException {
        super( visitF );
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
