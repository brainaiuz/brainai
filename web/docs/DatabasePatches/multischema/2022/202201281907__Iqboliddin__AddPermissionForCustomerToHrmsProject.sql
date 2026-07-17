insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_CUSTOMER_ADD_CLIENT', 'HRMS', 'Add Customer',
        ((select sorder from permission where name = 'Group Goals') + 1),
        (select id from permission where code = 'HRMS_GOAL_MANAGEMENT_TAB'), 'HRMS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_CUSTOMER_ADD_CLIENT', 'HRMS');

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_CUSTOMER_ADD_CLIENT', 'ALLOW', 'ADMIN');
