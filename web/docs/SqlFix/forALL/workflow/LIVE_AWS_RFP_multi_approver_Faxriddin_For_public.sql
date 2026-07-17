
ALTER TABLE company DROP COLUMN if exists selectFunctioncolumn;
ALTER TABLE company ADD COLUMN selectFunctioncolumn integer;

delete from model  where formid = 'REQUEST_FOR_PURCHASE_FORM';
insert into model(formID, active, title) values('REQUEST_FOR_PURCHASE_FORM',true, 'Request For Purchase');

DELETE from modelfield WHERE form_ID='REQUEST_FOR_PURCHASE_FORM';

INSERT INTO modelfield(form_ID, 				            field_ID, 	 						sorder,   widget , 				source, 	usableByWorkflow, 	     type, 		disableUpdate, isWorkflowAttribute) VALUES
											('REQUEST_FOR_PURCHASE_FORM', 'DUE_DATE',					        2, 		 null, 	        null,        true, 							  	null,	    true,          true),
                      ('REQUEST_FOR_PURCHASE_FORM', 'MANAGER',					        2, 		 null, 	        null,         true, 								null,	    true,          true),
                      ('REQUEST_FOR_PURCHASE_FORM', 'NUMBER',					          2, 		 null, 	        null,         true, 								null,	    true,          true),
                      ('REQUEST_FOR_PURCHASE_FORM', 'PROJECT',					        2, 		 null, 	        null,         true, 								null,	    true,          true),
                      ('REQUEST_FOR_PURCHASE_FORM', 'PREV_APPROVER',					  2, 		 null, 	        null,         true, 								null,	    true,          true),
											('REQUEST_FOR_PURCHASE_FORM', 'PREV_APPROVER_EMAIL',		  2, 		 null, 	        null,         true, 								null,	    true,          true),
											('REQUEST_FOR_PURCHASE_FORM', 'PREV_APPROVER_STATUS',	    2, 		 null, 	        null,         true, 								null,	    true,          true),
											('REQUEST_FOR_PURCHASE_FORM', 'CURRENT_APPROVER',			    2, 		 null, 	        null,         true, 								null,	    true,          true),
											('REQUEST_FOR_PURCHASE_FORM', 'CURRENT_APPROVER_EMAIL',   2, 		 null, 	        null,         true, 								null,	    true,          true),
											('REQUEST_FOR_PURCHASE_FORM', 'CURRENT_APPROVER_STATUS',  2, 		 null, 	        null,         true, 								null,	    true,          true),
											('REQUEST_FOR_PURCHASE_FORM', 'NEXT_APPROVER',					  2, 		 null, 	        null,         true, 								null,	    true,          true),
											('REQUEST_FOR_PURCHASE_FORM', 'NEXT_APPROVER_EMAIL',		  2, 		 null, 	        null,         true, 								null,	    true,          true),
											('REQUEST_FOR_PURCHASE_FORM', 'NEXT_APPROVER_STATUS',	    2, 		 null, 	        null,         true, 								null,	    true,          true);