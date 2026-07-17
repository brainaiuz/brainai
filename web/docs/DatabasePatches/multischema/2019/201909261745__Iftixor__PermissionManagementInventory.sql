
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') where code='ACCOUNTING_INVENTORY_LIST';
