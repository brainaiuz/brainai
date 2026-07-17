update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST' limit 1),
    sorder = (
select sorder + 1
from permission
where code = 'CRM_OPPORTUNITY_HISTORY_LIST' limit 1)
where code = 'CRM_OPPORTUNITY_SEND_SMS';
