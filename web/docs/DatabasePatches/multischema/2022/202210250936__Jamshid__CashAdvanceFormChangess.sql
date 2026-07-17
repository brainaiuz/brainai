delete from "0".customformsection where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and section = 'PURPOSE';
update "0".customformsection set expanded = true where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and section in ('DETAILS', 'ATTACHMENTS');
update "0".modelfield set fsection = 'ATTACHMENTS', columntype = 'COL_1' where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'PURPOSE';
update "0".modelfield set fsection = 'ATTACHMENTS', columntype = 'COL_2' where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'ATTACHMENTS';
update "0".modelfield set fsection = 'DETAILS', columntype = 'COL_3' where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'NUMBER';
update "0".modelfield set columntype = 'COL_3' where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'APPROVER';
update "0".modelfield set hide = true where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'PAYMENT_METHOD';
delete from "0".modelfield where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id in ('DATE', 'CASH_ADVANCE_ACCOUNT');