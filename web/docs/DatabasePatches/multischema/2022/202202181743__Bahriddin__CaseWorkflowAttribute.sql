update "anv".modelfield
set usablebyworkflow = true
where form_id = 'CASE_FORM'
  and field_id = 'INTERNAL_UPDATED_DATE';

update "anv".modelfield
set usablebyworkflow = true
where form_id = 'CASE_FORM'
  and field_id = 'INTERNAL_STATUS';

update "anv".modelfield
set usablebyworkflow = true
where form_id = 'CASE_FORM'
  and field_id = 'DESCRIPTION';
