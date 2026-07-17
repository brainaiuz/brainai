delete from "0".reference  where code = 'CUSTOM_FORM_ITEM_STATUS_DRAFT' and parentid = (select id from "0".reference where code='CUSTOM_FORM_ITEM_STATUS');
delete from "0".reference  where code = 'CUSTOM_FORM_ITEM_STATUS_SUBMITTED' and parentid = (select id from "0".reference where code='CUSTOM_FORM_ITEM_STATUS');
delete from "0".reference  where code = 'CUSTOM_FORM_ITEM_STATUS_REJECTED' and parentid = (select id from "0".reference where code='CUSTOM_FORM_ITEM_STATUS');
delete from "0".reference  where code = 'CUSTOM_FORM_ITEM_STATUS_APPROVED' and parentid = (select id from "0".reference where code='CUSTOM_FORM_ITEM_STATUS');
delete from "0".reference  where code = 'CUSTOM_FORM_ITEM_STATUS';

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('CUSTOM_FORM_ITEM_STATUS', false, true, 'Custom form status', true, 1, true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CUSTOM_FORM_ITEM_STATUS_DRAFT', false, true, 'Draft', true, 2, (select id from "0".reference where code='CUSTOM_FORM_ITEM_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CUSTOM_FORM_ITEM_STATUS_SUBMITTED', false, true, 'Submitted', true, 3, (select id from "0".reference where code='CUSTOM_FORM_ITEM_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CUSTOM_FORM_ITEM_STATUS_REJECTED', false, true, 'Rejected', true, 4, (select id from "0".reference where code='CUSTOM_FORM_ITEM_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CUSTOM_FORM_ITEM_STATUS_APPROVED', false, true, 'Approved', true, 5, (select id from "0".reference where code='CUSTOM_FORM_ITEM_STATUS'), true);

delete from "anv".reference  where code = 'CUSTOM_FORM_ITEM_STATUS_DRAFT' and parentid = (select id from "anv".reference where code='CUSTOM_FORM_ITEM_STATUS');
delete from "anv".reference  where code = 'CUSTOM_FORM_ITEM_STATUS_SUBMITTED' and parentid = (select id from "anv".reference where code='CUSTOM_FORM_ITEM_STATUS');
delete from "anv".reference  where code = 'CUSTOM_FORM_ITEM_STATUS_REJECTED' and parentid = (select id from "anv".reference where code='CUSTOM_FORM_ITEM_STATUS');
delete from "anv".reference  where code = 'CUSTOM_FORM_ITEM_STATUS_APPROVED' and parentid = (select id from "anv".reference where code='CUSTOM_FORM_ITEM_STATUS');
delete from "anv".reference  where code = 'CUSTOM_FORM_ITEM_STATUS';

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('CUSTOM_FORM_ITEM_STATUS', false, true, 'Custom form status', true, 1, true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CUSTOM_FORM_ITEM_STATUS_DRAFT', false, true, 'Draft', true, 2, (select id from "anv".reference where code='CUSTOM_FORM_ITEM_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CUSTOM_FORM_ITEM_STATUS_SUBMITTED', false, true, 'Submitted', true, 3, (select id from "anv".reference where code='CUSTOM_FORM_ITEM_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CUSTOM_FORM_ITEM_STATUS_REJECTED', false, true, 'Rejected', true, 4, (select id from "anv".reference where code='CUSTOM_FORM_ITEM_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CUSTOM_FORM_ITEM_STATUS_APPROVED', false, true, 'Approved', true, 5, (select id from "anv".reference where code='CUSTOM_FORM_ITEM_STATUS'), true);
