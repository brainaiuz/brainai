

update "0".model set viewname='ProductServiceView' where formid='PRODUCT';
delete from "0".customFormSection where form_id='PRODUCT' and section='ADDITIONAL_INFORMATION';
delete from "0".customFormSection where form_id='PRODUCT' and section='PRODUCT_CUSTOM_FIELDS';
insert into "0".customformsection (form_id, section, sorder, expanded) values('PRODUCT', 'ADDITIONAL_INFORMATION', 11, false);
delete from "0".modelfield where form_id='PRODUCT' and field_id='INPUT_PRODUCT_CUSTOM_FIELDS';

update "anv".model set viewname='ProductServiceView' where formid='PRODUCT';
delete from "anv".customFormSection where form_id='PRODUCT' and section='ADDITIONAL_INFORMATION';
delete from "anv".customFormSection where form_id='PRODUCT' and section='PRODUCT_CUSTOM_FIELDS';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PRODUCT', 'ADDITIONAL_INFORMATION', 11, false);
delete from "anv".modelfield where form_id='PRODUCT' and field_id='INPUT_PRODUCT_CUSTOM_FIELDS';
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('PRODUCT', 'ProductServiceView')) WHERE  id=(SELECT id FROM company LIMIT 1);

update "0_template".model set viewname='ProductServiceView' where formid='PRODUCT';
delete from "0_template".customFormSection where form_id='PRODUCT' and section='ADDITIONAL_INFORMATION';
delete from "0_template".customFormSection where form_id='PRODUCT' and section='PRODUCT_CUSTOM_FIELDS';
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PRODUCT', 'ADDITIONAL_INFORMATION', 11, false);
delete from "0_template".modelfield where form_id='PRODUCT' and field_id='INPUT_PRODUCT_CUSTOM_FIELDS';