Feature: HCP View Patient Labor and Delivery Reports
	As an HCP
	I want to view patients' labor and delivery reports
	So that I can see their labor and delivery report records
	
Scenario Outline: Patient has no labor and delivery report
	Given there exists an obstetrics patient in the system
	And there exists an <hcp_type> HCP in the iTrust2 system
	Then I log in as an <hcp_type> HCP and navigate to the View Patient Labor and Delivery Report page
	Then unselected patient text is displayed when no patient is selected
	Then I select to view <username>'s labor and delivery reports
	Then no current report text is displayed when the patient is selected
	
Examples:
	| username		| hcp_type         |
	| AliceThirteen | general          |
	| AliceThirteen | ophthalmologist  |
    | AliceThirteen | optometrist      |
    
	
Scenario Outline: Patient has a labor and delivery report without twins
	Given there exists an obstetrics patient in the system
	And The obstetrics patient has a current obstetrics record in the iTrust2 system
	And The patient does have an existing labor and delivery report without twins
	And there exists an <hcp_type> HCP in the iTrust2 system
    Then I log in as an <hcp_type> HCP and navigate to the View Patient Labor and Delivery Report page
	And I select to view <username>'s labor and delivery reports and the date of the report <dateDelivery>
	Then The patient's labor and delivery report is displayed when the patient is selected
	
Examples:
    | username		| hcp_type         | dateDelivery |
	| AliceThirteen | general          | 03/22/2019   |
	| AliceThirteen | ophthalmologist  | 03/22/2019   |
    | AliceThirteen | optometrist      | 03/22/2019   |
	
Scenario Outline: Patient has a labor and delivery report with twins
	Given there exists an obstetrics patient in the system
	And The obstetrics patient has a current obstetrics record in the iTrust2 system
	And The patient has an existing labor and delivery report with twins
	And there exists an <hcp_type> HCP in the iTrust2 system
    Then I log in as an <hcp_type> HCP and navigate to the View Patient Labor and Delivery Report page
	And I select to view <username>'s labor and delivery reports and the date of the report <dateDelivery>
	Then The patient's labor and delivery report is displayed with twins when the patient is selected
	
Examples:
    | username		| hcp_type         | dateDelivery |
	| AliceThirteen | general          | 03/22/2019   |
	| AliceThirteen | ophthalmologist  | 03/22/2019   |
    | AliceThirteen | optometrist      | 03/22/2019   |