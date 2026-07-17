insert into permission (code,               context,  ismainmenu, name,               sorder, parent,                                               iscore,             modulecode)
                values('CRM_ACCOUNT_OWNER_EDIT', 'CRM',     false,      'Account Owner Edit', 6,     (select id from permission where code ='CRM_ACCOUNTS_EDIT'), false, 'CRM_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_ACCOUNT_OWNER_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_ACCOUNT_OWNER_EDIT', 'DR','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_ACCOUNT_OWNER_EDIT', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_ACCOUNT_OWNER_EDIT', 'DR','ALLOW');
