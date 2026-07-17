


delete from permission where code='PERMISSION_LOGS';
insert into permission (code,context,name,sorder,parent,modulecode) values ('PERMISSION_LOGS',
                                                                            'SETTINGS',
                                                                            'Pemission Logs',
                                                                            1,
                                                                            (select id from permission where code='SYSTEM_LOGS'),
                                                                            'CORE'
                                                                           );

delete from "anv".rolepermission where permissioncode='PERMISSION_LOGS';
delete from "anv".permission_context where permissioncode='PERMISSION_LOGS';

insert into "anv".permission_context (permissioncode,contextcode) values ('PERMISSION_LOGS','SETTINGS');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('PERMISSION_LOGS','ALLOW','ADMIN');
