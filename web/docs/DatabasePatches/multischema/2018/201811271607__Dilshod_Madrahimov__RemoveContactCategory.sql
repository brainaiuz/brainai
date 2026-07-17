
DELETE from "0".permission_context WHERE permissioncode in('CRM_REMOVE_CATEGORY','CRM_SHARE_CATEGORY','CRM_EDIT_CATEGORY','CRM_ADD_NEW_CATEGORY');
DELETE from "anv".permission_context WHERE permissioncode in('CRM_REMOVE_CATEGORY','CRM_SHARE_CATEGORY','CRM_EDIT_CATEGORY','CRM_ADD_NEW_CATEGORY');

delete from permission where code = 'CRM_REMOVE_CATEGORY';
delete from "0".rolepermission where permissioncode = 'CRM_REMOVE_CATEGORY';
delete from "anv".rolepermission where permissioncode = 'CRM_REMOVE_CATEGORY';

delete from permission where code = 'CRM_SHARE_CATEGORY';
delete from "0".rolepermission where permissioncode = 'CRM_SHARE_CATEGORY';
delete from "anv".rolepermission where permissioncode = 'CRM_SHARE_CATEGORY';

delete from permission where code = 'CRM_EDIT_CATEGORY';
delete from "0".rolepermission where permissioncode = 'CRM_EDIT_CATEGORY';
delete from "anv".rolepermission where permissioncode = 'CRM_EDIT_CATEGORY';

delete from permission where code = 'CRM_ADD_NEW_CATEGORY';
delete from "0".rolepermission where permissioncode = 'CRM_ADD_NEW_CATEGORY';
delete from "anv".rolepermission where permissioncode = 'CRM_ADD_NEW_CATEGORY';

