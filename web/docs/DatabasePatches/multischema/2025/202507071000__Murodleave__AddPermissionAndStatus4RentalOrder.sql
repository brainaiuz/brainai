


delete from permission
where code = 'ACCOUNTING_RENTAL_ORDER_PRINT_PDF';
delete from "anv".permission_context
where permissioncode = 'ACCOUNTING_RENTAL_ORDER_PRINT_PDF';
delete from "anv".rolepermission
where permissioncode = 'ACCOUNTING_RENTAL_ORDER_PRINT_PDF';
INSERT INTO permission (code, context, name, sorder, parent, modulecode, parent_code)
SELECT 'ACCOUNTING_RENTAL_ORDER_PRINT_PDF', 'ACCOUNTING', 'Print Pdf',
       (select max(sorder) + 1 from permission p where parent = (select id from permission p where p.code = 'ACCOUNTING_RENTAL_ORDER_LIST' limit 1) and modulecode = 'RENTAL_ORDER_MODULE'),
       p.id, 'RENTAL_ORDER_MODULE', p.code
FROM permission p
WHERE p.code = 'ACCOUNTING_RENTAL_ORDER_LIST' limit 1;
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_RENTAL_ORDER_PRINT_PDF', 'ACCOUNTING');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_RENTAL_ORDER_PRINT_PDF', 'ALLOW', 'ADMIN');



delete from permission
where code = 'ACCOUNTING_RENTAL_ORDER_RETURN';
delete from "anv".permission_context
where permissioncode = 'ACCOUNTING_RENTAL_ORDER_RETURN';
delete from "anv".rolepermission
where permissioncode = 'ACCOUNTING_RENTAL_ORDER_RETURN';
INSERT INTO permission (code, context, name, sorder, parent, modulecode, parent_code)
SELECT 'ACCOUNTING_RENTAL_ORDER_RETURN', 'ACCOUNTING', 'Return',
       (select max(sorder) + 1 from permission p where parent = (select id from permission p where p.code = 'ACCOUNTING_RENTAL_ORDER_LIST' limit 1) and modulecode = 'RENTAL_ORDER_MODULE'),
       p.id, 'RENTAL_ORDER_MODULE', p.code
FROM permission p
WHERE p.code = 'ACCOUNTING_RENTAL_ORDER_LIST' limit 1;
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_RENTAL_ORDER_RETURN', 'ACCOUNTING');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_RENTAL_ORDER_RETURN', 'ALLOW', 'ADMIN');



delete from permission
where code = 'ACCOUNTING_RENTAL_ORDER_DELIVER';
delete from "anv".permission_context
where permissioncode = 'ACCOUNTING_RENTAL_ORDER_DELIVER';
delete from "anv".rolepermission
where permissioncode = 'ACCOUNTING_RENTAL_ORDER_DELIVER';
INSERT INTO permission (code, context, name, sorder, parent, modulecode, parent_code)
SELECT 'ACCOUNTING_RENTAL_ORDER_DELIVER', 'ACCOUNTING', 'Deliver',
       (select max(sorder) + 1 from permission p where parent = (select id from permission p where p.code = 'ACCOUNTING_RENTAL_ORDER_LIST' limit 1) and modulecode = 'RENTAL_ORDER_MODULE'),
       p.id, 'RENTAL_ORDER_MODULE', p.code
FROM permission p
WHERE p.code = 'ACCOUNTING_RENTAL_ORDER_LIST' limit 1;
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_RENTAL_ORDER_DELIVER', 'ACCOUNTING');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_RENTAL_ORDER_DELIVER', 'ALLOW', 'ADMIN');



INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
VALUES ('RENTAL_INVOICED', 'Invoiced', true, true, true, false, (select id from "anv".reference where code = 'RENTAL_STATUS'));

