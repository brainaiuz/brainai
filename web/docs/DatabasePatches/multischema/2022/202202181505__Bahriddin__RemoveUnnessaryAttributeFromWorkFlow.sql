update "anv".modelfield
set usablebyworkflow = false
where form_id = 'CANDIDATE_FORM'
  and field_id = 'ATTACHMENTS';
