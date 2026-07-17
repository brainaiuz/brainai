insert into "0".model (formid, title, viewname, active) values('BRAND_FORM', 'Brand Form', 'Brand', true);

insert into "0".customformsection (form_id, section, sorder, expanded) values('BRAND_FORM', 'TITLE', 0, true);


insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRAND_FORM', 'NAME', true, 'COL_1', 'TITLE', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRAND_FORM', 'DESCRIPTION', false, 'COL_1', 'TITLE', 1);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRAND_FORM', 'PARENT', false, 'COL_2', 'TITLE', 0);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRAND_FORM', 'IMAGE_PANEL', false, 'COL_3', 'TITLE', 0);




insert into "anv".model (formid, title, viewname, active) values('BRAND_FORM', 'Brand Form', 'Brand', true);

insert into "anv".customformsection (form_id, section, sorder, expanded) values('BRAND_FORM', 'TITLE', 0, true);


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRAND_FORM', 'NAME', true, 'COL_1', 'TITLE', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRAND_FORM', 'DESCRIPTION', false, 'COL_1', 'TITLE', 1);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRAND_FORM', 'PARENT', false, 'COL_2', 'TITLE', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BRAND_FORM', 'IMAGE_PANEL', false, 'COL_3', 'TITLE', 0);


