package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.hcp.GeneralObstetricsForm;
import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryReportForm;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.GeneralObstetrics;
import edu.ncsu.csc.itrust2.models.persistent.LaborDeliveryReport;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class for Rest API endpoints needed for UC26/LaborAndDeliveryReport :)
 *
 *
 * @author Madhura Waghmare (mswaghma@ncsu.edu)
 *
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
@RestController
public class APILaborDeliveryReportController extends APIController {
	
	/**
     * Creates a new labor and delivery report object and saves it to the DB
     *
     * @param patient
     *            the patient to create the labor and delivery report for
     * @param form
     *            the form being used to create a ObstetricRecord object
     * @return a response containing results of creating a new entry
     */
    @PreAuthorize ( "hasRole('ROLE_OBGYN')" )
    @PostMapping ( BASE_PATH + "laborDeliveryReports/{patient}" )
    public ResponseEntity createLaborDeliveryReport ( @PathVariable final String patient,
            @RequestBody final LaborDeliveryReportForm form ) {
        try {
        	
        	final LaborDeliveryReport report = new LaborDeliveryReport(form);
        	final Patient person = Patient.getByName( patient );
            // check if the patient is female
            if ( person.getGender().toString().equals( "Male" ) ) {
                return new ResponseEntity(
                        errorResponse( "Could not create Labor and Delivery Report because " + patient + " is a male patient" ),
                        HttpStatus.BAD_REQUEST );
            }
        	
            report.setPatient(patient);
            report.save();

            LoggerUtil.log( TransactionType.LABOR_DELIVERY_REPORT_CREATE, LoggerUtil.currentUser() );
            return new ResponseEntity( report, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not create Labor and Delivery Report provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }
	
    /**
     * Edits an Labor and Delivery Report object and saves it to the database.
     *
     * @param id
     *            the database id for this pre-existing entry
     *
     * @param form
     *            the form used to edit a Labor and Delivery Report object
     *
     * @return a response containing the results of editing an existing entry
     *
     */
    @PreAuthorize ( "hasRole('ROLE_OBGYN')" )
    @PutMapping ( BASE_PATH + "LaborDeliveryReports/{id}" )
    public ResponseEntity editLaborDeliveryReport ( @PathVariable final long id, @RequestBody final LaborDeliveryReportForm form ) {
        try {
            final LaborDeliveryReport current = new LaborDeliveryReport( form );
            final LaborDeliveryReport saved = LaborDeliveryReport.getById( id );

            if ( saved == null ) {
                return new ResponseEntity( errorResponse( "No report found with id " + id ), HttpStatus.NOT_FOUND );
            }

            current.setId( id );
            current.setPatient( saved.getPatient() );
            current.save();
            LoggerUtil.log( TransactionType.LABOR_DELIVERY_REPORT_EDIT, current.getPatient(),
                    "Edited labor and deleivery report with id " + id );
            return new ResponseEntity( current, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to edit labor and deleivery report " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }
    
    /**
     * Retrieves a list of patient Labor and Delivery reports, either for the current
     * patient if the user has role PATIENT
     *
     * @return a list of patient's labor and delivery reports
     */
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @GetMapping ( BASE_PATH + "laborDeliveryReports" )
    public ResponseEntity getLaborDeliveryReportsPatient () {
        LoggerUtil.log( TransactionType.LABOR_DELIVERY_REPORT_PATIENT_VIEW, LoggerUtil.currentUser() );
        return new ResponseEntity( LaborDeliveryReport.getByPatient( LoggerUtil.currentUser() ), HttpStatus.OK );
    }

    /**
     * Retrieves a list of patient Labor and Delivery reports, either for HCP to view
     * patient reports, or for HCP OBGYN to view patient reports.
     *
     * @return a list of patient's labor and delivery reports
     *
     * @param patient
     *            the username of the patient for which to get reports
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP','ROLE_OBGYN' )" )
    @GetMapping ( BASE_PATH + "laborDeliveryReports/{patient}" ) 
    public ResponseEntity getLaborDeliveryReportsHCP ( @PathVariable final String patient ) {
        if ( null == Patient.getByName( patient ) ) {
            return new ResponseEntity( errorResponse( "No patients found with username " + patient ),
                    HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.LABOR_DELIVERY_REPORT_HCP_VIEW, User.getByName( LoggerUtil.currentUser() ),
                User.getByName( patient ) );
        return new ResponseEntity( LaborDeliveryReport.getByPatient( patient ), HttpStatus.OK );
    }
    
   

}
