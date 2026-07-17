delete from "anv".permission_context where permissioncode='REMOVE_EMPLOYEE_DOCUMENTS' and contextcode='PM';
insert into "anv".permission_context (permissioncode, contextcode) values ('REMOVE_EMPLOYEE_DOCUMENTS', 'PM');

delete from "0".permission_context where permissioncode='REMOVE_EMPLOYEE_DOCUMENTS' and contextcode='PM';
insert into "0".permission_context (permissioncode, contextcode) values ('REMOVE_EMPLOYEE_DOCUMENTS', 'PM');

