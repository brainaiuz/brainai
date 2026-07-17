insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('SHIFT_FORM', 'OWNER', false, false, 'COL_2', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BRIGADA_FORM', 'OWNER', false, false, 'COL_2', 'BASIC_INFORMATION', 1);

insert into permission (code, context, name, sorder, parent, modulecode)
values ('BRIGADA_OWNER_LIST', 'HRMS', 'Owner List', 6, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('SHIFT_OWNER_LIST', 'HRMS', 'Owner List', 6, (select id from permission where code = 'HRMS_SHIFT'), 'RECRUITMENT_SYSTEM');


insert into "anv".permission_context (permissioncode, contextcode)
values ('BRIGADA_OWNER_LIST', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('SHIFT_OWNER_LIST', 'HRMS');