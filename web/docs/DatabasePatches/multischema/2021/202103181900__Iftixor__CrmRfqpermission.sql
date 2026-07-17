
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_ADD' and rolecode='ADMIN';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_ADD','ALLOW','ADMIN');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_LIST' and rolecode='ADMIN';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_LIST','ALLOW','ADMIN');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_EDIT' and rolecode='ADMIN';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_EDIT','ALLOW','ADMIN');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_DELETE' and rolecode='ADMIN';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_DELETE','ALLOW','ADMIN');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_SUMMARY' and rolecode='ADMIN';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_SUMMARY','ALLOW','ADMIN');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_PDF' and rolecode='ADMIN';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_PDF','ALLOW','ADMIN');


delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_ADD' and rolecode='DR';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_ADD','ALLOW','DR');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_LIST' and rolecode='DR';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_LIST','ALLOW','DR');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_EDIT' and rolecode='DR';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_EDIT','ALLOW','DR');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_DELETE' and rolecode='DR';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_DELETE','ALLOW','DR');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_SUMMARY' and rolecode='DR';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_SUMMARY','ALLOW','DR');
delete from "anv".rolePermission where permissioncode='CRM_REQUEST_FOR_QUOTE_PDF' and rolecode='DR';
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('CRM_REQUEST_FOR_QUOTE_PDF','ALLOW','DR');

