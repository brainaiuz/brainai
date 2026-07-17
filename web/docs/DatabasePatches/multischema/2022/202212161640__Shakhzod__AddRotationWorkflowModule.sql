insert into "anv".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_ROTATION', 'Rotation', (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
        (select id from "anv".reference where code = '_WORKFLOW_MODULE'));
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'ROTATION_FORM'
  and field_id = 'EMPLOYEES';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'ROTATION_FORM'
  and field_id = 'DATE';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'ROTATION_FORM'
  and field_id = 'NUMBER';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'ROTATION_FORM'
  and field_id = 'CURRENT_DEPARTMENT';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'ROTATION_FORM'
  and field_id = 'NEW_DEPARTMENT';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'ROTATION_FORM'
  and field_id = 'CURRENT_POSITION';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'ROTATION_FORM'
  and field_id = 'NEW_POSITION';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'ROTATION_FORM'
  and field_id = 'APPROVERS';