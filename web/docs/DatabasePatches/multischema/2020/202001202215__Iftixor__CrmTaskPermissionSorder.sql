

update permission set sorder=3,parent = (select id from permission where code = 'CUSTOMER_SERVICE_TAB') where code='CRM_TASKS_LIST';