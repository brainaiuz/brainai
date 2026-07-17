

delete from permission where code='CRM_OPPORTUNITY_HISTORY_LIST';
insert into permission (code, context, name, sorder, parent, modulecode) values
('CRM_OPPORTUNITY_HISTORY_LIST', 'CRM', 'Log History', 25,(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),'OPPORTUNITY_TRACKING');

delete from "anv".permission_context where permissioncode = 'CRM_OPPORTUNITY_HISTORY_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_OPPORTUNITY_HISTORY_LIST', 'CRM');


delete from "anv".rolepermission where permissioncode = 'CRM_OPPORTUNITY_HISTORY_LIST';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_OPPORTUNITY_HISTORY_LIST', 'ALLOW', 'DR'),
                                                                           ('CRM_OPPORTUNITY_HISTORY_LIST', 'ALLOW', 'SALESMAN'),
                                                                           ('CRM_OPPORTUNITY_HISTORY_LIST', 'ALLOW', 'ADMIN');