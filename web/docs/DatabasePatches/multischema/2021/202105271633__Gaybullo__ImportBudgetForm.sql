delete from "model"  where formid = 'IMPORT_BUDGET_MANAGER_FORM';
delete from "modelfield"  where form_id = 'IMPORT_BUDGET_MANAGER_FORM';

delete from "anv"."model"  where formid = 'IMPORT_BUDGET_MANAGER_FORM';
delete from "anv"."modelfield"  where form_id = 'IMPORT_BUDGET_MANAGER_FORM';

delete from "anv".customformsection where form_id = 'IMPORT_BUDGET_MANAGER_FORM';

insert into "anv".model(active, formid, title) values (true , 'IMPORT_BUDGET_MANAGER_FORM', 'Import Budget Manager');

INSERT INTO "anv".customformsection (active, custom, form_id, section, sorder) VALUES (true, false, 'IMPORT_BUDGET_MANAGER_FORM', 'REQUIRED_INFORMATIONS', 0);

insert into "anv".modelfield
(form_id, fsection, columntype, mandatory, forder, field_id)
values ('IMPORT_BUDGET_MANAGER_FORM', 'REQUIRED_INFORMATIONS', 'COL_1', true, 0, 'HAS_CSV_HEADER');

insert into "anv".modelfield
(form_id, fsection, columntype, mandatory, forder, field_id)
values ('IMPORT_BUDGET_MANAGER_FORM', 'REQUIRED_INFORMATIONS', 'COL_1', true, 0, 'NAME');

insert into "anv".modelfield
(form_id, fsection, columntype, mandatory, forder, field_id)
values ('IMPORT_BUDGET_MANAGER_FORM', 'REQUIRED_INFORMATIONS', 'COL_1', true, 0, 'EXPECTED');

insert into "anv".modelfield
(form_id, fsection, columntype, mandatory, forder, field_id)
values ('IMPORT_BUDGET_MANAGER_FORM', 'REQUIRED_INFORMATIONS', 'COL_1', true, 0, 'ACTUAL');

insert into "anv".modelfield
(form_id, fsection, columntype, mandatory, forder, field_id)
values ('IMPORT_BUDGET_MANAGER_FORM', 'REQUIRED_INFORMATIONS', 'COL_1', true, 0, 'DATE');