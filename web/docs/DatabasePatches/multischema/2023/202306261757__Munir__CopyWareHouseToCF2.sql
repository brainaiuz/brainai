insert into "0".model (formid, title, viewname, active)
values ('WAREHOUSE_FORM', 'WareHouses', 'WareHouses', true);

insert into "0".customformsection (form_id, section, sorder, expanded)
values ('WAREHOUSE_FORM', 'BASIC_INFORMATION', 0, true);


insert into "0".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_1', 'NAME', '0', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'TRUE', 'TRUE' );
insert into "0".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_1', 'OWNERS', '1', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "0".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_2', 'PRIMARY_CONTACT', '0', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "0".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_2', 'PHONE', '1', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "0".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_2', 'EMAIL', '2', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "0".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_2', 'ADDRESS', '3', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "0".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_3', 'NOTES', '1', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "0".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_3', 'NUMBER', '0', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );



insert into "anv".model (formid, title, viewname, active)
values ('WAREHOUSE_FORM', 'WareHouses', 'WareHouses', true);

insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('WAREHOUSE_FORM', 'BASIC_INFORMATION', 0, true);

insert into "anv".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_1', 'NAME', '0', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'TRUE', 'TRUE' );
insert into "anv".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_1', 'OWNERS', '1', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "anv".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_2', 'PRIMARY_CONTACT', '0', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "anv".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_2', 'PHONE', '1', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "anv".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_2', 'EMAIL', '2', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "anv".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_2', 'ADDRESS', '3', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "anv".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_3', 'NOTES', '1', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
insert into "anv".modelfield (columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory ) values('COL_3', 'NUMBER', '0', 'WAREHOUSE_FORM', 'BASIC_INFORMATION', 'FALSE', 'FALSE' );
