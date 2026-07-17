update "anv".customformsection
set sorder = 1
where form_id = 'ASSESSMENT_FORM'
  and section = 'ASSIGNED_GOALS';
update "anv".customformsection
set sorder = 2
where form_id = 'ASSESSMENT_FORM'
  and section = 'EMPLOYEE_COMPETENCIES';