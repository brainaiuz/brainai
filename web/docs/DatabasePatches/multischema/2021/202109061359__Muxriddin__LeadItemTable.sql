
delete from "anv".customformsection where form_id = 'LEAD_FORM' and section = 'CRM_LEAD_ITEMS';
insert into "anv".customformsection (active, custom, form_id, section, sorder) VALUES (true, false, 'LEAD_FORM', 'CRM_LEAD_ITEMS', 9);

delete from "anv".modelfield where form_id = 'LEAD_FORM' and field_id = 'CRM_LEAD_ITEMS';
insert into "anv".modelfield (widget, fullwidth, fieldsetstyle, fieldstyle, rowstyle, sectionstyle, form_id, fsection, field_id, forder, columntype, hide, hideincustomizeform)
values('UNKNOWN', true, 'slideDown-content group nobrd', 'field', 'row hideCustomField', 'slideDown-box  group expand hideCustomField', 'LEAD_FORM', 'CRM_LEAD_ITEMS', 'CRM_LEAD_ITEMS', 0, 'COL_1', false, false);


delete from "anv".itemtable_settings where section = 'LEAD_ITEM';
insert into "anv".itemtable_settings (section, settingsjsondata) values ('LEAD_ITEM',
    '[{"code":"PRODUCT","title":"Item","width":20,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"aliasName":"PRODUCT","order":0},
    {"code":"DESCRIPTION","title":"Description","width":30,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"order":1},
    {"code":"QTY","title":"Qty","width":10,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"aliasName":"QTY","order":2},
    {"code":"MEASUREMENT","title":"U/M","width":12,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"order":3},
    {"code":"UNITPRICE","title":"Price","width":13,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"aliasName":"UNITPRICE","order":4},
    {"code":"SUPPLIER","title":"Supplier","width":15,"selected":true,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"aliasName":"SUPPLIER","order":5},
    {"code":"CATEGORY","title":"Category","width":10,"selected":false,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"aliasName":"CATEGORY","order":6},
    {"code":"BRAND","title":"Brand","width":10,"selected":false,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"aliasName":"BRAND","order":7},
    {"code":"TAX_LIST","title":"Tax Rate","width":10,"selected":false,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"aliasName":"TAX_LIST","order":8},
    {"code":"NET_AMT","title":"Net Amount","width":10,"selected":false,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"aliasName":"NET_AMT","order":9},
    {"code":"TOTAL_AMT","title":"Total Amount","width":10,"selected":false,"required":false,"disabled":false,"changed":false,"clickable":false,"hasDefault":false,"minValue":0,"order":10}]');