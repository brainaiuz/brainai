

delete from permission where code='CRM_SHOW_SUPERVISED_OPPORTUNITIES';
insert into permission (code, context, name, sorder, parent, modulecode) values
('CRM_SHOW_SUPERVISED_OPPORTUNITIES', 'CRM', 'Show Supervised Opportunities', 2,(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),'OPPORTUNITY_TRACKING');

delete from "anv".permission_context where permissioncode = 'CRM_SHOW_SUPERVISED_OPPORTUNITIES';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_SHOW_SUPERVISED_OPPORTUNITIES', 'CRM');


delete from "anv".rolepermission where permissioncode = 'CRM_SHOW_SUPERVISED_OPPORTUNITIES';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SHOW_SUPERVISED_OPPORTUNITIES', 'ALLOW', 'DR'),
                                                                           ('CRM_SHOW_SUPERVISED_OPPORTUNITIES', 'ALLOW', 'SALESMAN'),
                                                                           ('CRM_SHOW_SUPERVISED_OPPORTUNITIES', 'ALLOW', 'ADMIN');