CREATE function setDefaultExpensePermissions(permissioncode text,rolecode text) returns void
LANGUAGE plpgsql
AS $$
DECLARE tblcopy text;
DECLARE cc INTEGER;
BEGIN
  FOR tblcopy in (select comp.id  from company comp inner join pg_namespace ns on comp.id = cast(ns.nspname as int4) where nspname ~ '^[0-9]')
  LOOP
    RAISE NOTICE 'Schema name=%', tblcopy;
    EXECUTE ('SELECT count(id) FROM "'||tblcopy||'".rolepermission where permissioncode='''||permissioncode||''' and rolecode='''||rolecode||'''') into cc;
    if cc is null or cc = 0 THEN
      RAISE NOTICE 'Record not found for schema %', tblcopy;
      EXECUTE ('insert into "'||tblcopy||'".rolepermission (permissioncode, rolecode, access) values('''||permissioncode||''', '''||rolecode||''', ''ALLOW'')');
    END IF;
  END LOOP;
END;
$$;


DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_EXPENSE_REPORT_LIST';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'MEM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'PM', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_EXPENSE_REPORT_ADD';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'MEM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'PM', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_EXPENSE_REPORT_EDIT';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'MEM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'PM', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_EXPENSE_REPORT_DELETE';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_DELETE', 'ACCOUNTANT', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_EXPENSE_REPORT_VOID';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'MEM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'PM', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_EXPENSE_REPORT_SUMMARY';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_SUMMARY', 'MEM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_SUMMARY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_SUMMARY', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_SUMMARY', 'PM', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'TL', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'PM', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'ACCOUNTANT', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_ACCOUNTANT_APPROVAL_EXPENCE_CLAIM';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ACCOUNTANT_APPROVAL_EXPENCE_CLAIM', 'ACCOUNTANT', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'PM', 'ALLOW');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_EXPENSE_FULL_LIST_ACCESS';
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'ACCOUNTANT', 'ALLOW');


--for all schema

select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_LIST', 'MEM');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_LIST', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_LIST', 'ACCOUNTANT');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_LIST', 'PM');

select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_ADD', 'MEM');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_ADD', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_ADD', 'ACCOUNTANT');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_ADD', 'PM');

select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_EDIT', 'MEM');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_EDIT', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_EDIT', 'ACCOUNTANT');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_EDIT', 'PM');

select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_DELETE', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_DELETE', 'ACCOUNTANT');

select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_VOID', 'MEM');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_VOID', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_VOID', 'ACCOUNTANT');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_VOID', 'PM');

select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_SUMMARY', 'MEM');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_SUMMARY', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_SUMMARY', 'ACCOUNTANT');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_SUMMARY', 'PM');

select setDefaultExpensePermissions('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'ACCOUNTANT');
select setDefaultExpensePermissions('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'TL');
select setDefaultExpensePermissions('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'PM');

select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'ACCOUNTANT');

select setDefaultExpensePermissions('ACCOUNTING_ACCOUNTANT_APPROVAL_EXPENCE_CLAIM', 'ACCOUNTANT');

select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'ACCOUNTANT');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'PM');

select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'DR');
select setDefaultExpensePermissions('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'ACCOUNTANT');
