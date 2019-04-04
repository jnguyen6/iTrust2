Feature: Patient View Obstetrics Records
  As an Patient
  I want to view my past and current obstetrics records
  So that I can see the information regarding my pregnancies
  
  Scenario Outline: The patient does not have any existing pregnancies or a current pregnancy
    Given There exists a patient in the system who is eligible for an obstetrics record.
    Then I log on as a patient.
    When I navigate to the View Obstetrics Records page.
    Then <text> is displayed on the page. 

    Examples:
    | text          |
    | There are no current obstetrics records.  |
    | There are no previous obstetrics records. |
    
  Scenario Outline: View obstetrics record as a Patient who only has a current pregnancy. 
    Given There exists a patient in the system who is eligible for an obstetrics record.
    Then I log in as an OB/GYN HCP to document an obstetrics record for a patient.
    When I navigate to the document obstetrics record page to document an obstetrics record for a patient.
    And I select radio button with text <username> to select a patient.
    And I enter <LMP> for a current obstetrics record for the patient.
    And I click create obstetrics record to document an obstetrics record for a patient.
    Then I log out as OB/GYN HCP.
    Then I log on as a patient.
    When I navigate to the View Obstetrics Records page.
    Then I can view the obstetrics record <lmp>, <dueDate>, <weeksPreg>.
    Then <text> is displayed on the page. 

    Examples:
    | username       | LMP         | text                                       | lmp         | dueDate   | weeksPreg |
    | JillBob        | 02/14/2017  | There are no previous obstetrics records.  | 02/14/2017  | 09/21/2017| 111       |

  Scenario Outline: View obstetrics record as a Patient who only has a past pregnancy. 
    Given There exists a patient in the system who is eligible for an obstetrics record.
    Then I log in as an OB/GYN HCP to document an obstetrics record for a patient.
    When I navigate to the document obstetrics record page to document an obstetrics record for a patient.
    And I select radio button with text <username> to select a patient.
    And I enter <previous_LMP>, <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins> for a previous obstetrics record to document an obstetrics record for a patient.
    And I click add to document an obstetrics record for a patient.
    Then I log out as OB/GYN HCP.
    Then I log on as a patient.
    When I navigate to the View Obstetrics Records page.
    Then I can view the previous obstetrics record <previous_LMP>, <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins>.
    Then <text> is displayed on the page.  

    Examples: 
    | username      | previous_LMP | conception_year | weeks_pregnant| hours_in_labor | delivery_method | twins | text                                      |
    | JillBob       | 11/26/2015   | 2015            | 30            | 15             | Cesarean        | false | There are no current obstetrics records.  |

  Scenario Outline: View obstetrics record as a Patient who has current and past pregnancies.
    Given There exists a patient in the system who is eligible for an obstetrics record.
    Then I log in as an OB/GYN HCP to document an obstetrics record for a patient.
    When I navigate to the document obstetrics record page to document an obstetrics record for a patient.
    And I select radio button with text <username> to select a patient.
    And I enter <LMP> for a current obstetrics record for the patient.
    And I click create obstetrics record to document an obstetrics record for a patient.
    And I enter <previous_LMP>, <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins> for a previous obstetrics record to document an obstetrics record for a patient.
    And I click add to document an obstetrics record for a patient.
    Then I log out as OB/GYN HCP.
    Then I log on as a patient.
    When I navigate to the View Obstetrics Records page.
    Then I can view the obstetrics record <LMP>, <dueDate>, <weeksPreg>.
    Then I can view the previous obstetrics record <previous_LMP>, <conception_year>, <weeks_pregnant>, <hours_in_labor>, <delivery_method>, <twins>.

    Examples:
    | username       | LMP         | dueDate    | weeksPreg    | previous_LMP | conception_year | weeks_pregnant| hours_in_labor | delivery_method | twins |
    | JillBob        | 02/14/2017  | 09/21/2017 |   111        | 11/26/2015   | 2015            | 30            | 15             | Cesarean        | false |
    
    
    