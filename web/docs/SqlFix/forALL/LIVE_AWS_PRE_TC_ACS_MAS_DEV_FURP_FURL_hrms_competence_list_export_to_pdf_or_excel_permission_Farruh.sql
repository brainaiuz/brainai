-------------Insert REPMISSIONS (public schema)--------
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('COMPETENCES_LIST_PDF_EXCEL_EXPORT', 'HRMS', 'f', 'Competences Pdf/excel export', '3', (select id from permission where code='HRMS_COMPETENCES'), 'PERFORMANCE_APPRAISAL');


-------------------------------------------------------------------for 'anv'-----------
----------------------- SEE_ATTACHMENTS ----- default -----
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('COMPETENCES_LIST_PDF_EXCEL_EXPORT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('COMPETENCES_LIST_PDF_EXCEL_EXPORT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('COMPETENCES_LIST_PDF_EXCEL_EXPORT', 'TL', 'ALLOW');

-------------------------------------------------------------------for '0'-----------
----------------------- SEE_ATTACHMENTS ----- default -----
insert into "0".rolepermission (permissioncode, rolecode, access) values ('COMPETENCES_LIST_PDF_EXCEL_EXPORT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('COMPETENCES_LIST_PDF_EXCEL_EXPORT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('COMPETENCES_LIST_PDF_EXCEL_EXPORT', 'TL', 'ALLOW');