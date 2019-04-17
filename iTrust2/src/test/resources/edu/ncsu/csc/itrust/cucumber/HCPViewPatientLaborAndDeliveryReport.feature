Feature: HCP View Patient Labor and Delivery Reports
	As an HCP
	I want to view patients' labor and delivery reports
	So that I can see their labor and delivery report records
	
Scenario Outline: Patient has no labor and delivery report
	Given There exists an obstetrics patient in the system
	And There exists an <hcp_type> HCP in the iTrust2 system
	Then the <hcp_type> HCP logs in and navigates to the view patient labor and delivery report page
	Then Unselected patient text is displayed when no patient is selected
	Then I select to view <username>'s labor and delivery reports
	Then No current report text is displayed when the patient is selected
	
Examples:
	| username		| hcp_type         |
	| AliceThirteen | general          |
	| AliceThirteen | ophthalmologist  |
    | AliceThirteen | optometrist      |
    | AliceThirteen | obstetrics       |
    
	
Scenario Outline: Patient has a labor and delivery report without twins
	Given There exists an obstetrics patient in the system
	And The obstetrics patient has a current obstetrics record in the iTrust2 system
	And The patient has an existing labor and delivery report without twins
	And There exists an <hcp_type> HCP in the iTrust2 system
    Then I log in as an <hcp_type> HCP and navigate to the View Patient Labor and Delivery Report page
	And I select to view <username>'s labor and delivery reports and the date of the report <dateDelivery>
	Then The patient's labor and delivery report is displayed when the patient is selected
	
Examples:
    | username		| hcp_type         | dateDelivery |
	| AliceThirteen | general          | 03/22/2019   |
	| AliceThirteen | ophthalmologist  | 03/22/2019   |
    | AliceThirteen | optometrist      | 03/22/2019   |
    | AliceThirteen | obstetrics       | 03/22/2019   |
	
Scenario Outline: Patient has a labor and delivery report with twins
	Given There exists an obstetrics patient in the system
	And The obstetrics patient has a current obstetrics record in the iTrust2 system
	And The patient has an existing labor and delivery report with twins
	And There exists an <hcp_type> HCP in the iTrust2 system
    Then I log in as an HCP and navigate to the View Patient Labor and Delivery Report page
	And I select to view <username>'s labor and delivery reports and the date of the report <dateDelivery>
	Then The patient's labor and delivery report is displayed when the patient is selected
	
Examples:
    | username		| hcp_type         | lmp         | dateDelivery |
	| AliceThirteen | general          | 11/01/2018  | 03/22/2019   |
	| AliceThirteen | ophthalmologist  | 11/01/2018  | 03/22/2019   |
    | AliceThirteen | optometrist      | 11/01/2018  | 03/22/2019   |
    | AliceThirteen | obstetrics       | 11/01/2018  | 03/22/2019   |