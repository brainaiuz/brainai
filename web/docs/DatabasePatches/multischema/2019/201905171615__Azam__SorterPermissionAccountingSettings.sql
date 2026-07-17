
delete from permission where code='ACCOUNTING_CONVERSION_BALANCE';

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_CONVERSION_BALANCE';
delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_CONVERSION_BALANCE';

delete from "0".permission_context where permissioncode = 'ACCOUNTING_CONVERSION_BALANCE' and contextcode='SETTINGS';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_CONVERSION_BALANCE' and contextcode='SETTINGS';


delete from permission where code='ACCOUNTING_ADD_ON_SETTINGS';

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_ADD_ON_SETTINGS';
delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_ADD_ON_SETTINGS';

delete from "0".permission_context where permissioncode = 'ACCOUNTING_ADD_ON_SETTINGS' and contextcode='SETTINGS';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_ADD_ON_SETTINGS' and contextcode='SETTINGS';


update permission set  sorder=1, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_INVOICE_SETTINGS';
update permission set  sorder=2, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_FINANCIAL_SETTINGS';
update permission set  sorder=3, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_ACCOUNT_NUMBERING';
update permission set  sorder=4, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_NUMBERING_SETTINGS';
update permission set  sorder=5, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_TAX_RATES_LIST';
update permission set  sorder=6, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_CURRENCY_RATES_LIST';
    update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_CURRENCY_RATES_LIST') where code='ACCOUNTING_CURRENCY_RATE_EDIT';

    delete from "0".permission_context where permissioncode = 'ACCOUNTING_SETTINGSE_EXCHANGE_RATE' and contextcode='ACCOUNTING';
    delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SETTINGSE_EXCHANGE_RATE' and contextcode='ACCOUNTING';
    delete from "0".permission_context where permissioncode = 'ACCOUNTING_SETTINGSE_EXCHANGE_RATE' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SETTINGSE_EXCHANGE_RATE' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_SETTINGSE_EXCHANGE_RATE', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_SETTINGSE_EXCHANGE_RATE', 'SETTINGS');
    update permission set context='SETTINGS', sorder=2, parent=(select id from permission where code='ACCOUNTING_CURRENCY_RATES_LIST') where code='ACCOUNTING_SETTINGSE_EXCHANGE_RATE';

update permission set  sorder=7, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_PRICE_LEVELS_LIST';
update permission set  sorder=8, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_TERMS_LIST';
update permission set  sorder=9, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_DISCOUNTS_LIST';
update permission set  sorder=10, parent=(select id from permission where code='ACCOUNTING_DISCOUNTS_LIST') where code='ACCOUNTING_DISCOUNT_ADD';
update permission set  sorder=11, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_UNIT_MEASUREMENTS_LIST';
update permission set  sorder=12, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_PRODUCT_CATEGORIES_LIST';
update permission set  sorder=13, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_BRANDS_LIST';
update permission set  sorder=14, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_SHIPPING_METHODS_LIST';

delete from "0".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_LIST' and contextcode='ACCOUNTING';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_LIST' and contextcode='ACCOUNTING';
delete from "0".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_LIST' and contextcode='SETTINGS';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_LIST' and contextcode='SETTINGS';

insert into "0".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PAYMENT_METHOD_LIST', 'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PAYMENT_METHOD_LIST', 'SETTINGS');
update permission set context='SETTINGS', name='Payment Methods', sorder=15, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_PAYMENT_METHOD_LIST';

    delete from "0".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_ADD' and contextcode='ACCOUNTING';
    delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_ADD' and contextcode='ACCOUNTING';
    delete from "0".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_ADD' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_ADD' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PAYMENT_METHOD_ADD', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PAYMENT_METHOD_ADD', 'SETTINGS');
    update permission set context='SETTINGS', name='Add', sorder=1, parent=(select id from permission where code='ACCOUNTING_PAYMENT_METHOD_LIST') where code='ACCOUNTING_PAYMENT_METHOD_ADD';

    delete from "0".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_EDIT' and contextcode='ACCOUNTING';
    delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_EDIT' and contextcode='ACCOUNTING';
    delete from "0".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_EDIT' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_EDIT' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PAYMENT_METHOD_EDIT', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PAYMENT_METHOD_EDIT', 'SETTINGS');
    update permission set context='SETTINGS', name='Edit', sorder=2, parent=(select id from permission where code='ACCOUNTING_PAYMENT_METHOD_LIST') where code='ACCOUNTING_PAYMENT_METHOD_EDIT';

    delete from "0".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_DELETE' and contextcode='ACCOUNTING';
    delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_DELETE' and contextcode='ACCOUNTING';
    delete from "0".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_DELETE' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PAYMENT_METHOD_DELETE' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PAYMENT_METHOD_DELETE', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PAYMENT_METHOD_DELETE', 'SETTINGS');
    update permission set context='SETTINGS', name='Delete', sorder=3, parent=(select id from permission where code='ACCOUNTING_PAYMENT_METHOD_LIST') where code='ACCOUNTING_PAYMENT_METHOD_DELETE';

update permission set  sorder=16, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_PRODUCT_TABLE_SETTINGS';
update permission set  sorder=17, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_ACCOUNT_LIST';