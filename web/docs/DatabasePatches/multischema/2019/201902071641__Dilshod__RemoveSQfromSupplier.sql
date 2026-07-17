---Remove SQ from supplier
DELETE from "0".rolepermission where permissioncode='CRM_SALES_QUOTE_LIST' and rolecode='SUPPLIER';
DELETE from "0".rolepermission where permissioncode='ACCOUNTING_SALES_QUOTE_LIST' and rolecode='SUPPLIER';
DELETE from "0".rolepermission where permissioncode='PM_SALES_QUOTE_LIST' and rolecode='SUPPLIER';
DELETE from "0".rolepermission where permissioncode='ACCOUNTING_SALES_ORDER_LIST' and rolecode='SUPPLIER';

DELETE from "anv".rolepermission where permissioncode='CRM_SALES_QUOTE_LIST' and rolecode='SUPPLIER';
DELETE from "anv".rolepermission where permissioncode='ACCOUNTING_SALES_QUOTE_LIST' and rolecode='SUPPLIER';
DELETE from "anv".rolepermission where permissioncode='PM_SALES_QUOTE_LIST' and rolecode='SUPPLIER';
DELETE from "anv".rolepermission where permissioncode='ACCOUNTING_SALES_ORDER_LIST' and rolecode='SUPPLIER';