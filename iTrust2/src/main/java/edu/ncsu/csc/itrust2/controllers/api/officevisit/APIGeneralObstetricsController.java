package edu.ncsu.csc.itrust2.controllers.api.officevisit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.controllers.api.APIController;
import edu.ncsu.csc.itrust2.forms.hcp.GeneralObstetricsForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.GeneralObstetrics;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides REST API endpoints for the ObstetricsOfficeVisit model.
 * In all requests made to this controller, the {id} provided is a Long that is
 * the primary key id of the office visit requested.
 *
 * @author Madhura Waghmare
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIGeneralObstetricsController extends APIController {

    /**
     * Retrieves the Obstetrics Office Visit specified by the id provided.
     *
     * @param id
     *            The (numeric) ID of the OfficeVisit desired
     * @return response
     */
    @GetMapping ( BASE_PATH + "/generalobstetrics/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_PATIENT', 'ROLE_OBGYN')" )
    public ResponseEntity getObstetricsOfficeVisit ( @PathVariable ( "id" ) final Long id ) {
        final GeneralObstetrics visit = GeneralObstetrics.getById( id );
        if ( null == visit ) {
            return new ResponseEntity( errorResponse( "No office visit found for id " + id ), HttpStatus.NOT_FOUND );
        }
        else {
            final User self = User.getByName( LoggerUtil.currentUser() );
            if ( null != self && self.isDoctor() ) {
                LoggerUtil.log( TransactionType.GENERAL_OBSTETRICS_PATIENT_VIEW, LoggerUtil.currentUser(),
                        visit.getPatient().getUsername() );
            }
            else {
                LoggerUtil.log( TransactionType.GENERAL_OBSTETRICS_PATIENT_VIEW, LoggerUtil.currentUser() );
            }
            return new ResponseEntity( visit, HttpStatus.OK );
        }
    }

    /**
     * Creates and saves a new Obstetrics Office Visit from the RequestBody
     * provided.
     *
     * @param visitForm
     *            The office visit to be validated and saved
     * @return response
     */

    @PostMapping ( BASE_PATH + "/generalobstetrics" )
    @PreAuthorize ( "hasAnyRole('ROLE_OBGYN')" )
    public ResponseEntity createObstetricsOfficeVisits ( @RequestBody final GeneralObstetricsForm visitForm ) {
        try {
            final GeneralObstetrics visit = new GeneralObstetrics( visitForm );

            if ( null != GeneralObstetrics.getById( visit.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "Office visit with the id " + visit.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            visit.save();
            LoggerUtil.log( TransactionType.GENERAL_OBSTETRICS_CREATE, LoggerUtil.currentUser(),
                    visit.getPatient().getUsername() );
            return new ResponseEntity( visit, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the OfficeVisit provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Updates the OfficeVisit with the id provided by overwriting it with the
     * new OfficeVisit that is provided. If the ID provided does not match the
     * ID set in the OfficeVisit provided, the update will not take place
     *
     * @param id
     *            The ID of the OfficeVisit to be updated
     * @param form
     *            The updated OfficeVisit to save
     * @return response
     */
    @PutMapping ( BASE_PATH + "/generalobstetrics/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_OBGYN')" )
    public ResponseEntity editObstetricsOfficeVisits ( @PathVariable final Long id,
            @RequestBody final GeneralObstetricsForm form ) {
        try {
            final GeneralObstetrics visit = new GeneralObstetrics( form );
            if ( null != visit.getId() && !id.equals( visit.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "The ID provided does not match the ID of the OfficeVisit provided" ),
                        HttpStatus.CONFLICT );
            }
            final GeneralObstetrics oVisit = GeneralObstetrics.getById( id );
            if ( null == oVisit ) {
                return new ResponseEntity( errorResponse( "No visit found for name " + id ), HttpStatus.NOT_FOUND );
            }
            // It is possible that the HCP did not update the BHM but only the
            // other fields (date, time, etc) thus we need to check if the old
            // BHM is different from the new BHM before logging
            if ( !oVisit.getBasicHealthMetrics().equals( visit.getBasicHealthMetrics() ) ) {
                LoggerUtil.log( TransactionType.GENERAL_OBSTETRICS_EDIT, form.getHcp(), form.getPatient(), form.getHcp()
                        + " updated basic health metrics for " + form.getPatient() + " from " + form.getDate() );
            }
            visit.save(); /* Will overwrite existing request */
            LoggerUtil.log( TransactionType.GENERAL_OBSTETRICS_EDIT, LoggerUtil.currentUser() );
            return new ResponseEntity( visit, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update " + form.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * This is used as a marker for the system to know that the HCP has viewed
     * the visit
     *
     * @param id
     *            The id of the office visit being viewed
     * @param form
     *            The office visit being viewed
     * @return OK if the office visit is found, NOT_FOUND otherwise
     */
    @PostMapping ( BASE_PATH + "/generalobstetrics/hcp/view/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_OBGYN')" )
    public ResponseEntity viewGeneralObstetrics ( @PathVariable final Long id,
            @RequestBody final GeneralObstetricsForm form ) {
        final GeneralObstetrics dbVisit = GeneralObstetrics.getById( id );
        if ( null == dbVisit ) {
            return new ResponseEntity( errorResponse( "No visit found for name " + id ), HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.GENERAL_OBSTETRICS_HCP_VIEW, form.getHcp(), form.getPatient(),
                form.getHcp() + " viewed basic health metrics for " + form.getPatient() + " from " + form.getDate() );
        return new ResponseEntity( HttpStatus.OK );
    }

    /**
     * This is used as a marker for the system to know that the patient has
     * viewed the visit
     *
     * @param id
     *            The id of the office visit being viewed
     * @param form
     *            The office visit being viewed
     * @return OK if the office visit is found, NOT_FOUND otherwise
     */
    @PostMapping ( BASE_PATH + "/generalobstetrics/patient/view/{id}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity viewGeneralObstetricsPatient ( @PathVariable final Long id,
            @RequestBody final GeneralObstetricsForm form ) {
        final GeneralObstetrics dbVisit = GeneralObstetrics.getById( id );
        if ( null == dbVisit ) {
            return new ResponseEntity( errorResponse( "No visit found for name " + id ), HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.GENERAL_OBSTETRICS_PATIENT_VIEW, form.getHcp(), form.getPatient(),
                form.getPatient() + " viewed their basic health metrics from " + form.getDate() );
        return new ResponseEntity( HttpStatus.OK );
    }
}
