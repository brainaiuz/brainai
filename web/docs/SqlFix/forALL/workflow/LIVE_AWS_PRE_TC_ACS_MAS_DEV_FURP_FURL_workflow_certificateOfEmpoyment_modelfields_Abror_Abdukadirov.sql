insert into modelfield(form_ID, 				                  field_ID, 	 						sorder, widget , 				source, 													    usableByWorkflow, 	  type, 		disableUpdate, isWorkflowAttribute) values
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'NUMBER',						     1,     'TextBox', 		  null,                                 true, 							  'Text',	  true,          false),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'EMPLOYEE',						   2,     'DropDown', 		'CRM@EMPLOYEE',                       true, 							  'Text',	  true,          false),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'CERTIFICATE_TYPE',			 3,     'DropDown', 		'HRMS@CERTIFICATE_TYPE',              true, 							  'Text',	  true,          false),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'PREV_APPROVER',					 4, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'PREV_APPROVER_EMAIL',		 5, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'PREV_APPROVER_STATUS',	 6, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'CURRENT_APPROVER',			 7, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'CURRENT_APPROVER_EMAIL', 8, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'CURRENT_APPROVER_STATUS',9, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'NEXT_APPROVER',					 10, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'NEXT_APPROVER_EMAIL',		 11, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('CERTIFICATE_OF_EMPLOYMENT_FORM', 'NEXT_APPROVER_STATUS',	 12, 		 null, 	        null,                                 true, 								null,	    true,          true);


update model set certificateForm = true where id in (select m.id from model m
                                                     inner join "anv".certificateofemploymenttype t on m.formid = t.formid
                                                     where t.deleted = false
                                                    );

update "anv".model set certificateForm = true where id in (select m.id from "anv".model m
                                                           inner join "anv".certificateofemploymenttype t on m.formid = t.formid
                                                           where t.deleted = false
                                                           );