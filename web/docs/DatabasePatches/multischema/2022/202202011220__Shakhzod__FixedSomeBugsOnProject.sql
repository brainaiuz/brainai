insert into permission (code, context, name, sorder, parent, modulecode)
values ('PM_CUSTOMER_LIST', 'PM', 'Customer Center', 8, 20, 'CUSTOMER_CENTER');


update permission
set sorder=1,
    parent=(select id from permission where code = 'PM_CUSTOMER_LIST'),
    name='Add'
where code = 'PM_CUSTOMER_ADD_CLIENT';
update permission
set sorder=2,
    parent=(select id from permission where code = 'PM_CUSTOMER_LIST'),
    name='Edit'
where code = 'PM_CUSTOMER_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'PM_CUSTOMER_LIST'),
    name='Delete'
where code = 'PM_CUSTOMER_REMOVE_CLIENT';
update permission
set sorder=4,
    parent=(select id from permission where code = 'PM_CUSTOMER_LIST'),
    name='Import'
where code = 'PM_CUSTOMER_IMPORT';
update permission
set sorder=5,
    parent=(select id from permission where code = 'PM_CUSTOMER_LIST'),
    name='Export'
where code = 'PM_CUSTOMER_EXPORT';
