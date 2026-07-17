delete from permission where code = 'CUSTOM_FORM_2_CUSTOMIZE_FORM';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('CUSTOM_FORM_2_CUSTOMIZE_FORM', 'CRM', false, 'Customize Form', 50,
        (select id from permission where code = 'CRM_MAIN_MENU'), true, 'CRM_MODULE');


delete from "anv".permission_context where permissioncode = 'CUSTOM_FORM_2_CUSTOMIZE_FORM';
insert into "anv".permission_context(permissioncode, contextcode)
values ('CUSTOM_FORM_2_CUSTOMIZE_FORM', 'CRM');