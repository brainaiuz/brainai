
delete from "anv".reference  where code = '_WORKFLOW_MODULE_PICKLIST';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('_WORKFLOW_MODULE_PICKLIST', false, true, 'Picklist', true,
							(select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
							(select id from "anv".reference where code='_WORKFLOW_MODULE'), true);


delete from "0".reference  where code = '_WORKFLOW_MODULE_PICKLIST';
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('_WORKFLOW_MODULE_PICKLIST', false, true, 'Picklist', true,
							(select max(sorder) from "0".reference where parentid = (select id from "0".reference where code = '_WORKFLOW_MODULE')),
							(select id from "0".reference where code='_WORKFLOW_MODULE'), true);

delete from "model"  where formid = 'PICKLIST_FORM';
insert into model(active, formid, title, viewname) values(true, 'PICKLIST_FORM', 'Picklist Form', 'Picklist');
