update "anv".modelfield
set usablebyworkflow    = false,
    isworkflowattribute = false
where field_id = 'CRM_OPPORTUNITY_ASSIGNEE';

update "anv".modelfield
set usablebyworkflow    = false,
    isworkflowattribute = false
where field_id = 'CRM_OPPORTUNITY_NAME';

update "anv".modelfield
set disableupdate = false
where field_id = 'CRM_OPPORTUNITY_STAGE';
