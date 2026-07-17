update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_PRODUCT_LIST'),
    name='Add'
where code = 'ACCOUNTING_PRODUCT_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_PRODUCT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PRODUCT_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_PRODUCT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PRODUCT_DELETE';
update permission
set sorder=4,
    parent=(select id from permission where code = 'CRM_PRODUCT_LIST'),
    name='Summary'
where code = 'ACCOUNTING_PRODUCT_SUMMARY';
update permission
set sorder=5,
    parent=(select id from permission where code = 'CRM_PRODUCT_LIST'),
    name='Quick Add'
where code = 'ACCOUNTING_PRODUCT_QUICK_ADD';
update permission
set sorder=6,
    parent=(select id from permission where code = 'CRM_PRODUCT_LIST'),
    name='Inventory'
where code = 'ACCOUNTING_INVENTORY_LIST';