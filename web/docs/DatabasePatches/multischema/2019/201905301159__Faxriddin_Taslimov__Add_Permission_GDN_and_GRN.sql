delete from permission where code = 'ACCOUNTING_GRN_DELETE';
delete from permission where code = 'ACCOUNTING_GRN_CONVERT_TO_INVOICE';
delete from permission where code = 'ACCOUNTING_GDN_DELETE';
delete from permission where code = 'ACCOUNTING_GDN_CONVERT_TO_INVOICE';

insert into permission (code,
				context,
				name,
				sorder,
				parent,
				modulecode)
                values ('ACCOUNTING_GRN_DELETE',
				'ACCOUNTING',
				'GRN Delete',
				31,
				(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
				'ACCOUNTING_MODULE');

insert into permission (code,
				context,
				name,
				sorder,
				parent,
				modulecode)
                values ('ACCOUNTING_GRN_CONVERT_TO_INVOICE',
				'ACCOUNTING',
				'GRN Convert To Invoice',
				32,
				(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
				'ACCOUNTING_MODULE');

insert into permission (code,
				context,
				name,
				sorder,
				parent,
				modulecode)
                values ('ACCOUNTING_GDN_DELETE',
				'ACCOUNTING',
				'GDN Delete',
				31,
				(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
				'ACCOUNTING_MODULE');

insert into permission (code,
				context,
				name,
				sorder,
				parent,
				modulecode)
                values ('ACCOUNTING_GDN_CONVERT_TO_INVOICE',
				'ACCOUNTING',
				'GDN Convert To Invoice',
				32,
				(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
				'ACCOUNTING_MODULE');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_GRN_DELETE';
insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_GRN_DELETE','ACCOUNTING');
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_GRN_CONVERT_TO_INVOICE';
insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_GRN_CONVERT_TO_INVOICE','ACCOUNTING');
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_GDN_DELETE';
insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_GDN_DELETE','ACCOUNTING');
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_GDN_CONVERT_TO_INVOICE';
insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_GDN_CONVERT_TO_INVOICE','ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_GRN_DELETE';
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_DELETE', 'ACCOUNTANT', 'ALLOW');


delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_GRN_CONVERT_TO_INVOICE';
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_CONVERT_TO_INVOICE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_CONVERT_TO_INVOICE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_CONVERT_TO_INVOICE', 'ACCOUNTANT', 'ALLOW');


delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_GDN_DELETE';
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_DELETE', 'ACCOUNTANT', 'ALLOW');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_GDN_CONVERT_TO_INVOICE';
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_CONVERT_TO_INVOICE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_CONVERT_TO_INVOICE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_CONVERT_TO_INVOICE', 'ACCOUNTANT', 'ALLOW');


delete from "0".permission_context where permissioncode = 'ACCOUNTING_GRN_DELETE';
insert into "0".permission_context (permissioncode,contextcode) values ('ACCOUNTING_GRN_DELETE','ACCOUNTING');
delete from "0".permission_context where permissioncode = 'ACCOUNTING_GRN_CONVERT_TO_INVOICE';
insert into "0".permission_context (permissioncode,contextcode) values ('ACCOUNTING_GRN_CONVERT_TO_INVOICE','ACCOUNTING');
delete from "0".permission_context where permissioncode = 'ACCOUNTING_GDN_DELETE';
insert into "0".permission_context (permissioncode,contextcode) values ('ACCOUNTING_GDN_DELETE','ACCOUNTING');
delete from "0".permission_context where permissioncode = 'ACCOUNTING_GDN_CONVERT_TO_INVOICE';
insert into "0".permission_context (permissioncode,contextcode) values ('ACCOUNTING_GDN_CONVERT_TO_INVOICE','ACCOUNTING');

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_GRN_DELETE';
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_DELETE', 'ACCOUNTANT', 'ALLOW');

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_GRN_CONVERT_TO_INVOICE';
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_CONVERT_TO_INVOICE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_CONVERT_TO_INVOICE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GRN_CONVERT_TO_INVOICE', 'ACCOUNTANT', 'ALLOW');

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_GDN_DELETE';
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_DELETE', 'ACCOUNTANT', 'ALLOW');

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_GDN_CONVERT_TO_INVOICE';
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_CONVERT_TO_INVOICE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_CONVERT_TO_INVOICE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_GDN_CONVERT_TO_INVOICE', 'ACCOUNTANT', 'ALLOW');