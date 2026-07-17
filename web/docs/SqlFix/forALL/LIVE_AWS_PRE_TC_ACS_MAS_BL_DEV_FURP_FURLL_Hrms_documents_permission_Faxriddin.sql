--parent: HRMS_MAIN_MENU
--code: HRMS_DOCUMENTS_MANAGEMENT


delete from permission where code = 'HRMS_DOCUMENTS_MANAGEMENT';
delete from permission where code = 'EMPLOYEE_INSURANCE_DOCUMENTS_LIST';

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
 ('HRMS_DOCUMENTS_MANAGEMENT','HRMS','Documents Tab',7,false,(select id from permission where code='HRMS_MAIN_MENU'),false,'HRMS_MODULE');

 insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
 ('EMPLOYEE_INSURANCE_DOCUMENTS_LIST','HRMS','Insurance documents list',15,false,(select id from permission where code='HRMS_DOCUMENTS_MANAGEMENT'),false,'HRMS_MODULE');


 delete from "anv".rolepermission where permissioncode = 'HRMS_DOCUMENTS_MANAGEMENT';
 delete from "anv".rolepermission where permissioncode = 'EMPLOYEE_INSURANCE_DOCUMENTS_LIST';

insert into "anv".rolepermission (permissioncode, rolecode,access) values('HRMS_DOCUMENTS_MANAGEMENT','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('HRMS_DOCUMENTS_MANAGEMENT','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('HRMS_DOCUMENTS_MANAGEMENT','HR','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode,access) values('EMPLOYEE_INSURANCE_DOCUMENTS_LIST','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('EMPLOYEE_INSURANCE_DOCUMENTS_LIST','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('EMPLOYEE_INSURANCE_DOCUMENTS_LIST','HR','ALLOW');

delete from "0".rolepermission where permissioncode = 'HRMS_DOCUMENTS_MANAGEMENT';
delete from "0".rolepermission where permissioncode = 'EMPLOYEE_INSURANCE_DOCUMENTS_LIST';

insert into "0".rolepermission (permissioncode, rolecode,access) values('HRMS_DOCUMENTS_MANAGEMENT','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('HRMS_DOCUMENTS_MANAGEMENT','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('HRMS_DOCUMENTS_MANAGEMENT','HR','ALLOW');


insert into "0".rolepermission (permissioncode, rolecode,access) values('EMPLOYEE_INSURANCE_DOCUMENTS_LIST','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('EMPLOYEE_INSURANCE_DOCUMENTS_LIST','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('EMPLOYEE_INSURANCE_DOCUMENTS_LIST','HR','ALLOW');