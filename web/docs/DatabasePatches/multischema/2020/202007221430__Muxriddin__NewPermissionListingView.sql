INSERT INTO permission (code, context, name, sorder, parent, modulecode)
values('PM_TASK_LIST_VIEW','PM', 'List view', 12,
	   (select id from permission where code='PM_TASKS_LIST'), 'TASK_MANAGEMENT');

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
values('CRM_LEAD_LIST_VIEW','CRM', 'List view', 17,
	   (select id from permission where code='CRM_LEADS_LIST'), 'LEAD_MANAGEMENT');

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
values('CRM_OPPORTUNITY_LIST_VIEW','CRM', 'List view', 20,
	   (select id from permission where code='CRM_OPPORTUNITIES_LIST'), 'OPPORTUNITY_TRACKING');

insert into "anv".permission_context (permissioncode, contextcode) values ('PM_TASK_LIST_VIEW', 'PM');
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_LEAD_LIST_VIEW', 'CRM');
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_OPPORTUNITY_LIST_VIEW', 'CRM');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PM_TASK_LIST_VIEW', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PM_TASK_LIST_VIEW', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PM_TASK_LIST_VIEW', 'ALLOW', 'ACCOUNTANT');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PM_TASK_LIST_VIEW', 'ALLOW', 'PM');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('PM_TASK_LIST_VIEW', 'ALLOW', 'MEM');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_LEAD_LIST_VIEW', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_LEAD_LIST_VIEW', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_LEAD_LIST_VIEW', 'ALLOW', 'ACCOUNTANT');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_LEAD_LIST_VIEW', 'ALLOW', 'SALESMAN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_LEAD_LIST_VIEW', 'ALLOW', 'MEM');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_OPPORTUNITY_LIST_VIEW', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_OPPORTUNITY_LIST_VIEW', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_OPPORTUNITY_LIST_VIEW', 'ALLOW', 'ACCOUNTANT');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_OPPORTUNITY_LIST_VIEW', 'ALLOW', 'SALESMAN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_OPPORTUNITY_LIST_VIEW', 'ALLOW', 'MEM');

