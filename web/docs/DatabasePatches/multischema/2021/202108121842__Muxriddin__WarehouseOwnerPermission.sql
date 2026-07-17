
delete from permission where code='WAREHOUSE_OWNER';
insert into permission (code,context,name,sorder,parent,modulecode) values ('WAREHOUSE_OWNER',
                                                                            'ACCOUNTING',
                                                                            'Warehouse Owner',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_WAREHOUSE_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_WAREHOUSE_LIST'),
                                                                             'INVENTORY_MANAGEMENT'
                                                                            );

delete from "anv".permission_context where permissioncode='WAREHOUSE_OWNER';
insert into "anv".permission_context (permissioncode,contextcode) values ('WAREHOUSE_OWNER','ACCOUNTING');

delete from "anv".rolepermission where permissioncode='WAREHOUSE_OWNER';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('WAREHOUSE_OWNER','ALLOW','ADMIN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('WAREHOUSE_OWNER','ALLOW','ACCOUNTANT');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('WAREHOUSE_OWNER','ALLOW','SALESMAN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('WAREHOUSE_OWNER','ALLOW','SALESPERSON');

