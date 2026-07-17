insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode) values('MONTHLY_TIMESHEET', 'PM', false, 'Monthly Timesheet', 6, (select id from permission where code ='PM_MAIN_MENU'), false, 'MONTHLY_TIMESHEET');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('MONTHLY_TIMESHEET', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MONTHLY_TIMESHEET', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MONTHLY_TIMESHEET', 'PM','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('MONTHLY_TIMESHEET', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MONTHLY_TIMESHEET', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MONTHLY_TIMESHEET', 'PM','ALLOW');