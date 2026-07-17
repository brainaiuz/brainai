
delete from "anv".reference where parentid = (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON');
delete from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON';


insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name)
values ('Employee Lost Reasons', 'EMPLOYEE_REJECTION_REASON', false, true, true, 'Employee Lost Reasons');


insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name, parentid, sorder, requiredComment)
values ('ReasonTest', 'EMPLOYEE_REJECTION_REASON_TEST_REASON', false, true, true, 'ReasonTest', (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON'), 1, true);

