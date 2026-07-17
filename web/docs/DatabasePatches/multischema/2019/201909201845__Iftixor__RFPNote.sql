

update "anv".rfpitem set hasproductlist = true where productid is not null;
update "anv".rfpitem set hasproductlist = false where itemname is not null;


delete from myupdatetype where code='ACCOUNTING_REQUEST_FOR_PURCHASE';
insert into myupdatetype (code, description) values ('ACCOUNTING_REQUEST_FOR_PURCHASE', 'All Request for purchase updates');
insert into myupdatetype (code, description, parentid) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_ADD',     'Records when user has added rfp',     (select id from myupdatetype where code='ACCOUNTING_REQUEST_FOR_PURCHASE'));
insert into myupdatetype (code, description, parentid) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT',    'Records when user has edited rfp',    (select id from myupdatetype where code='ACCOUNTING_REQUEST_FOR_PURCHASE'));
insert into myupdatetype (code, description, parentid) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE',  'Records when user has deleted rfp',   (select id from myupdatetype where code='ACCOUNTING_REQUEST_FOR_PURCHASE'));


UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('PRODUCT', 'ProductServiceView')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('COMPANY_GOAL_FORM', 'CompanyGoal')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('BUSINESS_GOAL_FORM', 'BusinessGoal')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('PROJECT_GOAL_FORM', 'ProjectGoal')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('DEPARTMENT_GOAL_FORM', 'DepartmentGoal')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('PERSONAL_GOAL_FORM', 'PersonalGoal')) WHERE  id=(SELECT id FROM company LIMIT 1);
