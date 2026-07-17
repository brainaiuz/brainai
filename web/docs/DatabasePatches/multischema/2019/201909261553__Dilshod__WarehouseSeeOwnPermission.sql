

delete from "0".rolepermission where permissioncode='WAREHOUSE_SEE_OWN';
delete from "0".rolepermission where permissioncode='ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS';

delete from "0".permission_context where permissioncode='WAREHOUSE_SEE_OWN';
delete from "0".permission_context where permissioncode='ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS';

insert into "0".permission_context (permissioncode,contextcode) values ('WAREHOUSE_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS','ACCOUNTING');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('WAREHOUSE_SEE_OWN','ALLOW','ADMIN');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS','ALLOW','ADMIN');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS','ALLOW','DR');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS','ALLOW','ACCOUNTANT');


delete from permission where code='WAREHOUSE_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('WAREHOUSE_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_WAREHOUSE_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_WAREHOUSE_LIST'),
                                                                             'INVENTORY_MANAGEMENT'
                                                                            );

delete from "anv".rolepermission where permissioncode='WAREHOUSE_SEE_OWN';
delete from "anv".permission_context where permissioncode='WAREHOUSE_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('WAREHOUSE_SEE_OWN','ACCOUNTING');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('WAREHOUSE_SEE_OWN','ALLOW','ADMIN');

delete from permission where code='ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS',
                                                                            'ACCOUNTING',
                                                                            'Full List Access',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_WAREHOUSE_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_WAREHOUSE_LIST'),
                                                                             'INVENTORY_MANAGEMENT'
                                                                            );

delete from "anv".rolepermission where permissioncode='ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS';
delete from "anv".permission_context where permissioncode='ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS','ACCOUNTING');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS','ALLOW','ADMIN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS','ALLOW','DR');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS','ALLOW','ACCOUNTANT');

