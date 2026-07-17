
--- ADD
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
select 'ACCOUNTING_CONTACT_ADD', 'ACCOUNTING',  'Contact Add', 1, (select id from permission where code = 'ACCOUNTING_CONTACT_LIST'), 'ACCOUNTING_MODULE'
from permission
where not exists (select code from permission where code = 'ACCOUNTING_CONTACT_ADD') limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_CONTACT_ADD', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_CONTACT_ADD'
                   and contextcode = 'ACCOUNTING') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_CONTACT_ADD', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'ACCOUNTING_CONTACT_ADD'
                   and contextcode = 'ACCOUNTING') limit 1;


--- EDIT
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
select 'ACCOUNTING_CONTACT_EDIT', 'ACCOUNTING',  'Contact Edit', 1, (select id from permission where code = 'ACCOUNTING_CONTACT_LIST'), 'ACCOUNTING_MODULE'
from permission
where not exists (select code from permission where code = 'ACCOUNTING_CONTACT_EDIT') limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_CONTACT_EDIT', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_CONTACT_EDIT'
                   and contextcode = 'ACCOUNTING') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_CONTACT_EDIT', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'ACCOUNTING_CONTACT_EDIT'
                   and contextcode = 'ACCOUNTING') limit 1;

--- DELETE
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
select 'ACCOUNTING_CONTACT_DELETE', 'ACCOUNTING',  'Contact Delete', 1, (select id from permission where code = 'ACCOUNTING_CONTACT_LIST'), 'ACCOUNTING_MODULE'
from permission
where not exists (select code from permission where code = 'ACCOUNTING_CONTACT_DELETE') limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_CONTACT_DELETE', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_CONTACT_DELETE'
                   and contextcode = 'ACCOUNTING') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_CONTACT_DELETE', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'ACCOUNTING_CONTACT_DELETE'
                   and contextcode = 'ACCOUNTING') limit 1;



DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_CONTACT_ADD';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_ADD', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_ADD', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_ADD', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_CONTACT_ADD';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_ADD', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_ADD', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_ADD', 'ALLOW', 'ACCOUNTANT');



DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_CONTACT_EDIT';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_EDIT', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_EDIT', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_EDIT', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_CONTACT_EDIT';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_EDIT', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_EDIT', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_EDIT', 'ALLOW', 'ACCOUNTANT');



DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_CONTACT_DELETE';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_DELETE', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_DELETE', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_DELETE', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_CONTACT_DELETE';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_DELETE', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_DELETE', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CONTACT_DELETE', 'ALLOW', 'ACCOUNTANT');
