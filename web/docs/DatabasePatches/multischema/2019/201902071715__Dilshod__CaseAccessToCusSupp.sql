
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_CASES_LIST' and rolecode='SUPPLIER';
DELETE FROM "0".rolepermission WHERE  permissioncode='ADD_NEW_CASE' and rolecode='SUPPLIER';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_EDIT_CASE' and rolecode='SUPPLIER';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_REMOVE_CASE' and rolecode='SUPPLIER';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_CHANGE_STATUS_CASE' and rolecode='SUPPLIER';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_CHANGE_PRIORITY_CASE' and rolecode='SUPPLIER';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_CHANGE_ASSIGNEE_CASE' and rolecode='SUPPLIER';

DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_CASES_LIST' and rolecode='CLIENT';
DELETE FROM "0".rolepermission WHERE  permissioncode='ADD_NEW_CASE' and rolecode='CLIENT';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_EDIT_CASE' and rolecode='CLIENT';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_REMOVE_CASE' and rolecode='CLIENT';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_CHANGE_STATUS_CASE' and rolecode='CLIENT';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_CHANGE_PRIORITY_CASE' and rolecode='CLIENT';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_CHANGE_ASSIGNEE_CASE' and rolecode='CLIENT';


DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_CASES_LIST' and rolecode='SUPPLIER';
DELETE FROM "anv".rolepermission WHERE  permissioncode='ADD_NEW_CASE' and rolecode='SUPPLIER';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_EDIT_CASE' and rolecode='SUPPLIER';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_REMOVE_CASE' and rolecode='SUPPLIER';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_CHANGE_STATUS_CASE' and rolecode='SUPPLIER';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_CHANGE_PRIORITY_CASE' and rolecode='SUPPLIER';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_CHANGE_ASSIGNEE_CASE' and rolecode='SUPPLIER';

DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_CASES_LIST' and rolecode='CLIENT';
DELETE FROM "anv".rolepermission WHERE  permissioncode='ADD_NEW_CASE' and rolecode='CLIENT';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_EDIT_CASE' and rolecode='CLIENT';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_REMOVE_CASE' and rolecode='CLIENT';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_CHANGE_STATUS_CASE' and rolecode='CLIENT';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_CHANGE_PRIORITY_CASE' and rolecode='CLIENT';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_CHANGE_ASSIGNEE_CASE' and rolecode='CLIENT';

INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_CASES_LIST','ALLOW','SUPPLIER') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('ADD_NEW_CASE','ALLOW','SUPPLIER') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_EDIT_CASE','ALLOW','SUPPLIER') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_REMOVE_CASE','ALLOW','SUPPLIER') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_CHANGE_STATUS_CASE','ALLOW','SUPPLIER') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_CHANGE_PRIORITY_CASE','ALLOW','SUPPLIER') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_CHANGE_ASSIGNEE_CASE','ALLOW','SUPPLIER') ;

INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_CASES_LIST','ALLOW','CLIENT') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_CASES_LIST','ALLOW','CLIENT') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('ADD_NEW_CASE','ALLOW','CLIENT') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_EDIT_CASE','ALLOW','CLIENT') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_REMOVE_CASE','ALLOW','CLIENT') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_CHANGE_STATUS_CASE','ALLOW','CLIENT') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_CHANGE_PRIORITY_CASE','ALLOW','CLIENT') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_CHANGE_ASSIGNEE_CASE','ALLOW','CLIENT') ;

