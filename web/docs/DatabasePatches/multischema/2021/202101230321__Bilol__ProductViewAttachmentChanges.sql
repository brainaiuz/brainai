update "0".modelfield
set columntype = 'COL_1'
where fsection='ATTACHMENTS' AND form_id='PRODUCT';

update "anv".modelfield
set columntype = 'COL_1'
where fsection='ATTACHMENTS' AND form_id='PRODUCT';


update "0".modelfield
set customizabletable = true
where field_id='ATTACHMENTS' AND form_id='PRODUCT';
update "anv".modelfield
set customizabletable = true
where field_id='ATTACHMENTS' AND form_id='PRODUCT';

delete from "0".itemtable_settings where section='PRODUCT_ATTACHMENTS';
INSERT INTO "0".itemtable_settings (section, settingsjsondata) VALUES ('PRODUCT_ATTACHMENTS', '[{"code":"NAME","title":"Name","width":20,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":true,"aliasName":"NAME","order":0},{"code":"DESCRIPTION","title":"Description","width":20,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":true,"aliasName":"DESCRIPTION","order":1},{"code":"DOCUMENT_ID","title":"Document ID","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":true,"aliasName":"DOCUMENT_ID","order":2},{"code":"TYPE","title":"Type","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":true,"aliasName":"TYPE","order":3},{"code":"CREATED_DATE","title":"Created Date","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"aliasName":"CREATED_DATE","order":4},{"code":"FILE_SIZE","title":"File Size","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"aliasName":"FILE_SIZE","order":5},{"code":"DOWNLOAD","title":"Download","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"aliasName":"DOWNLOAD","order":6},{"code":"REMOVE","title":"Remove","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"aliasName":"REMOVE","order":7}]');

delete from "anv".itemtable_settings where section='PRODUCT_ATTACHMENTS';
INSERT INTO "anv".itemtable_settings (section, settingsjsondata) VALUES ('PRODUCT_ATTACHMENTS', '[{"code":"NAME","title":"Name","width":20,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":true,"aliasName":"NAME","order":0},{"code":"DESCRIPTION","title":"Description","width":20,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":true,"aliasName":"DESCRIPTION","order":1},{"code":"DOCUMENT_ID","title":"Document ID","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":true,"aliasName":"DOCUMENT_ID","order":2},{"code":"TYPE","title":"Type","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":true,"aliasName":"TYPE","order":3},{"code":"CREATED_DATE","title":"Created Date","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"aliasName":"CREATED_DATE","order":4},{"code":"FILE_SIZE","title":"File Size","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"aliasName":"FILE_SIZE","order":5},{"code":"DOWNLOAD","title":"Download","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"aliasName":"DOWNLOAD","order":6},{"code":"REMOVE","title":"Remove","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"aliasName":"REMOVE","order":7}]');