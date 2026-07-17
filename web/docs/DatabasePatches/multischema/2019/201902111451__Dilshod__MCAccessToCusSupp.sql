
--Zero
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_MESSAGE_CENTER' and rolecode='SUPPLIER';
DELETE FROM "0".rolepermission WHERE  permissioncode='CRM_MESSAGE_CENTER' and rolecode='CLIENT';

INSERT INTO "0".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_MESSAGE_CENTER','ALLOW','SUPPLIER') ;
INSERT INTO "0".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_MESSAGE_CENTER','ALLOW','CLIENT') ;

--WORKSPACE
DELETE FROM "0".rolepermission WHERE  permissioncode='WORKSPACE_MAIN_MENU' and rolecode='SUPPLIER';
DELETE FROM "0".rolepermission WHERE  permissioncode='WORKSPACE_MAIN_MENU' and rolecode='CLIENT';

INSERT INTO "0".rolepermission(permissioncode,access,rolecode) VALUES ('WORKSPACE_MAIN_MENU','ALLOW','SUPPLIER') ;
INSERT INTO "0".rolepermission(permissioncode,access,rolecode) VALUES ('WORKSPACE_MAIN_MENU','ALLOW','CLIENT') ;



--MC
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_MESSAGE_CENTER' and rolecode='SUPPLIER';
DELETE FROM "anv".rolepermission WHERE  permissioncode='CRM_MESSAGE_CENTER' and rolecode='CLIENT';

INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_MESSAGE_CENTER','ALLOW','SUPPLIER') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('CRM_MESSAGE_CENTER','ALLOW','CLIENT') ;

--WORKSPACE
DELETE FROM "anv".rolepermission WHERE  permissioncode='WORKSPACE_MAIN_MENU' and rolecode='SUPPLIER';
DELETE FROM "anv".rolepermission WHERE  permissioncode='WORKSPACE_MAIN_MENU' and rolecode='CLIENT';

INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('WORKSPACE_MAIN_MENU','ALLOW','SUPPLIER') ;
INSERT INTO "anv".rolepermission(permissioncode,access,rolecode) VALUES ('WORKSPACE_MAIN_MENU','ALLOW','CLIENT') ;


