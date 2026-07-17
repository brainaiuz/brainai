insert into "anv".model (formid, title, viewname, active) values('BRIGADA_FORM', 'Brigada List View', 'BrigadaList', true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BRIGADA_FORM', 'BASIC_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BRIGADA_FORM', 'INVOLVED_EMPLOYEES', 1,true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BRIGADA_FORM', 'ATTACHMENTS', 2,true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BRIGADA_FORM', 'ADDITIONAL_INFORMATION', 3,true);


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRIGADA_FORM','NUMBER',true,'COL_1','BASIC_INFORMATION'	,0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRIGADA_FORM','STATUS',	false,	'COL_2', 'BASIC_INFORMATION',	0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRIGADA_FORM','MANAGER',false,'COL_3','BASIC_INFORMATION',	0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRIGADA_FORM','NAME',true,'COL_1','BASIC_INFORMATION',	1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRIGADA_FORM','BACKUP_MANAGER',false,'COL_3','BASIC_INFORMATION',1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRIGADA_FORM','INVOLVED_EMPLOYEE',true,'COL_1','INVOLVED_EMPLOYEES',0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRIGADA_FORM','PROJECT_NOTE',true,'COL_1','ATTACHMENTS',	0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRIGADA_FORM','ATTACHMENTS',true,'COL_2','ATTACHMENTS',	0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRIGADA_FORM','EMPLOYEE_ASSIGNMENT',true,'COL_1','INVOLVED_EMPLOYEES',1);