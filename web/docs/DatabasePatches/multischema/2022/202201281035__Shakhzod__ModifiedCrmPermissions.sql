update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_CASES_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_TASKS_LIST';