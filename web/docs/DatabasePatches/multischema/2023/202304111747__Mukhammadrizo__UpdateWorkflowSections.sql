
 update "anv".customformsection set expanded = true where form_id = 'WORKFLOW_FORM';

  update "anv".modelfield set usablebyworkflow = true where form_ID = 'OPPORTUNITY_FORM' and field_id = 'CRM_OPPORTUNITY_ASSIGNEE';
