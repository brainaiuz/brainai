update permission
set sorder = 4,
    name='Hire'
where code = 'HRMS_HIRE_PLACEMENT'
  and parent = 797;
update permission
set sorder = 8
where code = 'HRMS_PLACEMENT_SEE_All'
  and parent = 797;
update permission
set sorder = 9
where code = 'HRMS_PLACEMENT_SEE_OWN'
  and parent = 797;

-- EDIT
delete
from permission
where code = 'HRMS_EDIT_PLACEMENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_EDIT_PLACEMENT',
        'HRMS',
        'Edit',
        2,
        (select id from permission where code = 'HRMS_PLACEMENT_LIST_VIEW'),
        'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_EDIT_PLACEMENT';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_EDIT_PLACEMENT', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_EDIT_PLACEMENT';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_EDIT_PLACEMENT', 'ALLOW', 'HR'),
       ('HRMS_EDIT_PLACEMENT', 'ALLOW', 'ADMIN');

-- DRAFT
delete
from permission
where code = 'HRMS_DRAFT_PLACEMENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_DRAFT_PLACEMENT',
        'HRMS',
        'Draft',
        3,
        (select id from permission where code = 'HRMS_PLACEMENT_LIST_VIEW'),
        'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_DRAFT_PLACEMENT';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_DRAFT_PLACEMENT', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_DRAFT_PLACEMENT';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_DRAFT_PLACEMENT', 'ALLOW', 'HR'),
       ('HRMS_DRAFT_PLACEMENT', 'ALLOW', 'ADMIN');

-- APPROVE
delete
from permission
where code = 'HRMS_APPROVE_PLACEMENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_APPROVE_PLACEMENT',
        'HRMS',
        'Approve',
        5,
        (select id from permission where code = 'HRMS_PLACEMENT_LIST_VIEW'),
        'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_APPROVE_PLACEMENT';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_APPROVE_PLACEMENT', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_APPROVE_PLACEMENT';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_APPROVE_PLACEMENT', 'ALLOW', 'HR'),
       ('HRMS_APPROVE_PLACEMENT', 'ALLOW', 'ADMIN');

-- APPROVE_AND_HIRE
delete
from permission
where code = 'HRMS_APPROVE_AND_HIRE_PLACEMENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_APPROVE_AND_HIRE_PLACEMENT',
        'HRMS',
        'Approve And Hire',
        6,
        (select id from permission where code = 'HRMS_PLACEMENT_LIST_VIEW'),
        'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_APPROVE_AND_HIRE_PLACEMENT';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_APPROVE_AND_HIRE_PLACEMENT', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_APPROVE_AND_HIRE_PLACEMENT';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_APPROVE_AND_HIRE_PLACEMENT', 'ALLOW', 'HR'),
       ('HRMS_APPROVE_AND_HIRE_PLACEMENT', 'ALLOW', 'ADMIN');

-- PDF
delete
from permission
where code = 'HRMS_PRINT_PDF_PLACEMENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_PRINT_PDF_PLACEMENT',
        'HRMS',
        'Print PDF',
        7,
        (select id from permission where code = 'HRMS_PLACEMENT_LIST_VIEW'),
        'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_PRINT_PDF_PLACEMENT';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_PRINT_PDF_PLACEMENT', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_PRINT_PDF_PLACEMENT';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_PRINT_PDF_PLACEMENT', 'ALLOW', 'HR'),
       ('HRMS_PRINT_PDF_PLACEMENT', 'ALLOW', 'ADMIN');
