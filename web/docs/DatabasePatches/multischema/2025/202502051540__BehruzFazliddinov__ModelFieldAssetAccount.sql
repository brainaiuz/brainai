delete from "anv".modelfield where form_id='PRODUCT' and field_id='ASSET_ACCOUNT';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PRODUCT', 'ASSET_ACCOUNT', false , 'COL_1', 'INVENTORY_STOCK_INFORMATION', 3);



delete from "0".modelfield where form_id='PRODUCT' and field_id='ASSET_ACCOUNT';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PRODUCT', 'ASSET_ACCOUNT', false , 'COL_1', 'INVENTORY_STOCK_INFORMATION', 3);
