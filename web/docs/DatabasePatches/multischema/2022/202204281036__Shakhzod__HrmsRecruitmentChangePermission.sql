update permission
set context = 'HRMS',
    parent  = (select id from permission where code = 'HRMS_ACTIVITIES_VIEW')
where code = 'ACTIVITY_SEE_OWN';
