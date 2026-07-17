update permission set context='SETTINGS', sorder=8, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='PAYROLL_SETTINGS';

  delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_EMPLOYER_SETTINGS' and contextcode='PAYROLL';
  delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_EMPLOYER_SETTINGS' and contextcode='PAYROLL';
  delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_EMPLOYER_SETTINGS' and contextcode='SETTINGS';
  delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_EMPLOYER_SETTINGS' and contextcode='SETTINGS';

  insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_EMPLOYER_SETTINGS', 'SETTINGS');
  insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_EMPLOYER_SETTINGS', 'SETTINGS');
  update permission set context='SETTINGS', sorder=1, parent=(select id from permission where code='PAYROLL_SETTINGS') where code='PAYROLL_SETTINGS_EMPLOYER_SETTINGS';

  delete from "0".permission_context where permissioncode = 'PAYROLL_GROUP_LIST' and contextcode='PAYROLL';
  delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_LIST' and contextcode='PAYROLL';
  delete from "0".permission_context where permissioncode = 'PAYROLL_GROUP_LIST' and contextcode='SETTINGS';
  delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_LIST' and contextcode='SETTINGS';

  insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_GROUP_LIST', 'SETTINGS');
  insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_GROUP_LIST', 'SETTINGS');
  update permission set context='SETTINGS', name='Payroll Groups List', sorder=2, parent=(select id from permission where code='PAYROLL_SETTINGS') where code='PAYROLL_GROUP_LIST';

    delete from "0".permission_context where permissioncode = 'PAYROLL_GROUP_FULL_ACCESS' and contextcode='PAYROLL';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_FULL_ACCESS' and contextcode='PAYROLL';
    delete from "0".permission_context where permissioncode = 'PAYROLL_GROUP_FULL_ACCESS' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_FULL_ACCESS' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_GROUP_FULL_ACCESS', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_GROUP_FULL_ACCESS', 'SETTINGS');
    update permission set context='SETTINGS', name='Full Access', sorder=1, parent=(select id from permission where code='PAYROLL_GROUP_LIST') where code='PAYROLL_GROUP_FULL_ACCESS';

    delete from "0".permission_context where permissioncode = 'PAYROLL_GROUP_ADD' and contextcode='PAYROLL';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_ADD' and contextcode='PAYROLL';
    delete from "0".permission_context where permissioncode = 'PAYROLL_GROUP_ADD' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_ADD' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_GROUP_ADD', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_GROUP_ADD', 'SETTINGS');
    update permission set context='SETTINGS', name='Add', sorder=2, parent=(select id from permission where code='PAYROLL_GROUP_LIST') where code='PAYROLL_GROUP_ADD';

    delete from "0".permission_context where permissioncode = 'PAYROLL_GROUP_DELETE' and contextcode='PAYROLL';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_DELETE' and contextcode='PAYROLL';
    delete from "0".permission_context where permissioncode = 'PAYROLL_GROUP_DELETE' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_DELETE' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_GROUP_DELETE', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_GROUP_DELETE', 'SETTINGS');
    update permission set context='SETTINGS', name='Delete', sorder=3, parent=(select id from permission where code='PAYROLL_GROUP_LIST') where code='PAYROLL_GROUP_DELETE';

 delete from "0".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_LIST' and contextcode='PAYROLL';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_LIST' and contextcode='PAYROLL';
 delete from "0".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_LIST' and contextcode='SETTINGS';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_LIST' and contextcode='SETTINGS';

 insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_PAYMENT_DEDUCATION_LIST', 'SETTINGS');
 insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_PAYMENT_DEDUCATION_LIST', 'SETTINGS');
 update permission set context='SETTINGS', name='Global Payroll Settings List', sorder=3, parent=(select id from permission where code='PAYROLL_SETTINGS') where code='PAYROLL_PAYMENT_DEDUCATION_LIST';

  delete from "0".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_ADD' and contextcode='PAYROLL';
  delete from "anv".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_ADD' and contextcode='PAYROLL';
  delete from "0".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_ADD' and contextcode='SETTINGS';
  delete from "anv".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_ADD' and contextcode='SETTINGS';

  insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_PAYMENT_DEDUCATION_ADD', 'SETTINGS');
  insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_PAYMENT_DEDUCATION_ADD', 'SETTINGS');
  update permission set context='SETTINGS', name='Add', sorder=1, parent=(select id from permission where code='PAYROLL_PAYMENT_DEDUCATION_LIST') where code='PAYROLL_PAYMENT_DEDUCATION_ADD';

  delete from "0".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_VIEW' and contextcode='PAYROLL';
  delete from "anv".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_VIEW' and contextcode='PAYROLL';
  delete from "0".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_VIEW' and contextcode='SETTINGS';
  delete from "anv".permission_context where permissioncode = 'PAYROLL_PAYMENT_DEDUCATION_VIEW' and contextcode='SETTINGS';

  insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_PAYMENT_DEDUCATION_VIEW', 'SETTINGS');
  insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_PAYMENT_DEDUCATION_VIEW', 'SETTINGS');
  update permission set context='SETTINGS', name='View', sorder=2, parent=(select id from permission where code='PAYROLL_PAYMENT_DEDUCATION_LIST') where code='PAYROLL_PAYMENT_DEDUCATION_VIEW';

 delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_PENSION_PROVIDERS' and contextcode='PAYROLL';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_PENSION_PROVIDERS' and contextcode='PAYROLL';
 delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_PENSION_PROVIDERS' and contextcode='SETTINGS';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_PENSION_PROVIDERS' and contextcode='SETTINGS';

 insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_PENSION_PROVIDERS', 'SETTINGS');
 insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_PENSION_PROVIDERS', 'SETTINGS');
 update permission set context='SETTINGS', sorder=4, parent=(select id from permission where code='PAYROLL_SETTINGS') where code='PAYROLL_SETTINGS_PENSION_PROVIDERS';

 delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_PENSION_SCHEMES' and contextcode='PAYROLL';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_PENSION_SCHEMES' and contextcode='PAYROLL';
 delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_PENSION_SCHEMES' and contextcode='SETTINGS';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_PENSION_SCHEMES' and contextcode='SETTINGS';

 insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_PENSION_SCHEMES', 'SETTINGS');
 insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_PENSION_SCHEMES', 'SETTINGS');
 update permission set context='SETTINGS', name='Pension Scheme', sorder=5, parent=(select id from permission where code='PAYROLL_SETTINGS') where code='PAYROLL_SETTINGS_PENSION_SCHEMES';

 delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_PAYMENT_CATEGORIES' and contextcode='PAYROLL';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_PAYMENT_CATEGORIES' and contextcode='PAYROLL';
 delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_PAYMENT_CATEGORIES' and contextcode='SETTINGS';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_PAYMENT_CATEGORIES' and contextcode='SETTINGS';

 insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_PAYMENT_CATEGORIES', 'SETTINGS');
 insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_PAYMENT_CATEGORIES', 'SETTINGS');
 update permission set context='SETTINGS', sorder=6, parent=(select id from permission where code='PAYROLL_SETTINGS') where code='PAYROLL_SETTINGS_PAYMENT_CATEGORIES';

 delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES' and contextcode='PAYROLL';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES' and contextcode='PAYROLL';
 delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES' and contextcode='SETTINGS';
 delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES' and contextcode='SETTINGS';

 insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_DEDUCATION_CATEGORIES', 'SETTINGS');
 insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_DEDUCATION_CATEGORIES', 'SETTINGS');
 update permission set context='SETTINGS', sorder=7, parent=(select id from permission where code='PAYROLL_SETTINGS') where code='PAYROLL_SETTINGS_DEDUCATION_CATEGORIES';

    delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_ADD' and contextcode='PAYROLL';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_ADD' and contextcode='PAYROLL';
    delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_ADD' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_ADD' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_ADD', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_ADD', 'SETTINGS');
    update permission set context='SETTINGS', name='Add', sorder=1, parent=(select id from permission where code='PAYROLL_SETTINGS_DEDUCATION_CATEGORIES') where code='PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_ADD';

    delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_EDIT' and contextcode='PAYROLL';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_EDIT' and contextcode='PAYROLL';
    delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_EDIT' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_EDIT' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_EDIT', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_EDIT', 'SETTINGS');
    update permission set context='SETTINGS', name='Edit', sorder=2, parent=(select id from permission where code='PAYROLL_SETTINGS_DEDUCATION_CATEGORIES') where code='PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_EDIT';

    delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_DELETE' and contextcode='PAYROLL';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_DELETE' and contextcode='PAYROLL';
    delete from "0".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_DELETE' and contextcode='SETTINGS';
    delete from "anv".permission_context where permissioncode = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_DELETE' and contextcode='SETTINGS';

    insert into "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_DELETE', 'SETTINGS');
    insert into "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_DELETE', 'SETTINGS');
    update permission set context='SETTINGS', name='Delete', sorder=3, parent=(select id from permission where code='PAYROLL_SETTINGS_DEDUCATION_CATEGORIES') where code='PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_DELETE';

update permission set sorder=9  where code='SETTINGS_DASHBOARD_LIST';
update permission set sorder=10 where code='CUSTOM_FIELD_SETTINGS';
update permission set sorder=11 where code='SETTINGS_RECURRENCE_SETTINGS';
update permission set sorder=12 where code='SETTINGS_WORKFLOW';
update permission set sorder=13 where code='ADD_SYSTEM_FILTER';
update permission set sorder=14 where code='SETTINGS_EMAIL_SETTINGS';
update permission set sorder=15 where code='REFERENCE_LIST';