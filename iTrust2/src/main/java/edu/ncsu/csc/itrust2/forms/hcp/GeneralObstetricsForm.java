package edu.ncsu.csc.itrust2.forms.hcp;

import edu.ncsu.csc.itrust2.models.persistent.GeneralObstetrics;

/**
 * GeneralObstetricsForm. The actual concrete form for the obstetrics office
 * visit.
 * 
 * @author srazdan
 *
 */
public class GeneralObstetricsForm extends ObstetricsOfficeVisitForm {

    /**
     * For Hibernate/Thymeleaf
     */
    public GeneralObstetricsForm () {
    }

    /**
     * Creates a form object out of the given appointment
     *
     * @param ov
     *            OfficeVisit to create form out of
     */
    public GeneralObstetricsForm ( final GeneralObstetrics ov ) {
        super( ov );
    }

}
