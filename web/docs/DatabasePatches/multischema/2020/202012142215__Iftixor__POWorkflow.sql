
update modelfield
set disableupdate = false
where form_id='PURCHASEORDER_FORM' and field_id='STATUS';

update "anv".modelfield
set disableupdate = false
where form_id='PURCHASEORDER_FORM' and field_id='STATUS';
