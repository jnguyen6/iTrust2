Feature: Patient Views Labor and Delivery Reports
    As an Patient
    I want to view my labor and delivery reports
    So that I can see the information regarding my labor and delivery report records
    
Scenario Outline: The patient does not have a labor and delivery report
    Given there exists a patient in the iTrust2 system
    Then I log on as a patient.
    When I navigate to the View Obstetrics Records page.
    Then <text> is displayed on the page. 
    
Examples:
    | text                                      |
    | You have no labor and delivery reports.   |
    
Scenario Outline: The patient has a labor and delivery report to view without twins
    Given there exists a patient in the iTrust2 system
	And the patient has an existing labor and delivery report without twins
	Then the patient logs in and navigates to the View Patient Labor and Delivery Report page
	Then the patient select the date of the report <dateDelivery>
	Then the patient's labor and delivery report is displayed
	
Examples:
    | dateDelivery |
    | 03/22/2019   |
    
Scenario Outline: The patient has a labor and delivery report to view with twins
    Given there exists a patient in the iTrust2 system
	And the patient has an existing labor and delivery report with twins
	Then the patient logs in and navigates to the View Patient Labor and Delivery Report page
	Then the patient select the date of the report <dateDelivery>
	Then the patient's labor and delivery report of twins is displayed
	
Examples:
    | dateDelivery |
    | 03/22/2019   |