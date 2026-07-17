update "anv".modelfield set usablebyworkflow = true where form_id = 'TASK_MAX_FORM' and field_id = 'ASSIGNEE';

update "anv".modelfield set source = 'CRM@EMPLOYEE' where form_id = 'TASK_MAX_FORM' and field_id = 'ASSIGNEE';