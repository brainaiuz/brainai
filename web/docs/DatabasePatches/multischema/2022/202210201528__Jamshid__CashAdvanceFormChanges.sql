delete from "anv".customformsection where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and section = 'PURPOSE';
update "anv".customformsection set expanded = true where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and section in ('DETAILS', 'ATTACHMENTS');
update "anv".modelfield set fsection = 'ATTACHMENTS', columntype = 'COL_1' where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'PURPOSE';
update "anv".modelfield set fsection = 'ATTACHMENTS', columntype = 'COL_2' where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'ATTACHMENTS';
update "anv".modelfield set fsection = 'DETAILS', columntype = 'COL_3' where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'NUMBER';
update "anv".modelfield set columntype = 'COL_3' where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'APPROVER';
update "anv".modelfield set hide = true where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id = 'PAYMENT_METHOD';
delete from "anv".modelfield where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id in ('DATE', 'CASH_ADVANCE_ACCOUNT');