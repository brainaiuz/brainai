
delete from permission where code='CRM_OPPORTUNITY_LINKS';
insert into permission (code, context, name, sorder, parent, modulecode) values
('CRM_OPPORTUNITY_LINKS', 'CRM', 'Links', 11, (select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),'OPPORTUNITY_TRACKING');

delete from "anv".permission_context where permissioncode = 'CRM_OPPORTUNITY_LINKS';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_OPPORTUNITY_LINKS', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_OPPORTUNITY_LINKS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_OPPORTUNITY_LINKS', 'ALLOW', 'DR'),
                                                                           ('CRM_OPPORTUNITY_LINKS', 'ALLOW', 'ADMIN'),
                                                                           ('CRM_OPPORTUNITY_LINKS', 'ALLOW', 'SALESMAN');
