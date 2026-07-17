

delete from permission where code='CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE';
delete from permission where code='CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE';
insert into permission (code, context, name, sorder, parent, modulecode) values
('CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE', 'CRM', 'Assignee List Value', 22,(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),'OPPORTUNITY_TRACKING'),
('CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE', 'CRM', 'Backup Assignee LIst Value', 23,(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),'OPPORTUNITY_TRACKING');

delete from "anv".permission_context where permissioncode = 'CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE';
delete from "anv".permission_context where permissioncode = 'CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE', 'CRM'),
                                                                          ('CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE', 'CRM');


delete from "anv".rolepermission where permissioncode = 'CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE';
delete from "anv".rolepermission where permissioncode = 'CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE', 'ALLOW', 'DR'),
                                                                           ('CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE', 'ALLOW', 'SALESMAN'),
                                                                           ('CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE', 'ALLOW', 'SALESPERSON'),
                                                                           ('CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE', 'ALLOW', 'DR'),
                                                                           ('CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE', 'ALLOW', 'SALESMAN'),
                                                                           ('CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE', 'ALLOW', 'SALESPERSON');

