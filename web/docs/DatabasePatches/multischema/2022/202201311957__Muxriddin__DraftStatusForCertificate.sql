update "23039".customformsection set expanded = true where form_id='CERTIFICATE_OF_EMPLOYMENT_FORM' and custom is false;

delete from "anv".reference  where code = 'CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT', false, true, 'Draft', true, 5, (select id from "anv".reference where code='CERTIFICATE_OF_EMPLOYMENT_STATUS'), true);
