/**
 *
 */
package edu.ncsu.csc.itrust2.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class for Rest API endpoints needed for UC24/ObstetricRecordInitialization
 *
 *
 * @author Shukri Qubain (scqubain@ncsu.edu)
 *
 */
public class APIObstetricsRecordController extends APIController {

    /**
     * Creates a new obstetrics record object and saves it to the DB
     *
     * @param form
     *            the form being used to create a ObstetricRecord object
     * @return a response containing results of creating a new entry
     */
    @SuppressWarnings ( { "unchecked", "rawtypes" } )
    @PreAuthorize ( "hasRole('ROLE_OBGYN')" )
    @PostMapping ( BASE_PATH + "/obstetricsRecord" )
    public ResponseEntity createRecord ( @RequestBody final ObstetricsRecordForm form ) {
        try {

            final ObstetricsRecord dEntry = new ObstetricsRecord( form );
            dEntry.setPatient( LoggerUtil.currentUser() );
            dEntry.save();

            final Patient person = Patient.getByName( LoggerUtil.currentUser() );
            // check if the patient is female
            if ( person.getGender().toString().equals( "Male" ) ) {
                LoggerUtil.log( TransactionType.CREATE_NEW_OBSTETRICS_RECORD, LoggerUtil.currentUser(),
                        "Can't make record for a male patient" );
                return new ResponseEntity( errorResponse( "Could not create Obstetrics Record because "
                        + person.getFirstName().toString() + " is a male patient" ), HttpStatus.BAD_REQUEST );
            }

            LoggerUtil.log( TransactionType.CREATE_NEW_OBSTETRICS_RECORD, LoggerUtil.currentUser() );
            return new ResponseEntity( dEntry, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not create Obstetrics Record provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Edits an ObstetricsRecord object and saves it to the database.
     *
     * @param id
     *            the database id for this pre-existing entry
     *
     * @param form
     *            the form used to edit an ObstetricsRecord object
     *
     * @return a response containing the results of editing an existing entry
     *
     */
    @SuppressWarnings ( { "unchecked", "rawtypes" } )
    @PreAuthorize ( "hasRole('ROLE_OBGYN')" )
    @PutMapping ( BASE_PATH + "obstetricsRecord/{id}" )
    public ResponseEntity editRecord ( @PathVariable final long id, @RequestBody final ObstetricsRecordForm form ) {
        try {
            final ObstetricsRecord current = new ObstetricsRecord( form );
            final ObstetricsRecord saved = ObstetricsRecord.getById( id );

            if ( saved == null ) {
                LoggerUtil.log( TransactionType.EDIT_OBSTETRICS_RECORD, LoggerUtil.currentUser(),
                        "No record found with id " + id );
                return new ResponseEntity( errorResponse( "No record found with id " + id ), HttpStatus.NOT_FOUND );
            }

            current.setId( id );
            current.setPatient( LoggerUtil.currentUser() );
            current.save();
            LoggerUtil.log( TransactionType.EDIT_OBSTETRICS_RECORD, current.getPatient(),
                    "Edited obstetrics record with id " + id );
            return new ResponseEntity( current, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            LoggerUtil.log( TransactionType.EDIT_OBSTETRICS_RECORD, LoggerUtil.currentUser(),
                    "Failed to edit obstetrics record" );
            return new ResponseEntity( errorResponse( "Failed to edit obstetrics record " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Retrieves a list of patient Obstetrics records, either for the current
     * patient if the user has role PATIENT
     *
     * @return a list of patient's obstetrics records
     */
    @SuppressWarnings ( { "unchecked", "rawtypes" } )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @GetMapping ( BASE_PATH + "obstetricsRecord" )
    public ResponseEntity getRecordsPatient () {
        LoggerUtil.log( TransactionType.PATIENT_VIEW_OBSTETRICS_RECORD, LoggerUtil.currentUser() );
        return new ResponseEntity( ObstetricsRecord.getByPatient( LoggerUtil.currentUser() ), HttpStatus.OK );
    }

    /**
     * Retrieves a list of patient ObstetricsRecords, either for HCP to view
     * patient records, or for HCP OBGYN to view patient records.
     *
     * @return a list of patient's obstetrics records
     *
     * @param patient
     *            the username of the patient for which to get records
     */
    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @PreAuthorize ( "hasRole('ROLE_HCP','ROLE_OBGYN' )" )
    @GetMapping ( BASE_PATH + "obstetricsRecord/{patient}" )
    public ResponseEntity getRecordsHCP ( @PathVariable final String patient ) {
        if ( null == Patient.getByName( patient ) ) {
            return new ResponseEntity( errorResponse( "No patients found with username " + patient ),
                    HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.HCP_VIEW_OBSTETRICS_RECORD, User.getByName( LoggerUtil.currentUser() ),
                User.getByName( patient ) );
        return new ResponseEntity( ObstetricsRecord.getByPatient( patient ), HttpStatus.OK );
    }
}
