update "anv".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_CREATE' where executionCriteria = 'ON_CREATE';
update "anv".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_EDIT' where executionCriteria = 'ON_EDIT';
update "anv".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT' where executionCriteria = 'ON_CREATE_EDIT';
update "anv".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_REMOVE' where executionCriteria = 'ON_DELETE';
update "anv".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_UPDATE_SPECIFIED_FIELD' where executionCriteria = 'ON_UPDATE_FIELD';
update "anv".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_RECURRENCE' where executionCriteria = 'RECURRENCE';
update "anv".workflowrule set executionCriteria = '_WORKFLOW_ACTION_APPROVING' where executionCriteria = 'ON_APPROVE_REJECT';

update "anv".workflowrule set active = true where status = '_WORKFLOW_STATUS_ACTIVE';

update "0".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_CREATE' where executionCriteria = 'ON_CREATE';
update "0".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_EDIT' where executionCriteria = 'ON_EDIT';
update "0".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT' where executionCriteria = 'ON_CREATE_EDIT';
update "0".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_REMOVE' where executionCriteria = 'ON_DELETE';
update "0".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_UPDATE_SPECIFIED_FIELD' where executionCriteria = 'ON_UPDATE_FIELD';
update "0".workflowrule set executionCriteria = '_WORKFLOW_EXECUTION_CRITERIA_RECURRENCE' where executionCriteria = 'RECURRENCE';
update "0".workflowrule set executionCriteria = '_WORKFLOW_ACTION_APPROVING' where executionCriteria = 'ON_APPROVE_REJECT';

update "0".workflowrule set active = true where status = '_WORKFLOW_STATUS_ACTIVE';
