
--print pdf for candidate
delete from permission where code='HRMS_PRINT_PDF_CANDIDATE';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_PRINT_PDF_CANDIDATE', 'HRMS', 'Print PDF', (SELECT max(sorder) + 1 from permission WHERE code = 'HRMS_CANDIDATE_LIST_VIEW'),
        (select id from permission where code = 'HRMS_CANDIDATE_LIST_VIEW'), 'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_PRINT_PDF_CANDIDATE';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_PRINT_PDF_CANDIDATE', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_PRINT_PDF_CANDIDATE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_PRINT_PDF_CANDIDATE', 'ALLOW', 'DR'),
                                                                           ('HRMS_PRINT_PDF_CANDIDATE', 'ALLOW', 'ADMIN'),
                                                                           ('HRMS_PRINT_PDF_CANDIDATE', 'ALLOW', 'HR');

--search for candidate
delete from permission where code='HRMS_SEARCH_CANDIDATE';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SEARCH_CANDIDATE', 'HRMS', 'Search', (SELECT max(sorder) + 1 from permission WHERE code = 'HRMS_CANDIDATE_LIST_VIEW'),
        (select id from permission where code = 'HRMS_CANDIDATE_LIST_VIEW'), 'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_SEARCH_CANDIDATE';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_SEARCH_CANDIDATE', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_SEARCH_CANDIDATE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_SEARCH_CANDIDATE', 'ALLOW', 'DR'),
                                                                           ('HRMS_SEARCH_CANDIDATE', 'ALLOW', 'ADMIN'),
                                                                           ('HRMS_SEARCH_CANDIDATE', 'ALLOW', 'HR');
