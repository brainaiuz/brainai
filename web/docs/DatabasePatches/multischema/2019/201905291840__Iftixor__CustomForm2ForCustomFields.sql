
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('ISSUE_FORM', 'Issues')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('FIXED_ASSET_FORM', 'FixedAsset')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('PRODUCT', 'ProductServiceAdd')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('LEAVE_REQUEST_FORM', 'LeaveRequest')) WHERE  id=(SELECT id FROM company LIMIT 1);


delete from "0".modelfield where form_id='PRODUCT' and fsection='PRODUCT_CUSTOM_FIELDS';
delete from "0".modelfield where form_id='PRODUCT' and fsection='PRODUCT_CATEGORY_CUSTOM_FIELDS';
delete from "0".customformsection where form_id='PRODUCT' and section='ADDITIONAL_INFORMATION';

insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('PRODUCT', 'INPUT_PRODUCT_CUSTOM_FIELDS', false, 'PRODUCT_CUSTOM_FIELDS', 0);
insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('PRODUCT', 'INPUT_CUSTOM_FIELDS', false, 'PRODUCT_CATEGORY_CUSTOM_FIELDS', 0);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PRODUCT', 'PRODUCT_CUSTOM_FIELDS', 9, false);

delete from "anv".modelfield where form_id='PRODUCT' and fsection='PRODUCT_CUSTOM_FIELDS';
delete from "anv".modelfield where form_id='PRODUCT' and fsection='PRODUCT_CATEGORY_CUSTOM_FIELDS';
delete from "anv".customformsection where form_id='PRODUCT' and section='PRODUCT_CUSTOM_FIELDS';

insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('PRODUCT', 'INPUT_PRODUCT_CUSTOM_FIELDS', false, 'PRODUCT_CUSTOM_FIELDS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('PRODUCT', 'INPUT_CUSTOM_FIELDS', false, 'PRODUCT_CATEGORY_CUSTOM_FIELDS', 0);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PRODUCT', 'PRODUCT_CUSTOM_FIELDS', 9, false);


delete from "0_template".modelfield where form_id='PRODUCT' and fsection='PRODUCT_CUSTOM_FIELDS';
delete from "0_template".modelfield where form_id='PRODUCT' and fsection='PRODUCT_CATEGORY_CUSTOM_FIELDS';
delete from "0_template".customformsection where form_id='PRODUCT' and section='ADDITIONAL_INFORMATION';

insert into "0_template".modelfield (form_id, field_id, mandatory,  fsection, forder) values('PRODUCT', 'INPUT_PRODUCT_CUSTOM_FIELDS', false, 'PRODUCT_CUSTOM_FIELDS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory,  fsection, forder) values('PRODUCT', 'INPUT_CUSTOM_FIELDS', false, 'PRODUCT_CATEGORY_CUSTOM_FIELDS', 0);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PRODUCT', 'PRODUCT_CUSTOM_FIELDS', 9, false);
