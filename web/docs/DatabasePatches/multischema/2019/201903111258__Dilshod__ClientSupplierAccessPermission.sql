

delete from  "0".rolepermission where permissioncode='CRM_MESSAGE_CENTER' and rolecode in('CLIENT','SUPPLIER');
delete from  "0".rolepermission where permissioncode='WORKSPACE_MAIN_MENU' and rolecode in('CLIENT','SUPPLIER');

delete from  "anv".rolepermission where permissioncode='CRM_MESSAGE_CENTER' and rolecode in('CLIENT','SUPPLIER');
delete from  "anv".rolepermission where permissioncode='WORKSPACE_MAIN_MENU' and rolecode in('CLIENT','SUPPLIER');
