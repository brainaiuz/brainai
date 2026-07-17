delete from "anv".permission_context where permissioncode = 'CRM_CONTACT_LOOK_UP' and contextcode='ACCOUNTING';

insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_CONTACT_LOOK_UP', 'ACCOUNTING');