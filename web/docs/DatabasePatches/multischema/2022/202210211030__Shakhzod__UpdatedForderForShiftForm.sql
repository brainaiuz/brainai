update "anv".modelfield
set forder = 0
where field_id = 'monthPicker'
  and form_id = 'SHIFT_FORM';
update "anv".modelfield
set forder = 1
where field_id = 'APPROVERS'
  and form_id = 'SHIFT_FORM';
update "anv".modelfield
set forder = 2
where field_id = 'shift'
  and form_id = 'SHIFT_FORM';
update "anv".modelfield
set forder = 3
where field_id = 'shiftContainer'
  and form_id = 'SHIFT_FORM';