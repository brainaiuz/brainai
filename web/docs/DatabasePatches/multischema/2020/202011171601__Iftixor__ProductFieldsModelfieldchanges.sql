
delete from "anv".modelfield where form_id='PRODUCT' and field_id='SKU_NUMBER';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PRODUCT', 'SKU_NUMBER', false , 'COL_1', 'MORE_OPTIONS', 0);
delete from "anv".modelfield where form_id='PRODUCT' and field_id='UPC_NUMBER';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PRODUCT', 'UPC_NUMBER', false , 'COL_1', 'MORE_OPTIONS', 1);
delete from "anv".modelfield where form_id='PRODUCT' and field_id='UNIT_MEASUREMENT';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PRODUCT', 'UNIT_MEASUREMENT', false , 'COL_2', 'MORE_OPTIONS', 0);
delete from "anv".modelfield where form_id='PRODUCT' and field_id='MANUFACTURER';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PRODUCT', 'MANUFACTURER', false , 'COL_2', 'MORE_OPTIONS', 1);
delete from "anv".modelfield where form_id='PRODUCT' and field_id='BARCODE';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PRODUCT', 'BARCODE', false , 'COL_2', 'MORE_OPTIONS', 2);
delete from "anv".modelfield where form_id='PRODUCT' and field_id='PART_NUMBER';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PRODUCT', 'PART_NUMBER', false , 'COL_3', 'MORE_OPTIONS', 0);
delete from "anv".modelfield where form_id='PRODUCT' and field_id='SUPPLIERS';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PRODUCT', 'SUPPLIERS', false , 'COL_3', 'MORE_OPTIONS', 1);

delete from "anv".modelfield where form_id='PRODUCT' and field_id='MORE_DETAILS';
