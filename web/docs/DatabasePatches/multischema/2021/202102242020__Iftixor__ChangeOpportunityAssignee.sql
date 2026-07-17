

delete from permission where code='CHANGE_OPPORTUNITY_ASSIGNEE';
insert into permission (code, context, name, sorder, parent, modulecode) values
('CHANGE_OPPORTUNITY_ASSIGNEE', 'CRM', 'Change Assignee', 24,(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),'OPPORTUNITY_TRACKING');

delete from "anv".permission_context where permissioncode = 'CHANGE_OPPORTUNITY_ASSIGNEE';
insert into "anv".permission_context (permissioncode, contextcode) values ('CHANGE_OPPORTUNITY_ASSIGNEE', 'CRM');


delete from "anv".rolepermission where permissioncode = 'CHANGE_OPPORTUNITY_ASSIGNEE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CHANGE_OPPORTUNITY_ASSIGNEE', 'ALLOW', 'DR'),
                                                                           ('CHANGE_OPPORTUNITY_ASSIGNEE', 'ALLOW', 'SALESMAN'),
                                                                           ('CHANGE_OPPORTUNITY_ASSIGNEE', 'ALLOW', 'ADMIN');

