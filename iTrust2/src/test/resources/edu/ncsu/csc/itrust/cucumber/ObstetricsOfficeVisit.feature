Feature: Document obstetrics office visits
	Obstetrics (OB/GYN) HCPs should be able to document obstetrics office visits
	And OB/GYN or non OB/GYN HCPs and patients should be able to view obstetrics office visits
	So that the obstetrics office visit record of the patient can be tracked
	
Scenario Outline: OB/GYN HCP documents an obstetrics office visit
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	And The obstetrics patient has a current obstetrics record
	Then The OB/GYN HCP logs in and navigates to the Document Office Visit page
    When The OB/GYN HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
	And The OB/GYN HCP enters the obstetrics health metrics with fetal heart rate <fetalHeartRate>, fundal height of uterus <fundalHeight>, twins <isTwins>, and low-lying placenta <isLowLyingPlacenta> 
	And The OB/GYN HCP enters notes <notes>
	And The OB/GYN HCP submits the obstetrics office visit
	Then The obstetrics office visit is documented successfully
	
Examples:
	| date			| time 		| patient 		| type 					  | hospital 			| fetalHeartRate	| fundalHeight	| isTwins 	| isLowLyingPlacenta 	| notes 	                                                     |
	| 03/11/2019	| 10:00 am	| AliceThirteen | GENERAL_OBSTETRICS      | General Hospital 	| 30				| 1.5 		    | false		| false			        | This is a test for documenting a valid obstetrics office visit |
	
Scenario Outline: OB/GYN HCP incorrectly documents an obstetrics office visit
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	And The obstetrics patient has a current obstetrics record
	Then The OB/GYN HCP logs in and navigates to the Document Office Visit page
	When The OB/GYN HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
	And The OB/GYN HCP enters the obstetrics health metrics with fetal heart rate <fetalHeartRate>, fundal height of uterus <fundalHeight>, twins <isTwins>, and low-lying placenta <isLowLyingPlacenta> 
	And The OB/GYN HCP enters notes <notes>
	And The OB/GYN HCP submits the obstetrics office visit
	Then The obstetrics office visit is not documented successfully
	
Examples:
	| date			| time 	    | patient 		| type 					| hospital 			| fetalHeartRate	| fundalHeight	| isTwins 	| isLowLyingPlacenta | notes 	                                                        |
	| 03/11/2019	| 10:00 am	| AliceThirteen | GENERAL_OBSTETRICS	| General Hospital 	| 30				| 1.5		    | false		| false			     | This is a test for documenting an invalid obstetrics office visit |
	| 03/11/2019	| 10:00 am	| AliceThirteen | GENERAL_OBSTETRICS	| General Hospital 	| 30			    | 1.5		    | false		| false			     | This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| -30				| 1.5		    | false  	| false			     | This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| fetal         	| 1.5		    | false  	| false			     | This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 30				| 1		        | false  	| false			     | This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 30				| fundal    	| false  	| false			     | This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 30				| 1.5		    | null  	| false			     | This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| -30				| 1.5		    | false  	| null			     | This is a test for documenting an invalid obstetrics office visit	|	
	
Scenario Outline: OB/GYN HCP documents an obstetrics office visit with Basic Health Metrics
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	And The obstetrics patient has a current obstetrics record
	Then The OB/GYN HCP logs in and navigates to the Document Office Visit page
    When The OB/GYN HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
	And The OB/GYN HCP enters the basic health metrics with height <height>, weight <weight>, systolic <systolic>, diastolic <diastolic>, HDL <HDL>, LDL <LDL>, Triglycerides <tri>, patient smoking status <patientSmoking>, and household smoking status <householdSmokingStatus>
	And The OB/GYN HCP enters the obstetrics health metrics with fetal heart rate <fetalHeartRate>, fundal height of uterus <fundalHeight>, twins <isTwins>, and low-lying placenta <isLowLyingPlacenta> 
	And The OB/GYN HCP enters notes <notes>
	And The OB/GYN HCP submits the obstetrics office visit
	Then The obstetrics office visit is documented successfully

Examples:
	| date			| time 		| patient 		| type 					| hospital 			| fetalHeartRate	| fundalHeight	| isTwins	| isLowLyingPlacenta 	|  height  |  weight  |  systolic  |  diastolic  |  HDL    |  LDL  |  tri  | patientSmoking  | householdSmokingStatus  |  notes 									                |
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 30				| 2.5		    | false		| false		            |  160     |  160     |  50        |     50      |  60     |  105  |  100  | NEVER           | NONSMOKING              |  This is a test for documenting obstetrics office visit	|
	
Scenario Outline: OB/GYN HCP edits an obstetrics office visit
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	And The obstetrics patient has a current obstetrics record
	And The obstetrics patient has a documented obstetrics office visit
	Then The OB/GYN HCP logs in and navigates to the Edit Office Visit page
	When The OB/GYN HCP selects the existing office visit
	And The OB/GYN HCP modifies the fetal heart rate to be <newFetalHeartRate>, fundal height <newFundalHeight>, and the weeks pregnant <newWeeksPregnant>
	And The OB/GYN HCP saves the office visit
	Then The obstetrics office visit is updated successfully

Examples:
	| newFetalHeartRate   	| newFundalHeight | newWeeksPregnant	|
	| 50        	        | 150    	      | 5				    |

Scenario Outline: OB/GYN HCP incorrectly edits an obstetrics office visit 
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	And The obstetrics patient has a current obstetrics record
    And The obstetrics patient has a documented obstetrics office visit
	Then The OB/GYN HCP logs in and navigates to the Edit Office Visit page
	When The OB/GYN HCP selects the existing office visit
	And The OB/GYN HCP modifies the fetal heart rate to be <newFetalHeartRate>, fundal height <newFundalHeight>, and the weeks pregnant <newWeeksPregnant>
	And The OB/GYN HCP saves the office visit
	Then The obstetrics office visit is not updated successfully

Examples:
	| newFetalHeartRate   	| newFundalHeight | newWeeksPregnant	|
	| -30        	        | 150    	      | 5				    |
    | 30        	        | -150    	      | 5				    | 
    | 30        	        | 150    	      | -5				    |