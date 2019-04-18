Feature: Document Labor and Delivery Reports
    Obstetrics (OB/GYN) HCPs should be able to document labor and delivery reports
	And OB/GYN or non OB/GYN HCPs and patients should be able to view the reports
	So that the patients' previous labor and delivery reports can be tracked
	
Scenario Outline: OB/GYN HCP documents a labor and delivery report for no twins
	Given There exists an obstetrics HCP in the system
	And The obstetrics patient has a current obstetrics record
	Then The OB/GYN HCP logs in and navigates to the Document Patient Labor and Delivery Reports page
    When The OB/GYN HCP selects the patient <patient> and enters the date of labor <dateLabor>, time of labor <timeLabor>, date of delivery <dateDelivery>, time of delivery <timeDelivery>, delivery method <deliveryType>, weight in pounds <lbs> and ounces <oz>, length <length>, heart rate <heartRate>, blood pressure <bloodPres>, first name <firstName>, and last name <lastName>
    And The OB/GYN HCP adds the labor and delivery report
	Then The labor and delivery report is documented successfully

Examples:
    | patient         | dateLabor         | timeLabor     | dateDelivery   | timeDelivery   | deliveryType   | lbs      | oz     | length   | heartRate   | bloodPres   | firstName   | lastName   |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Vaginal        | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | null       |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Miscarriage    | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    
Scenario Outline: OB/GYN HCP documents a labor and delivery report for twins
	Given There exists an obstetrics HCP in the iTrust2 system
	And The obstetrics patient has a current obstetrics record in the iTrust2 system
	Then The OB/GYN HCP logs in and navigates to the Document Patient Labor and Delivery Reports page
    When The OB/GYN HCP selects the patient <patient> and enters the date of labor <dateLabor>, time of labor <timeLabor>, date of delivery <dateDelivery>, time of delivery <timeDelivery>, delivery method <deliveryType>, weight in pounds <lbs> and ounces <oz>, length <length>, heart rate <heartRate>, blood pressure <bloodPres>, first name <firstName>, and last name <lastName> for twins
    And the OB/GYN HCP adds the labor and delivery report
	Then The labor and delivery report is documented successfully
	
Examples:
    | patient         | dateLabor         | timeLabor     | dateDelivery   | timeDelivery   | deliveryType   | lbs      | oz     | length   | heartRate   | bloodPres   | firstName   | lastName   |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Vaginal        | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | null       |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Miscarriage    | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    
Scenario Outline: OB/GYN HCP incorrectly documents a labor and delivery report
	Given There exists an obstetrics HCP in the iTrust2 system
	And The obstetrics patient has a current obstetrics record in the iTrust2 system
	Then The OB/GYN HCP logs in and navigates to the Document Patient Labor and Delivery Reports page
    When The OB/GYN HCP selects the patient <patient> and enters the date of labor <dateLabor>, time of labor <timeLabor>, date of delivery <dateDelivery>, time of delivery <timeDelivery>, delivery method <deliveryType>, weight in pounds <lbs> and ounces <oz>, length <length>, heart rate <heartRate>, blood pressure <bloodPres>, first name <firstName>, and last name <lastName>
    And the OB/GYN HCP adds the labor and delivery report
	Then The labor and delivery report is not documented successfully
	
Examples:
    | patient         | dateLabor         | timeLabor     | dateDelivery   | timeDelivery   | deliveryType   | lbs      | oz     | length   | heartRate   | bloodPres   | firstName   | lastName   |
    | AliceThirteen   | 03/11/2020        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10000         | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/11/2020     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10000          | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Mistake        | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | -10      | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | -1     | 9        | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | -9       | 60          | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | -60         | 90          | Moe         | Joe        |
    | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | -90         | null        | Joe        |

Scenario Outline: OB/GYN HCP edits a labor and delivery report
	Given There exists an obstetrics patient in the system
	And The obstetrics patient has a current obstetrics record in the iTrust2 system
	And There exists an obstetrics HCP in the iTrust2 system
	And The obstetrics patient has a documented labor and delivery report
	Then The OB/GYN HCP logs in and navigates to the Edit Patient Labor and Delivery Reports page
    When The OB/GYN HCP selects the patient <patient> and the date of the report <dateReport>
	And The OB/GYN HCP modifies the date of labor <newDateLabor>, length <newLength>, and the first name <newFirstName>
	And The OB/GYN HCP saves the labor and delivery report
	Then The labor and delivery report is updated successfully

Examples:
    | newDateLabor    | newLength   | newFirstName    | patient         |
    | 02/11/2019      | 7           | Doe             | JillBob   |
    
Scenario Outline: OB/GYN HCP incorrectly edits a labor and delivery report
	Given There exists an obstetrics patient in the system
	And The obstetrics patient has a current obstetrics record in the iTrust2 system
	And There exists an obstetrics HCP in the iTrust2 system
	And The obstetrics patient has a documented labor and delivery report
	Then The OB/GYN HCP logs in and navigates to the Document Patient Labor and Delivery Reports page
    When The OB/GYN HCP selects the patient <patient> and the date of the report <dateReport>
	And The OB/GYN HCP modifies the date of labor <newDateLabor>, length <newLength>, and the first name <newFirstName>
	And The OB/GYN HCP saves the labor and delivery report
	Then The labor and delivery report is not updated successfully
	
Examples:
    | newDateLabor    | newLength   | newFirstName    | patient         | dateLabor         | timeLabor     | dateDelivery   | timeDelivery   | deliveryType   | lbs      | oz     | length   | heartRate   | bloodPres   | firstName   | lastName   |
    | 02/11/2020      | 7           | Doe             | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | 02/11/2019      | -7          | Doe             | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |
    | 02/11/2020      | 7           | null            | AliceThirteen   | 03/11/2019        | 10:00 am      | 03/22/2019     | 10:00 am       | Cesarean       | 10       | 1      | 9        | 60          | 90          | Moe         | Joe        |

    