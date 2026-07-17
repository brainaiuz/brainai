
delete from "anv".itemtable_settings where section = 'RFP_ITEM';
insert into "anv".itemtable_settings (section, settingsjsondata)
values ('RFP_ITEM',
        '[{"code": "PRODUCT","title": "Item", "width":20, "selected": true,"required": true,"order": 1},'
        '{"code": "DESCRIPTION","title": "Description", "width":35, "selected": true,"required": false,"order": 2},'
        '{"code": "QTY","title": "Qty", "width":10, "selected": true,"required": true,"order": 3},'
        '{"code": "MEASUREMENT","title": "U/M", "width":10, "selected": true,"required": false,"order": 4},'
        '{"code": "QTY_ON_HAND","title":"Qty On Hand", "width":10, "selected":true,"required": false,"order":5},'
        '{"code": "WAREHOUSE","title":"Warehouse", "width":13, "selected":true,"required": true,"order":6},'
        '{"code": "DEPARTMENT","title":"Department", "width":15, "selected":true,"required":false,"order":7}]');




