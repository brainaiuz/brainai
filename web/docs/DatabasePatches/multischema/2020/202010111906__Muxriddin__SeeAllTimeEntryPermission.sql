delete from permission where code='TIME_ENTRIES_LIST';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
values('SEE_ALL_TIME_ENTRIES','PM', 'See All Time Entries', 13,
	   (select id from permission where code='PM_TASKS_LIST'), 'TASK_MANAGEMENT');

delete from "anv".permission_context where permissioncode='TIME_ENTRIES_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('SEE_ALL_TIME_ENTRIES', 'PM');

delete from "anv".rolepermission where permissioncode='TIME_ENTRIES_LIST';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('SEE_ALL_TIME_ENTRIES', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('SEE_ALL_TIME_ENTRIES', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('SEE_ALL_TIME_ENTRIES', 'ALLOW', 'PM');
