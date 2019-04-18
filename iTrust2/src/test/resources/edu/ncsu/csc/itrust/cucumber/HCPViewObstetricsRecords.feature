Feature: View obstetrics patient records
    As a non-OB/GYN HCP, I want to be able to view current and previous obstetrics patient records.
    
  
Scenario Outline: Patient has no current or previous obstetrics records.
	Given A female patient exists in the system.
	Given We have added an HCP into the system.
	Then I log in as an HCP and navigate to the View Obstetrics Records page.
	Then Unselected patient text is displayed when no patient is selected.
	And I select to view <username>'s obstetrics records.
	Then No current pregnancies text is displayed when the patient is selected and has no current pregnancies.
	And No previous pregnancies text is displayed when the patient is selected and has no previous pregnancies.
	
Examples:
	| username		|
	| AliceThirteen |
	| AliceThirteen	|
	
	
Scenario Outline: Patient has a current but no previous obstetrics records.
	Given A female patient exists in the system.
	Given We have added an HCP into the system.
	When As an OB/GYN HCP I add a current obstetrics record for <username> with LMP <lmp>.
	Then I log in as an HCP and navigate to the View Obstetrics Records page.
	And I select to view <username>'s obstetrics records.
	Then The patient's correct LMP <lmp>, Estimated Due Date, and Weeks Pregnant are displayed.
	And The other user <username2> still has no records.
	
Examples:
	| username	    | username2     | lmp			|
	| AliceThirteen	|	JillBob		|	11/01/2018	|
	| JillBob       | AliceThirteen	| 04/03/2019	|
	
	
Scenario Outline: Patient has a previous but no current obstetrics records.
	Given A female patient exists in the system.
	Given We have added an HCP into the system.
	When As an OB/GYN HCP I add a previous obstetrics record for <username> with conception year <conception_year>, <weeks_pregnant> weeks pregnant, <hours_in_labor> hours in labor, <delivery_method>, twins: <twins> for a previous obstetrics record.
	Then I log in as an HCP and navigate to the View Obstetrics Records page.
	And I select to view <username>'s obstetrics records.
	Then The patient's conception year <conception_year>, <weeks_pregnant> weeks pregnant, <hours_in_labor> hours in labor, <delivery_method>, twins: <twins> for a previous obstetrics record are displayed for record <recordNo>.
	And The other user <username2> still has no records.
	
Examples:
	| username			| username2			| conception_year | weeks_pregnant| hours_in_labor | delivery_method | twins | recordNo	|
	| AliceThirteen	|	JillBob				| 2015            | 36            | 10             | Vaginal         | true  | 0				|
	| JillBob				| AliceThirteen	| 2015            | 30            | 15             | Cesarean        | false | 0				|
	
	
Scenario Outline: Patient has both current and previous obstetrics records.
	Given A female patient exists in the system.
	Given We have added an HCP into the system.
	When As an OB/GYN HCP I add a current obstetrics record for <username> with LMP <lmp>.
	When As an OB/GYN HCP I add a previous obstetrics record for <username> with conception year <conception_year>, <weeks_pregnant> weeks pregnant, <hours_in_labor> hours in labor, <delivery_method>, twins: <twins> for a previous obstetrics record.
	Then I log in as an HCP and navigate to the View Obstetrics Records page.
	And I select to view <username>'s obstetrics records.
	Then The patient's correct LMP <lmp>, Estimated Due Date, and Weeks Pregnant are displayed.
	Then The patient's conception year <conception_year>, <weeks_pregnant> weeks pregnant, <hours_in_labor> hours in labor, <delivery_method>, twins: <twins> for a previous obstetrics record are displayed for record <recordNo>.
	And The other user <username2> still has no records.
	
Examples:
	| username			| username2			| lmp					| conception_year | weeks_pregnant| hours_in_labor | delivery_method | twins | recordNo	|
	| AliceThirteen	|	JillBob				| 11/01/2018	| 2015            | 36            | 10             | Vaginal         | true  | 1				|
	| JillBob				| AliceThirteen	| 04/03/2019	| 2015            | 30            | 15             | Cesarean        | false | 1				|
	
	