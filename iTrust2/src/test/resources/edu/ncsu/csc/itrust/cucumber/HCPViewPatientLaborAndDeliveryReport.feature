Feature: HCP View Patient Labor and Delivery Reports
	As an HCP
	I want to view patients' labor and delivery reports
	So that I can see their labor and delivery report records
	
Scenario Outline: Patient has no labor and delivery report
	Given There exists an obstetrics patient in the system
	Given We have added an HCP into the system.
	Then I log in as an HCP and navigate to the View Patient Labor and Delivery Report page.
	Then Unselected patient text is displayed when no patient is selected.
	And I select to view <username>'s labor and delivery reports.
	Then No current report text is displayed when the patient is selected.
	
Examples:
	| username		|
	| AliceThirteen |
	
Scenario Outline: Patient has a labor and delivery report
	Given There exists an obstetrics patient in the system
	Given We have added an HCP into the system.
	When As an OB/GYN HCP I add a current obstetrics record for <username> with LMP <lmp>.
	Then I document a labor and delivery report for a patient.
    Then I log in as an HCP and navigate to the View Patient Labor and Delivery Report page.
	And I select to view <username>'s labor and delivery reports.
	And I select the date of the report <dateDelivery>.
	Then The patient's labor and delivery report is displayed when the patient is selected
	
Examples:
	| username	    | lmp		    | dateDelivery   |
	| AliceThirteen	| 11/01/2018	| 03/22/2019     |
	
