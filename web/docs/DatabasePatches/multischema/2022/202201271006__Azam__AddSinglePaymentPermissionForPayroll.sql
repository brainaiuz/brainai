
--single payment list
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST';
insert into permission (code, context, name, sorder, parent, modulecode) values
('PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST', 'PAYROLL', 'Single Payments', 6, (select id from permission where code = 'PAYROLL_MAIN_CONTENT'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST', 'ALLOW', 'DR'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST', 'ALLOW', 'HR');

--single payment view
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_ITEM_VIEW';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_VIEW', 'PAYROLL', 'View', 1, (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_VIEW';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_VIEW', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_VIEW';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_VIEW', 'ALLOW', 'DR'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_VIEW', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_VIEW', 'ALLOW', 'HR');
--single payment pdf
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_ITEM_PDF';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_PDF', 'PAYROLL', 'PDF', 2, (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_PDF';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_PDF', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_PDF';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_PDF', 'ALLOW', 'DR'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_PDF', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_PDF', 'ALLOW', 'HR');

--single payment delete
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_ITEM_DELETE';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_DELETE', 'PAYROLL', 'Delete', 3, (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_DELETE';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_DELETE', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_DELETE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_DELETE', 'ALLOW', 'DR'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_DELETE', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_DELETE', 'ALLOW', 'HR');

--update sorder
update permission set sorder=7 where code='END_OF_SERVICE_GRATUITY_LIST';
update permission set sorder=8 where code='PAYROLL_PENDING_CHANGES';