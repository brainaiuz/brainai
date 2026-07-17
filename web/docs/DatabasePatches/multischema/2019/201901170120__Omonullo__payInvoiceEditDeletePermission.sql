INSERT INTO permission (code, context, name, sorder, parent, modulecode)
select 'PAY_INVOICE_EDIT', 'ACCOUNTING',  'Receive Payment Edit', 1, (select id from permission where code = 'ACCOUNTING_PAY_BILL'), 'PURCHASE_INVOICING'
from permission
where not exists (select code from permission where code = 'PAY_INVOICE_EDIT') limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'PAY_INVOICE_EDIT', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'PAY_INVOICE_EDIT'
                   and contextcode = 'ACCOUNTING') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'PAY_INVOICE_EDIT', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'PAY_INVOICE_EDIT'
                   and contextcode = 'ACCOUNTING') limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'PAY_INVOICE_EDIT', 'LOGISTICS'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'PAY_INVOICE_EDIT'
                   and contextcode = 'LOGISTICS') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'PAY_INVOICE_EDIT', 'LOGISTICS'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'PAY_INVOICE_EDIT'
                   and contextcode = 'LOGISTICS') limit 1;

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
select 'PAY_INVOICE_DELETE', 'ACCOUNTING',  'Receive Payment Delete', 1, (select id from permission where code = 'ACCOUNTING_PAY_BILL'), 'PURCHASE_INVOICING'
from permission
where not exists (select code from permission where code = 'PAY_INVOICE_DELETE') limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'PAY_INVOICE_DELETE', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'PAY_INVOICE_DELETE'
                   and contextcode = 'ACCOUNTING') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'PAY_INVOICE_DELETE', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'PAY_INVOICE_DELETE'
                   and contextcode = 'ACCOUNTING') limit 1;


insert into "anv".permission_context (permissioncode, contextcode)
select 'PAY_INVOICE_DELETE', 'LOGISTICS'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'PAY_INVOICE_DELETE'
                   and contextcode = 'LOGISTICS') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'PAY_INVOICE_DELETE', 'LOGISTICS'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'PAY_INVOICE_DELETE'
                   and contextcode = 'LOGISTICS') limit 1;
                   
                   
                                      
DELETE FROM "anv".rolepermission WHERE permissioncode = 'PAY_INVOICE_EDIT';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_EDIT', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_EDIT', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_EDIT', 'ALLOW', 'ACCOUNTANT');

                   
DELETE FROM "0".rolepermission WHERE permissioncode = 'PAY_INVOICE_EDIT';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_EDIT', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_EDIT', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_EDIT', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'PAY_INVOICE_DELETE';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_DELETE', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_DELETE', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_DELETE', 'ALLOW', 'ACCOUNTANT');

                   
DELETE FROM "0".rolepermission WHERE permissioncode = 'PAY_INVOICE_DELETE';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_DELETE', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_DELETE', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('PAY_INVOICE_DELETE', 'ALLOW', 'ACCOUNTANT');