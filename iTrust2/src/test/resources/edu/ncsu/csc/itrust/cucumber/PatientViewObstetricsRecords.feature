Feature: Patient View Obstetrics Records
  As an Patient
  I want to view my past and current obstetrics records
  So that I can see the information regarding my pregnancies
  
  Scenario Outline: The patient does not have any existing pregnancies or a current pregnancy
    Given There exists an obstetrics patient in the system.
    Then I log on as a patient.
    When I navigate to the View Obstetrics Records page.
    Then <text> is displayed on the page. 
    Then I log out.

    Examples:
    | text          |
    | There are no current obstetrics records.  |
    | There are no previous obstetrics records. |
    
  Scenario Outline: View obstetrics record as a Patient who only has a current pregnancy. 
    Given There exists a patient in the system who can have an obstetrics record.
    Then I log in as an OB/GYN HCP.
    When I navigate to the document obstetrics record page.
    And I select a patient using her <username>.
    And I enter <LMP> for a current obstetrics record.
    And I click create obstetrics record.
    Then I log out.
    Then I log on as a patient.
    And I navigate to the View Obstetrics Records page.
    Then <text> is displayed on the page. 
    Then I can view the obstetrics record <lmp>, <dueDate>, <weeksPreg>.
    And I am still logged on the iTrust2 homepage. 

    Examples:
    | username       | LMP         | text                                       | lmp         | dueDate   | weeksPreg |
    | AliceThirteen  | 02/14/2017  | There are no previous obstetrics records.  | 02/14/2017  | 09/21/2017| 111       |
   

  Scenario Outline: View obstetrics record as a Patient who only has a past pregnancy. 
    Given There exists a patient in the system who can have an obstetrics record.
    Then I log in as an OB/GYN HCP.
    When I navigate to the document obstetrics record page.
    And I select a patient using her <username>.
    And I enter <previous_LMP>, <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins> for a previous obstetrics record.
    And I click add.
    Then I log out.
    Then I log on as a patient.
    And I navigate to the View Obstetrics Records page.
    Then I can view the previous obstetrics record <previous_LMP>, <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins>.
    Then text <text> is displayed on the page. 
    And I am still logged on the iTrust2 homepage. 

    Examples: 
    | username      | previous_LMP | conception_year | weeks_pregnant| hours_in_labor | delivery_method | twins | text                                      |
    | AliceThirteen | 11/26/2015   | 2015            | 30            | 15             | Cesarean        | false | There are no current obstetrics records.  |

  Scenario Outline: View obstetrics record as a Patient who has current and past pregnancies.
    Given There exists a patient in the system who can have an obstetrics record.
    Then I log in as an OB/GYN HCP.
    When I navigate to the document obstetrics record page.
    And I select a patient using her <username>.
    And I enter <LMP> for a current obstetrics record.
    And I click create obstetrics record.
    And I enter <previous_LMP>, <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins> for a previous obstetrics record.
    And I click add.
    Then I log out.
    Then I log on as a patient.
    And I navigate to the View Obstetrics Records page.
    Then I can view the obstetrics record <LMP>, <dueDate>, <weeksPreg>.
    Then I can view both previous and current obstetrics records <previous_LMP>, <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins>.
    And I am still logged on the iTrust2 homepage.

    Examples:
    | username       | LMP         | dueDate    | weeksPreg    | previous_LMP | conception_year | weeks_pregnant| hours_in_labor | delivery_method | twins |
    | AliceThirteen  | 02/14/2017  | 09/21/2017 |   111        | 11/26/2015   | 2015            | 30            | 15             | Cesarean        | false |
    
    
    