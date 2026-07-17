

delete from "anv".budget_manager_settings where budgetManagerid = (select id from "anv".budget_manager where code='PRODUCTS' limit 1);
delete from "anv".budget_manager  where code='PRODUCTS';
delete from "anv".budget_manager_settings where budgetManagerid = (select id from "anv".budget_manager where code='EMPLOYEES' limit 1);
delete from "anv".budget_manager  where code='EMPLOYEES';
delete from "anv".budget_manager_settings where budgetManagerid = (select id from "anv".budget_manager where code='CUSTOMER' limit 1);
delete from "anv".budget_manager  where code='CUSTOMER';
delete from "anv".budget_manager_settings where budgetManagerid = (select id from "anv".budget_manager where code='CHART_OF_ACCOUNT' limit 1);
delete from "anv".budget_manager  where code='CHART_OF_ACCOUNT';