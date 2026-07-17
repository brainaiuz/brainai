
update modelfield
set disableupdate = false
where form_id='SALEQUOTE_FORM' and field_id='STATUS';

update modelfield
set disableupdate = false
where form_id='SALEORDER_FORM' and field_id='STATUS';

update "anv".modelfield
set disableupdate = false
where form_id='SALEQUOTE_FORM' and field_id='STATUS';

update "anv".modelfield
set disableupdate = false
where form_id='SALEORDER_FORM' and field_id='STATUS';