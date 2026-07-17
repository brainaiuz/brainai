delete from "anv".modelfield where form_ID = 'EXPENSE_CLAIM_FORM' and field_ID = 'TOTAL';

insert into "anv".modelfield(form_ID, 			    field_ID,   sorder,                                                                   widget, 		source,   usableByWorkflow, type, 		 disableUpdate,  isWorkflowAttribute) values
                            ('EXPENSE_CLAIM_FORM',  'TOTAL',	(select count(id) from modelfield where form_id = 'EXPENSE_CLAIM_FORM'),  'TextBox', 	null,     true, 			'Number',	 true,           false);
