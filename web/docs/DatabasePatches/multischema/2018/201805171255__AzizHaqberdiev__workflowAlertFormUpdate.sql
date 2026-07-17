update modelfield set nolabelfor = 'addForm,editForm,viewForm' where form_id = 'WORKFLOW_ALERT_FORM' and field_id = 'BCC_CC_PANEL';
update "0".modelfield set nolabelfor = 'addForm,editForm,viewForm' where form_id = 'WORKFLOW_ALERT_FORM' and field_id = 'BCC_CC_PANEL';
update "anv".modelfield set nolabelfor = 'addForm,editForm,viewForm' where form_id = 'WORKFLOW_ALERT_FORM' and field_id = 'BCC_CC_PANEL';


update modelfield set nolabelfor='', nowrapperfor='' where form_id = 'WORKFLOW_ALERT_FORM' and field_id = 'WORKFLOW_TIME_BASED';
update "0".modelfield set nolabelfor='', nowrapperfor='' where form_id = 'WORKFLOW_ALERT_FORM' and field_id = 'WORKFLOW_TIME_BASED';
update "anv".modelfield set nolabelfor='', nowrapperfor='' where form_id = 'WORKFLOW_ALERT_FORM' and field_id = 'WORKFLOW_TIME_BASED';
