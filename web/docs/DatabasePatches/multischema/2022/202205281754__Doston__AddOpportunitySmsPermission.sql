insert into "anv".permission_context (permissioncode, contextcode)
select 'CRM_OPPORTUNITY_SEND_SMS', 'CRM' where NOT EXISTS (SELECT permissioncode FROM "anv".permission_context where permissioncode='CRM_OPPORTUNITY_SEND_SMS' AND where contextcode='CRM');

insert into permission (code, context, name, sorder, parent, modulecode)
select 'CRM_OPPORTUNITY_SEND_SMS',
       'CRM',
       'Send SMS',
       30,
       83,
       'CRM_MODULE' where NOT EXISTS (SELECT code from permission where code='CRM_OPPORTUNITY_SEND_SMS')