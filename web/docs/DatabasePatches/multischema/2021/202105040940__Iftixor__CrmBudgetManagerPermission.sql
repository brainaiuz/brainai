

delete from permission where code='CRM_BUDGET_SHEET';
insert into permission (code,context,name,sorder,parent,modulecode) values ('CRM_BUDGET_SHEET','CRM','Budget Manager',0,(select id from permission where code='CRM_SALES_TAB'),'ACCOUNTING_MODULE');

delete from "anv".rolepermission where permissioncode='CRM_BUDGET_SHEET';
delete from "anv".permission_context where permissioncode='CRM_BUDGET_SHEET';

insert into "anv".permission_context (permissioncode,contextcode) values ('CRM_BUDGET_SHEET','CRM');
insert into "anv".rolepermission (permissioncode,access,rolecode) values
                                          ('CRM_BUDGET_SHEET','ALLOW','ADMIN'),
                                          ('CRM_BUDGET_SHEET','ALLOW','SALESMAN'),
                                          ('CRM_BUDGET_SHEET','ALLOW','DR');