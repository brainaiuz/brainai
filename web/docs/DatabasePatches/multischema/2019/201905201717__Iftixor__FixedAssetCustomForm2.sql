insert into "0".model (formid, title, viewname, active) values('FIXED_ASSET_FORM', 'Fixed Asset Form', 'fixedasset', true);

insert into "0".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'FIXED_ASSET_INFORMATION', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'FIXED_ASSET_FINANCING', 1, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'DEPRECIATION_ACCOUNT', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'ADDITIONAL_INFORMATION', 3, false);


insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'CODE', false, 'COL_1', 'FIXED_ASSET_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'NAME', true, 'COL_1', 'FIXED_ASSET_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'COST', true, 'COL_1', 'FIXED_ASSET_INFORMATION', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'QUANTITY', false, 'COL_1', 'FIXED_ASSET_INFORMATION', 3);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'PURCHASE_DATE', true, 'COL_1', 'FIXED_ASSET_INFORMATION', 4);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'TAX_CALC_TYPE', false, 'COL_1', 'FIXED_ASSET_INFORMATION', 5);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'OWNER', false, 'COL_2', 'FIXED_ASSET_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'CATEGORY', true, 'COL_2', 'FIXED_ASSET_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'USEFUL_LIFE', true, 'COL_2', 'FIXED_ASSET_INFORMATION', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'RESIDUAL_VALUE', true, 'COL_2', 'FIXED_ASSET_INFORMATION', 3);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'TAX_VALUE', false, 'COL_2', 'FIXED_ASSET_INFORMATION', 4);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'DEPARTMENT', false, 'COL_2', 'FIXED_ASSET_INFORMATION', 5);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'UPLOAD_FORM', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'LOCATION_FIELD', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'DESCRIPTION', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'SHOW_DESCRIPTION_IN_BARCODE', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 3);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'ACCOUNT_NAME', true, 'COL_1', 'FIXED_ASSET_FINANCING', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'RELATED_ITEM', false, 'COL_1', 'FIXED_ASSET_FINANCING', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'FIXED_ASSET_ACCOUNT', true, 'COL_1', 'DEPRECIATION_ACCOUNT', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'EXPENSE_ACCOUNT', true, 'COL_2', 'DEPRECIATION_ACCOUNT', 0);

insert into "anv".model (formid, title, viewname, active) values('FIXED_ASSET_FORM', 'Fixed Asset Form', 'fixedasset', true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'FIXED_ASSET_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'FIXED_ASSET_FINANCING', 1, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'DEPRECIATION_ACCOUNT', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'ADDITIONAL_INFORMATION', 3, false);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'CODE', false, 'COL_1', 'FIXED_ASSET_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'NAME', true, 'COL_1', 'FIXED_ASSET_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'COST', true, 'COL_1', 'FIXED_ASSET_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'QUANTITY', false, 'COL_1', 'FIXED_ASSET_INFORMATION', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'PURCHASE_DATE', true, 'COL_1', 'FIXED_ASSET_INFORMATION', 4);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'TAX_CALC_TYPE', false, 'COL_1', 'FIXED_ASSET_INFORMATION', 5);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'OWNER', false, 'COL_2', 'FIXED_ASSET_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'CATEGORY', true, 'COL_2', 'FIXED_ASSET_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'USEFUL_LIFE', true, 'COL_2', 'FIXED_ASSET_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'RESIDUAL_VALUE', true, 'COL_2', 'FIXED_ASSET_INFORMATION', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'TAX_VALUE', false, 'COL_2', 'FIXED_ASSET_INFORMATION', 4);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'DEPARTMENT', false, 'COL_2', 'FIXED_ASSET_INFORMATION', 5);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'UPLOAD_FORM', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'LOCATION_FIELD', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'DESCRIPTION', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'SHOW_DESCRIPTION_IN_BARCODE', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'ACCOUNT_NAME', true, 'COL_1', 'FIXED_ASSET_FINANCING', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'RELATED_ITEM', false, 'COL_1', 'FIXED_ASSET_FINANCING', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'FIXED_ASSET_ACCOUNT', true, 'COL_1', 'DEPRECIATION_ACCOUNT', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'EXPENSE_ACCOUNT', true, 'COL_2', 'DEPRECIATION_ACCOUNT', 0);


insert into "0_template".model (formid, title, viewname, active) values('FIXED_ASSET_FORM', 'Fixed Asset Form', 'fixedasset', true);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'FIXED_ASSET_INFORMATION', 0, true);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'FIXED_ASSET_FINANCING', 1, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'DEPRECIATION_ACCOUNT', 2, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('FIXED_ASSET_FORM', 'ADDITIONAL_INFORMATION', 3, false);

insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'CODE', false, 'COL_1', 'FIXED_ASSET_INFORMATION', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'NAME', true, 'COL_1', 'FIXED_ASSET_INFORMATION', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'COST', true, 'COL_1', 'FIXED_ASSET_INFORMATION', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'QUANTITY', false, 'COL_1', 'FIXED_ASSET_INFORMATION', 3);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'PURCHASE_DATE', true, 'COL_1', 'FIXED_ASSET_INFORMATION', 4);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'TAX_CALC_TYPE', false, 'COL_1', 'FIXED_ASSET_INFORMATION', 5);

insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'OWNER', false, 'COL_2', 'FIXED_ASSET_INFORMATION', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'CATEGORY', true, 'COL_2', 'FIXED_ASSET_INFORMATION', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'USEFUL_LIFE', true, 'COL_2', 'FIXED_ASSET_INFORMATION', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'RESIDUAL_VALUE', true, 'COL_2', 'FIXED_ASSET_INFORMATION', 3);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'TAX_VALUE', false, 'COL_2', 'FIXED_ASSET_INFORMATION', 4);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'DEPARTMENT', false, 'COL_2', 'FIXED_ASSET_INFORMATION', 5);

insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'UPLOAD_FORM', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'LOCATION_FIELD', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'DESCRIPTION', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'SHOW_DESCRIPTION_IN_BARCODE', false, 'COL_3', 'FIXED_ASSET_INFORMATION', 3);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'ACCOUNT_NAME', true, 'COL_1', 'FIXED_ASSET_FINANCING', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'RELATED_ITEM', false, 'COL_1', 'FIXED_ASSET_FINANCING', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'FIXED_ASSET_ACCOUNT', true, 'COL_1', 'DEPRECIATION_ACCOUNT', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('FIXED_ASSET_FORM', 'EXPENSE_ACCOUNT', true, 'COL_2', 'DEPRECIATION_ACCOUNT', 0);
