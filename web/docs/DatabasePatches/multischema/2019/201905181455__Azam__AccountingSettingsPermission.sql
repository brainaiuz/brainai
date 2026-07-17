
update permission set context='SETTINGS' where code='ACCOUNTING_DISCOUNTS_LIST';
update permission set context='SETTINGS' where code='ACCOUNTING_DISCOUNT_ADD';

delete from permission where code='ACCOUNTING_DISCOUNT_EDIT';
delete from permission where code='ACCOUNTING_DISCOUNT_DELETE';