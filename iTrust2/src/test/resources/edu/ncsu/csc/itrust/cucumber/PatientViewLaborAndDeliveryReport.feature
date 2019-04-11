Feature: Patient Views Labor and Delivery Reports
    As an Patient
    I want to view my labor and delivery reports
    So that I can see the information regarding my labor and delivery report records
    
Scenario Outline: The patient does not have a labor and delivery report
    Given There exists a patient in the system who is eligible for an obstetrics record.
    Then I log on as a patient.
    When I navigate to the View Obstetrics Records page.
    Then <text> is displayed on the page. 
    
Examples:
    | text                                      |
    | You have no labor and delivery reports.   |
    
Scenario Outline: The patient has a labor and delivery report to view
    Given There exists a patient in the system who is eligible for an obstetrics record.
    Then I log in as an OB/GYN HCP to document an obstetrics record for a patient.
    When I navigate to the document obstetrics record page to document an obstetrics record for a patient.
    And I select radio button with text <username> to select a patient.
    And I enter <LMP> for a current obstetrics record for the patient.
    And I click create obstetrics record to document an obstetrics record for a patient.
    Then I document a labor and delivery report for a patient.
    Then I log out as OB/GYN HCP.
    Then I log on as an obstetrics patient.
    When I navigate to the View Obstetrics Labor and Delivery Report page.
    Then I select the patient's <username> and the radio button with text <dateDelivery>.
	Then The patient's labor and delivery report is displayed
    
Examples:
	| username	    | LMP		    | dateDelivery   |
	| AliceThirteen	| 11/01/2018	| 03/22/2019     |