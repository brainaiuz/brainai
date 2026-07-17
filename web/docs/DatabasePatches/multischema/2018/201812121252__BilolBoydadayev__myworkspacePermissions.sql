delete from "anv".rolepermission where permissioncode = 'MYWORKSPACE_MAIN_MENU';
delete from "anv".mymodule where code = 'MYWORKSPACE_MODULE';
delete from "anv".permission_context where permissioncode = 'MYWORKSPACE_MAIN_MENU' and contextcode = 'MYWORKSPACE';
delete from "0".rolepermission where permissioncode = 'MYWORKSPACE_MAIN_MENU';
delete from "0".mymodule where code = 'MYWORKSPACE_MODULE';
delete from "0".permission_context where permissioncode = 'MYWORKSPACE_MAIN_MENU' and contextcode = 'MYWORKSPACE';

delete from context where code='MYWORKSPACE';
delete from permission where modulecode='MYWORKSPACE_MODULE';