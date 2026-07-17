

insert into "anv".mymodule (code,name,section,active) values ('SOLUTION_MANAGEMENT','Solutions','crm',false) on conflict do nothing;

update permission set  modulecode='SOLUTION_MANAGEMENT'  where code='CRM_SOLUTIONS_LIST';
update permission set  modulecode='SOLUTION_MANAGEMENT'  where code='ADD_NEW_SOLUTION';
update permission set  modulecode='SOLUTION_MANAGEMENT'  where code='CRM_EDIT_SOLUTION';
update permission set  modulecode='SOLUTION_MANAGEMENT'  where code='CRM_REMOVE_SOLUTION';
update permission set  modulecode='SOLUTION_MANAGEMENT'  where code='CRM_SOLUTIONS_EXPORT';