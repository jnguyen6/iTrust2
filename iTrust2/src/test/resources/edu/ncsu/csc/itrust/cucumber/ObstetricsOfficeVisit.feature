Feature: Document obstetrics office visits
	Obstetrics (OB/GYN) HCPs should be able to document obstetrics office visits
	And OB/GYN or non OB/GYN HCPs and patients should be able to view obstetrics office visits
	So that the obstetrics office visit record of the patient can be tracked
	
Scenario Outline: OB/GYN HCP documents an obstetrics office visit
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	Then The OB/GYN HCP logs in and navigates to the Document Office Visit page
    When The OB/GYN HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
	And The OB/GYN HCP enters the obstetrics health metrics with weeks pregnant <weeksPreg>, fetal heart rate <fetalHeartRate>, fundal height of uterus <fundalHeight>, twins <twins>, and low-lying placenta <lowPlacenta> 
	And The OB/GYN HCP enters notes <notes>
	And The OB/GYN HCP submits the obstetrics office visit
	Then The obstetrics office visit is documented successfully
	And The log is updated stating that the obstetrics office visit was documented
	
Examples:
	| date			| time 		| patient 		| type 					  | hospital 			| weeksPreg 	| fetalHeartRate	| fundalHeight	| twins 	| lowPlacenta 	| notes 	                                                     |
	| 03/11/2019	| 10:00 am	| AliceThirteen | GENERAL_OBSTETRICS      | General Hospital 	| 10		    | 30				| 1.5 		    | false		| false			| This is a test for documenting a valid obstetrics office visit |
	
Scenario Outline: OB/GYN HCP incorrectly documents an obstetrics office visit
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	Then The OB/GYN HCP logs in and navigates to the Document Office Visit page
	When The OB/GYN HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
	And The OB/GYN HCP enters the obstetrics health metrics with weeks pregnant <weeksPreg>, fetal heart rate <fetalHeartRate>, fundal height of uterus <fundalHeight>, twins <twins>, and low-lying placenta <lowPlacenta> 
	And The OB/GYN HCP enters notes <notes>
	And The OB/GYN HCP submits the obstetrics office visit
	Then The obstetrics office visit is not documented successfully
	
Examples:
	| date			| time 	    | patient 		| type 					| hospital 			| weeksPreg 	    | fetalHeartRate	| fundalHeight	| twins 	| lowPlacenta 	| notes 	                                                        |
	| 03/11/2019	| 10:00 am	| AliceThirteen | GENERAL_OBSTETRICS	| General Hospital 	| -10			    | 30				| 1.5		    | false		| false			| This is a test for documenting an invalid obstetrics office visit |
	| 03/11/2019	| 10:00 am	| AliceThirteen | GENERAL_OBSTETRICS	| General Hospital 	| weeks			    | 30			    | 1.5		    | false		| false			| This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 10			    | -30				| 1.5		    | false  	| false			| This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 10			    | fetal         	| 1.5		    | false  	| false			| This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 10			    | 30				| 1		        | false  	| false			| This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 10			    | 30				| fundal    	| false  	| false			| This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 10			    | 30				| 1.5		    | null  	| false			| This is a test for documenting an invalid obstetrics office visit	|
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 10			    | -30				| 1.5		    | false  	| null			| This is a test for documenting an invalid obstetrics office visit	|	
	
Scenario Outline: OB/GYN HCP documents an obstetrics office visit with Basic Health Metrics
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	Then The OB/GYN HCP logs in and navigates to the Document Office Visit page
    When The OB/GYN HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
	And The OB/GYN HCP enters the basic health metrics with height <height>, weight <weight>, systolic <systolic>, diastolic <diastolic>, HDL <HDL>, LDL <LDL>, Triglycerides <tri>, patient smoking status <patientSmoking>, and household smoking status <householdSmokingStatus>
	And The OB/GYN HCP enters the obstetrics health metrics with weeks pregnant <weeksPreg>, fetal heart rate <fetalHeartRate>, fundal height of uterus <fundalHeight>, twins <twins>, and low-lying placenta <lowPlacenta> 
	And The OB/GYN HCP enters notes <notes>
	And The OB/GYN HCP submits the obstetrics office visit
	Then The obstetrics office visit is documented successfully
	And The log is updated stating that the obstetrics office visit was documented

Examples:
	| date			| time 		| patient 		| type 					| hospital 			| weeksPreg	        | fetalHeartRate	| fundalHeight	| twins	    | lowPlacenta 	|  height  |  weight  |  systolic  |  diastolic  |  HDL    |  LDL  |  tri  | patientSmoking  | householdSmokingStatus  |  notes 									                |
	| 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 10				| 30				| 2.5		    | false		| false		    |  160     |  160     |  50        |     50      |  60     |  105  |  550  | NEVER           | NONSMOKING              |  This is a test for documenting obstetrics office visit	|
	
Scenario Outline: OB/GYN HCP edits an obstetrics office visit
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	Then The OB/GYN HCP logs in and navigates to the Document Office Visit page
    When The OB/GYN HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
	And The OB/GYN HCP enters the basic health metrics with height <height>, weight <weight>, systolic <systolic>, diastolic <diastolic>, HDL <HDL>, LDL <LDL>, Triglycerides <tri>, patient smoking status <patientSmoking>, and household smoking status <householdSmokingStatus>
    And The OB/GYN HCP enters the obstetrics health metrics with weeks pregnant <weeksPreg>, fetal heart rate <fetalHeartRate>, fundal height of uterus <fundalHeight>, twins <twins>, and low-lying placenta <lowPlacenta> 
	And The OB/GYN HCP enters notes <notes>
	And The OB/GYN HCP submits the obstetrics office visit
	Then The obstetrics office visit is documented successfully
	Then The OB/GYN HCP logs in and navigates to the Edit Office Visit page
	When The OB/GYN HCP selects the existing office visit
	And The OB/GYN HCP modifies the date to be <newdate>, height <newheight>, and the weeks pregnant <newWeeksPreg>
	And The OB/GYN HCP saves the office visit
	Then The obstetrics office visit is updated successfully

Examples:
	| newdate   	| newheight | newWeeksPreg		| date			| time 		| patient 		| type 					| hospital 			| weeksPreg	        | fetalHeartRate	| fundalHeight	| twins	    | lowPlacenta 	|  height  |  weight  |  systolic  |  diastolic  |  HDL    |  LDL  |  tri  | patientSmoking  | householdSmokingStatus  |  notes 									                |
	| 03/29/2019	| 150    	| 5				    | 03/11/2019	| 10:00 am	| AliceThirteen	| GENERAL_OBSTETRICS	| General Hospital 	| 10				| 30				| 2.5		    | false		| false		    |  160     |  160     |  50        |     50      |  60     |  105  |  550  | NEVER           | NONSMOKING              |  This is a test for editing obstetrics office visit	    |

Scenario Outline: OB/GYN HCP incorrectly edits an obstetrics office visit 
	Given There exists an obstetrics patient in the system
	And There exists an obstetrics HCP in the system
	And there are office visits of all types
	Then The OB/GYN HCP logs in and navigates to the Edit Office Visit page
	When The OB/GYN HCP selects the existing office visit
	And The OB/GYN HCP modifies the date to be <date>, height <height>, and the weeks pregnant <weeksPreg>
	And The OB/GYN HCP saves the office visit
	Then The obstetrics office visit is not updated successfully

Examples:
	| date			| weeksPreg		| fundalHeight     	|
	| 03/11/2019	| -1   		    | 30            	|
	| 03/11/2019	| 10            | -30     			|