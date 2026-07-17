insert into permission (   code, context,      name,                  sorder, parent,                                                             modulecode         ) values
('UPDATE_CUSTOMER_CREDIT_LIMIT', 'ACCOUNTING', 'Update Credit Limit', 9,      (select id from permission where code ='ACCOUNTING_CUSTOMER_LIST'), 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('UPDATE_CUSTOMER_CREDIT_LIMIT', 'ADMIN','ALLOW');
insert into "0".permission_context(permissioncode, contextcode) values('UPDATE_CUSTOMER_CREDIT_LIMIT', 'ACCOUNTING');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('UPDATE_CUSTOMER_CREDIT_LIMIT', 'ADMIN','ALLOW');
insert into "anv".permission_context(permissioncode, contextcode) values('UPDATE_CUSTOMER_CREDIT_LIMIT', 'ACCOUNTING');
