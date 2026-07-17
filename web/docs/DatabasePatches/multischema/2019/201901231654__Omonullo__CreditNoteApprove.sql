INSERT INTO permission (code, context, name, sorder, parent, modulecode)
select 'CREDIT_NOTE_APPROVE',
       'ACCOUNTING',
       'Credit Note Approve',
       (select max(sorder) + 1
        from permission
        where parent = (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST')),
       (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
       'SALES_INVOICING'
from permission
where not exists(select code from permission where code = 'CREDIT_NOTE_APPROVE')
limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'CREDIT_NOTE_APPROVE', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'CREDIT_NOTE_APPROVE'
                   and contextcode = 'ACCOUNTING')
limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'CREDIT_NOTE_APPROVE', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'CREDIT_NOTE_APPROVE'
                   and contextcode = 'ACCOUNTING')
limit 1;

insert into "anv".permission_context (permissioncode, contextcode)
select 'CREDIT_NOTE_APPROVE', 'LOGISTICS'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'CREDIT_NOTE_APPROVE'
                   and contextcode = 'LOGISTICS')
limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'CREDIT_NOTE_APPROVE', 'LOGISTICS'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'CREDIT_NOTE_APPROVE'
                   and contextcode = 'LOGISTICS')
limit 1;

DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'CREDIT_NOTE_APPROVE';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CREDIT_NOTE_APPROVE', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CREDIT_NOTE_APPROVE', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CREDIT_NOTE_APPROVE', 'ALLOW', 'ACCOUNTANT');


DELETE
FROM "0".rolepermission
WHERE permissioncode = 'CREDIT_NOTE_APPROVE';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode)
VALUES ('CREDIT_NOTE_APPROVE', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode)
VALUES ('CREDIT_NOTE_APPROVE', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode)
VALUES ('CREDIT_NOTE_APPROVE', 'ALLOW', 'ACCOUNTANT');