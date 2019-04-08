Feature: Add obstetrics patient records and view obstetrics patient records
    As an OB/GYN HCP, I want to be able to add new and old obstetrics patient records
    And view those obstetrics patient records
    
Scenario Outline: Invalid current pregnancy data.
	Given There exists a patient in the system who can have an obstetrics record.
	Then I log in as an OB/GYN HCP.
	When I navigate to the document obstetrics record page.
	And I select a patient using her <username>.
	And I enter <LMP> for a current obstetrics record, where the LMP is invalid.
	And I click create obstetrics record.
	Then the obstetrics record is not made. 
	
Examples:
	| username       | LMP         |
	| AliceThirteen  | 11/01/2019  |
	| AliceThirteen  | 02/14/2020  | 
    
Scenario Outline: Creating a current pregnancy for a patient with no current or previous records.
	Given There exists a patient in the system who can have an obstetrics record.
	Then I log in as an OB/GYN HCP.
	When I navigate to the document obstetrics record page.
	And I select a patient using her <username>.
	And I enter <LMP> for a current obstetrics record.
	And I click create obstetrics record.
	Then the patient with this <username> has her current pregnancy is updated.
	
Examples:
	| username       | LMP         |
	| AliceThirteen  | 11/01/2018  |
	| AliceThirteen  | 02/14/2017  | 
	
Scenario Outline: Creating a current pregnancy for a patient with previous records.
	Given There exists a patient in the system who can have an obstetrics record.
	Then I log in as an OB/GYN HCP.
	When I navigate to the document obstetrics record page.
	And I select a patient using her <username>.
	And I enter <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins> for a previous obstetrics record.
	And I click add.
	And I enter <current_LMP> for a current obstetrics record.
	And I click create obstetrics record.
	Then the patient with this <username> has her current pregnancy is updated.
	
Examples:  
	| username      | current_LMP         | conception_year | weeks_pregnant| hours_in_labor | delivery_method | twins |
 	| AliceThirteen | 11/01/2018          | 2015            | 36            | 10             | Vaginal         | true  |
 	| AliceThirteen | 12/15/2017          | 2015            | 30            | 15             | Cesarean        | false |
 	
Scenario Outline: Creating a previous pregnancy for a patient with current pregnancy.
	Given There exists a patient in the system who can have an obstetrics record.
	Then I log in as an OB/GYN HCP.
	When I navigate to the document obstetrics record page.
	And I select a patient using her <username>.
	And I enter <current_LMP> for a current obstetrics record.
	And I click create obstetrics record.
	And I enter <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins> for a previous obstetrics record.
	And I click add.
	Then the patient with this <username> has her current pregnancy is updated.
	
Examples:  
	| username      | current_LMP         | conception_year | weeks_pregnant| hours_in_labor | delivery_method | twins |
 	| AliceThirteen | 11/01/2018          | 2015            | 36            | 10             | Vaginal         | true  |
 	| AliceThirteen | 12/15/2017          | 2015            | 30            | 15             | Cesarean        | false |
	
Scenario Outline: Invalid previous pregnancy data.
	Given There exists a patient in the system who can have an obstetrics record.
	Then I log in as an OB/GYN HCP.
	When I navigate to the document obstetrics record page.
	And I select a patient using her <username>.
	And I enter <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method> and <twins> for a previous obstetrics record, where one input is incorrect.
	Then the obstetrics record is not made. 
	
Examples:
	| username      | conception_year | weeks_pregnant| hours_in_labor | delivery_method | twins |
 	| AliceThirteen | 2015            | 36            | 10             | Vaginal         | true  |
 	| AliceThirteen | 2015            | 30            | 15             | Cesarean        | false |
 	| AliceThirteen | -1              | 36            | 10             | Vaginal         | true  |
 	| AliceThirteen | null            | 30            | 15             | Cesarean        | false |
  	| AliceThirteen | 2015            | 36            | 10             | Vaginal         | true  |
 	| AliceThirteen | 2015            | 30            | 15             | Cesarean        | false |
 	| AliceThirteen | 2015            | -1            | 10             | Vaginal         | true  |
 	| AliceThirteen | 2015            | null          | 15             | Cesarean        | false |
    | AliceThirteen | 2015            | 36            | -1             | Vaginal         | true  |
 	| AliceThirteen | 2015            | 36            | null           | Cesarean        | false |
