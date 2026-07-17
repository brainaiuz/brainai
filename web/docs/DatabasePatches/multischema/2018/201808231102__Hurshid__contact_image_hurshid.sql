insert into "0".modelfield (forder, columntype, fsection, section, field_id, form_id, hide) values(10010, 'COL_1', 'CONTACT_INFORMATION', 'CONTACT_INFORMATION', 'PROFILE_PICTURE', 'CONTACT_FORM', true);
insert into "anv".modelfield (forder, columntype, fsection, section, field_id, form_id, hide) values(10010, 'COL_1', 'CONTACT_INFORMATION', 'CONTACT_INFORMATION', 'PROFILE_PICTURE', 'CONTACT_FORM', true);

DELETE FROM "0".modelfield WHERE form_id='CONTACT_FORM' and field_id='IMAGE_UPLOAD';
DELETE FROM "anv".modelfield WHERE form_id='CONTACT_FORM' and field_id='IMAGE_UPLOAD';