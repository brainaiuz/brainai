
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
select 'RECEIVE_PAYMENT_EDIT', 'ACCOUNTING',  'Pay Invoice Edit', 1, (select id from permission where code = 'ACCOUNTING_PAY_BILL'), 'PURCHASE_INVOICING'
from permission
where not exists (select code from permission where code = 'RECEIVE_PAYMENT_EDIT') limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'RECEIVE_PAYMENT_EDIT', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'RECEIVE_PAYMENT_EDIT'
                   and contextcode = 'ACCOUNTING') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'RECEIVE_PAYMENT_EDIT', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'RECEIVE_PAYMENT_EDIT'
                   and contextcode = 'ACCOUNTING') limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'RECEIVE_PAYMENT_EDIT', 'LOGISTICS'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'RECEIVE_PAYMENT_EDIT'
                   and contextcode = 'LOGISTICS') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'RECEIVE_PAYMENT_EDIT', 'LOGISTICS'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'RECEIVE_PAYMENT_EDIT'
                   and contextcode = 'LOGISTICS') limit 1;

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
select 'RECEIVE_PAYMENT_DELETE', 'ACCOUNTING',  'Pay Invoice Delete', 1, (select id from permission where code = 'ACCOUNTING_PAY_BILL'), 'PURCHASE_INVOICING'
from permission
where not exists (select code from permission where code = 'RECEIVE_PAYMENT_DELETE') limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'RECEIVE_PAYMENT_DELETE', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'RECEIVE_PAYMENT_DELETE'
                   and contextcode = 'ACCOUNTING') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'RECEIVE_PAYMENT_DELETE', 'LOGISTICS'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'RECEIVE_PAYMENT_DELETE'
                   and contextcode = 'LOGISTICS') limit 1;

DELETE FROM "anv".rolepermission WHERE permissioncode = 'RECEIVE_PAYMENT_EDIT';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_EDIT', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_EDIT', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_EDIT', 'ALLOW', 'ACCOUNTANT');


DELETE FROM "0".rolepermission WHERE permissioncode = 'RECEIVE_PAYMENT_EDIT';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_EDIT', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_EDIT', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_EDIT', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'RECEIVE_PAYMENT_DELETE';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_DELETE', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_DELETE', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_DELETE', 'ALLOW', 'ACCOUNTANT');


DELETE FROM "0".rolepermission WHERE permissioncode = 'RECEIVE_PAYMENT_DELETE';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_DELETE', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_DELETE', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RECEIVE_PAYMENT_DELETE', 'ALLOW', 'ACCOUNTANT');