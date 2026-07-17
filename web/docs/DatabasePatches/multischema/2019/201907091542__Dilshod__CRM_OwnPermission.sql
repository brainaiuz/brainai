---CONTACT_SEE_OWN
delete from permission where code='CONTACT_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('CONTACT_SEE_OWN',
                                                                            'CRM',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='CRM_CONTACTS_LIST')),
                                                                             (select id from permission where code='CRM_CONTACTS_LIST'),
                                                                             'CONTACT_MANAGEMENT'
                                                                            );

delete from "anv".rolepermission where permissioncode='CONTACT_SEE_OWN';
delete from "anv".permission_context where permissioncode='CONTACT_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('CONTACT_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('CONTACT_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CONTACT_SEE_OWN','ALLOW','ADMIN');


---ACTIVITY_SEE_OWN
delete from permission where code='ACTIVITY_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACTIVITY_SEE_OWN',
                                                                            'CRM',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='CRM_ACTIVITIES_LIST')),
                                                                             (select id from permission where code='CRM_ACTIVITIES_LIST'),
                                                                             'ACTIVITIES'
                                                                            );

delete from "anv".rolepermission where permissioncode='ACTIVITY_SEE_OWN';
delete from "anv".permission_context where permissioncode='ACTIVITY_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACTIVITY_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('ACTIVITY_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACTIVITY_SEE_OWN','ALLOW','ADMIN');



---OPPORTUNITY_SEE_OWN
delete from permission where code='OPPORTUNITY_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('OPPORTUNITY_SEE_OWN',
                                                                            'CRM',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST')),
                                                                             (select id from permission where code='CRM_OPPORTUNITIES_LIST'),
                                                                             'OPPORTUNITY_TRACKING'
                                                                            );

delete from "anv".rolepermission where permissioncode = 'OPPORTUNITY_SEE_OWN';
delete from "anv".permission_context where permissioncode='OPPORTUNITY_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('OPPORTUNITY_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('OPPORTUNITY_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('OPPORTUNITY_SEE_OWN','ALLOW','ADMIN');





---------------- ZERO ------------------------------------



----CONTACT_SEE_OWN

delete from "0".rolepermission where permissioncode='CONTACT_SEE_OWN';
delete from "0".permission_context where permissioncode='CONTACT_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('CONTACT_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('CONTACT_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('CONTACT_SEE_OWN','ALLOW','ADMIN');


---ACTIVITY_SEE_OWN

delete from "0".rolepermission where permissioncode='ACTIVITY_SEE_OWN';
delete from "0".permission_context where permissioncode='ACTIVITY_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('ACTIVITY_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('ACTIVITY_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('ACTIVITY_SEE_OWN','ALLOW','ADMIN');



---OPPORTUNITY_SEE_OWN

delete from "0".rolepermission where permissioncode = 'OPPORTUNITY_SEE_OWN';
delete from "0".permission_context where permissioncode='OPPORTUNITY_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('OPPORTUNITY_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('OPPORTUNITY_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('OPPORTUNITY_SEE_OWN','ALLOW','ADMIN');















