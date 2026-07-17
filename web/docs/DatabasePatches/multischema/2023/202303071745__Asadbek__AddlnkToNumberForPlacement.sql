delete from permission where code='HRMS_SUMMARY_PLACEMENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values('HRMS_SUMMARY_PLACEMENT', 'HRMS', 'Placement Summary', 3,(select id from permission where code = 'HRMS_PLACEMENT_LIST_VIEW'), 'HRMS_MODULE');

delete from "anv".permission_context where permissioncode = 'HRMS_SUMMARY_PLACEMENT';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_SUMMARY_PLACEMENT', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_SUMMARY_PLACEMENT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_SUMMARY_PLACEMENT', 'ALLOW', 'ADMIN'),
                                                                           ('HRMS_SUMMARY_PLACEMENT', 'ALLOW', 'DR'),
                                                                           ('HRMS_SUMMARY_PLACEMENT', 'ALLOW', 'MEM');