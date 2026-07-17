update "anv".modelfield
set usablebyworkflow = true
where form_id = 'SHIFT_FORM'
  and field_id in ('TYPE', 'BACKUP_MANAGER', 'MANAGER', 'OWNER');

update "anv".modelfield
set source = 'Shift-:-Duty-:-Overtime',
    widget = 'LOOKUP'
where form_id = 'SHIFT_FORM'
  and field_id = 'TYPE';
update "anv".modelfield
set source = 'CRM@EMPLOYEE',
    widget = 'LOOKUP'
where form_id = 'SHIFT_FORM'
  and field_id = 'OWNER';