
delete from permission where code = 'HRMS_APPROVE_AND_HIRE_PLACEMENT';
delete from "anv".permission_context where permissioncode = 'HRMS_APPROVE_AND_HIRE_PLACEMENT';
delete from "anv".rolepermission where permissioncode = 'HRMS_APPROVE_AND_HIRE_PLACEMENT';



insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('PLACEMENT_STATUS_REJECTED', false, true, 'Rejected', true, 3, (select id from "anv".reference where code='_PLACEMENT_STATUS'), true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('PLACEMENT_STATUS_SUBMITTED', false, true, 'Submitted', true, 3, (select id from "anv".reference where code='_PLACEMENT_STATUS'), true);



delete from "anv".modelfield where form_id = 'PLACEMENT_FORM' and field_id='APPROVERS' ;
insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
('PLACEMENT_FORM',	'PLACEMENT_BASIC_INFORMATION','PLACEMENT_BASIC_INFORMATION','field',false,false,'COL_1',true,'','LOOKUP',2,'APPROVERS');