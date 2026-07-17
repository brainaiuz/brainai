
delete from permission where code='CRM_LEADS_LIST_CUSTOMIZE_BUTTON';
insert into permission (code, context, name, sorder, parent, modulecode) values
('CRM_LEADS_LIST_CUSTOMIZE_BUTTON', 'CRM', 'Customize List', 11, (select id from permission where code = 'CRM_LEADS_LIST'),'LEAD_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'CRM_LEADS_LIST_CUSTOMIZE_BUTTON';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_LEADS_LIST_CUSTOMIZE_BUTTON', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_LEADS_LIST_CUSTOMIZE_BUTTON';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_LEADS_LIST_CUSTOMIZE_BUTTON', 'ALLOW', 'DR'),
                                                                           ('CRM_LEADS_LIST_CUSTOMIZE_BUTTON', 'ALLOW', 'ADMIN'),
                                                                           ('CRM_LEADS_LIST_CUSTOMIZE_BUTTON', 'ALLOW', 'SALESMAN'),
                                                                           ('CRM_LEADS_LIST_CUSTOMIZE_BUTTON', 'ALLOW', 'CUSTOMER_SERVICE_REPRESENTATIVE'),
                                                                           ('CRM_LEADS_LIST_CUSTOMIZE_BUTTON', 'ALLOW', 'CUSTOMER_SERVICE_MANAGER'),
                                                                           ('CRM_LEADS_LIST_CUSTOMIZE_BUTTON', 'ALLOW', 'ACCOUNTANT');