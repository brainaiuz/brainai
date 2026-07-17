update "anv".modelfield
set usablebyworkflow = true
where form_id = 'GROUP_PLACEMENT_FORM'
  and field_id = 'DATE';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'GROUP_PLACEMENT_FORM'
  and field_id = 'APPROVERS';
update "anv".modelfield
set type = 'Date'
where form_id = 'GROUP_PLACEMENT_FORM'
  and field_id = 'DATE';

