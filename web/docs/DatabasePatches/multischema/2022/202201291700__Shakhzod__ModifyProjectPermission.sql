update permission
set sorder = 1
where code = 'PM_TASKS_LIST';
update permission
set sorder = 2
where code = 'PM_TIMESHEET';
update permission
set sorder = 3
where code = 'PM_TIMESHEET_APPROVAL';
update permission
set sorder = 4
where code = 'PM_PROJECT_LIST';
update permission
set sorder = 5
where code = 'PM_EMPLOYEE_LIST';
update permission
set sorder = 6
where code = 'PM_RESOURCE_UTILIZATION_LIST';
update permission
set sorder = 7
where code = 'PM_CONTRACT_LIST';
update permission
set sorder = 8
where code = 'PM_BOOKING_ITEMS';

update permission
set sorder=1,
    parent=(select id from permission where code = 'PM_TASKS_LIST'),
    name='See All'
where code = 'PM_SHOW_ALL_TASKS';
update permission
set sorder=2,
    parent=(select id from permission where code = 'PM_TASKS_LIST'),
    name='Add'
where code = 'PM_TASKS_ADD';
update permission
set sorder=3,
    parent=(select id from permission where code = 'PM_TASKS_LIST'),
    name='Edit'
where code = 'PM_TASKS_EDIT';
update permission
set sorder=4,
    parent=(select id from permission where code = 'PM_TASKS_LIST'),
    name='Delete'
where code = 'PM_TASKS_REMOVE';


update permission
set sorder=1,
    parent=(select id from permission where code = 'PM_TIMESHEET'),
    name='Approvers'
where code = 'PM_TIMESHEET_APPROVERS';


update permission
set sorder=1,
    parent=(select id from permission where code = 'PM_TIMESHEET_APPROVAL'),
    name='Review'
where code = 'PM_APPROVE_REJECT';
update permission
set sorder=2,
    parent=(select id from permission where code = 'PM_TIMESHEET_APPROVAL'),
    name='Approve/Reject '
where code = 'PM_APPROVE_REJECT_ALL_TIMESHEETS';


update permission
set sorder=1,
    parent=(select id from permission where code = 'PM_PROJECT_LIST'),
    name='See All'
where code = 'PM_SEE_ALL_PROJECTS';
update permission
set sorder=2,
    parent=(select id from permission where code = 'PM_PROJECT_LIST'),
    name='Add'
where code = 'PM_PROJECT_ADD';
update permission
set sorder=3,
    parent=(select id from permission where code = 'PM_PROJECT_LIST'),
    name='Edit'
where code = 'PM_PROJECT_EDIT';
update permission
set sorder=4,
    parent=(select id from permission where code = 'PM_PROJECT_LIST'),
    name='Delete'
where code = 'PM_PROJECT_REMOVE';

update permission
set sorder=1,
    parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'),
    name='See All'
where code = 'PM_SHOW_ALL_EMPLOYEE_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'),
    name='Add'
where code = 'PM_EMPLOYEE_ADD';
update permission
set sorder=3,
    parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'),
    name='Edit'
where code = 'PM_EMPLOYEE_EDIT';
update permission
set sorder=4,
    parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'),
    name='Delete'
where code = 'PM_EMPLOYEE_REMOVE';

update permission
set sorder=1,
    parent=(select id from permission where code = 'PM_CONTRACT_LIST'),
    name='Convert To Project'
where code = 'PM_CONTRACT_CONVERT_TO_PROJECT';
update permission
set sorder=2,
    parent=(select id from permission where code = 'PM_CONTRACT_LIST'),
    name='Add/Edit'
where code = 'PM_CONTRACT_ADD_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'PM_CONTRACT_LIST'),
    name='Reminder Receivers'
where code = 'PM_CONTRACT_REMINDER';
update permission
set sorder=4,
    parent=(select id from permission where code = 'PM_CONTRACT_LIST'),
    name='Delete'
where code = 'PM_CONTRACT_DELETE';


update permission
set sorder=1,
    parent=(select id from permission where code = 'PM_BOOKING_ITEMS'),
    name='Add'
where code = 'PM_BOOKING_ITEMS_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'PM_BOOKING_ITEMS'),
    name='Edit'
where code = 'PM_BOOKING_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'PM_BOOKING_ITEMS'),
    name='Add Reservation'
where code = 'PM_ADD_RESERVATION';