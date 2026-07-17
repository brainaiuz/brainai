insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('OPPORTUNITY_FORM', 'APPROVERS', true, false, 'COL_3', 'OPPORTUNITY_INFORMATION', 1);


insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('OPPORTUNITY_STATUS', false, 'Opportunity Status', 'Opportunity Status', false, false, true, 0, null, false, true);



insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('OPPORTUNITY_REJECTED', false, 'OPPORTUNITY Rejected', 'OPPORTUNITY Rejected', false, false, true, 1,
        (select r.id from "anv".reference r where r.code = 'OPPORTUNITY_STATUS' order by r.id desc limit 1), false, true),
       ('OPPORTUNITY_SUBMITTED', false, 'OPPORTUNITY Submitted', 'OPPORTUNITY Submitted', false, false, true, 2,
        (select r.id from "anv".reference r where r.code = 'OPPORTUNITY_STATUS' order by r.id desc limit 1), false, true),
       ('OPPORTUNITY_APPROVED', false, 'OPPORTUNITY Approved', 'OPPORTUNITY Approved', false, false, true, 3,
        (select r.id from "anv".reference r where r.code = 'OPPORTUNITY_STATUS' order by r.id desc limit 1), false, true),
       ('OPPORTUNITY_DRAFT', false, 'OPPORTUNITY Draft', 'OPPORTUNITY Draft', false, false, true, 4,
        (select r.id from "anv".reference r where r.code = 'OPPORTUNITY_STATUS' order by r.id desc limit 1), false, true);


insert into myupdatetype (code, description, parentid)
values ('OPPORTUNITY_SUBMITTED', 'Records when user has submited OPPORTUNITY',
        (select id from myupdatetype where code = 'OPPORTUNITY'));
insert into myupdatetype (code, description, parentid)
values ('OPPORTUNITY_APPROVED', 'Records when user has approved OPPORTUNITY',
        (select id from myupdatetype where code = 'OPPORTUNITY'));
insert into myupdatetype (code, description, parentid)
values ('OPPORTUNITY_REJECTED', 'Records when user has rejected OPPORTUNITY',
        (select id from myupdatetype where code = 'OPPORTUNITY'));

