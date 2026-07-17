delete from "anv".itemtable_settings where section = 'BANK_PAYMENT_ITEM';
insert into "anv".itemtable_settings (section, settingsjsondata)
values ('BANK_PAYMENT_ITEM',
        '[{"code": "ACCOUNT","title": "Account", "width":12, "selected": true,"required": true,"order": 1},'
            '{"code": "DESCRIPTION","title": "Description", "width":16, "selected": true,"required": false,"order": 2},'
            '{"code": "REFERENCE","title": "Reference", "width":12, "selected": true,"required": false,"order": 3},'
            '{"code": "AMOUNT","title": "Amount", "width":10, "selected": true,"required": true,"order": 4},'
            '{"code": "TAX_RATE","title": "Tax Rate", "width":10, "selected": true,"required": false,"order": 5},'
            '{"code": "NAME","title":"Name", "width":10, "selected":true,"required": false,"order":6},'
            '{"code": "CLIENT","title":"Bill To", "width":10, "selected":true,"required": false,"order":7},'
            '{"code": "PROJECT","title":"Project", "width":10, "selected":true,"required": false,"order":8},'
            '{"code": "DEPARTMENT","title":"Department", "width":10, "selected":true,"required": false,"order":9}]');


delete from "anv".itemtable_settings where section = 'CASH_PAYMENT_ITEM';
insert into "anv".itemtable_settings (section, settingsjsondata)
values ('CASH_PAYMENT_ITEM',
        '[{"code": "ACCOUNT","title": "Account", "width":12, "selected": true,"required": true,"order": 1},'
            '{"code": "DESCRIPTION","title": "Description", "width":16, "selected": true,"required": false,"order": 2},'
            '{"code": "REFERENCE","title": "Reference", "width":12, "selected": true,"required": false,"order": 3},'
            '{"code": "AMOUNT","title": "Amount", "width":10, "selected": true,"required": true,"order": 4},'
            '{"code": "TAX_RATE","title": "Tax Rate", "width":10, "selected": true,"required": false,"order": 5},'
            '{"code": "NAME","title":"Name", "width":10, "selected":true,"required":false,"order":6},'
            '{"code": "CLIENT","title":"Bill To", "width":10, "selected":true,"required":false,"order":7},'
            '{"code": "PROJECT","title":"Project", "width":10, "selected":true,"required":false,"order":8},'
            '{"code": "DEPARTMENT","title":"Department", "width":10, "selected":true,"required":false,"order":9}]');


delete from "anv".itemtable_settings where section = 'BANK_RECEIPT_ITEM';
insert into "anv".itemtable_settings (section, settingsjsondata)
values ('BANK_RECEIPT_ITEM',
        '[{"code": "ACCOUNT","title": "Account", "width":15, "selected": true,"required": true,"order": 1},'
            '{"code": "DESCRIPTION","title": "Description", "width":20, "selected": true,"required": false,"order": 2},'
            '{"code": "REFERENCE","title": "Reference", "width":15, "selected": true,"required": false,"order": 3},'
            '{"code": "AMOUNT","title": "Amount", "width":10, "selected": true,"required": true,"order": 4},'
            '{"code": "TAX_RATE","title": "Tax Rate", "width":10, "selected": true,"required": false,"order": 5},'
            '{"code": "NAME","title":"Name", "width":10, "selected":true,"required":false,"order":6},'
            '{"code": "PROJECT","title":"Project", "width":10, "selected":true,"required":false,"order":7},'
            '{"code": "DEPARTMENT","title":"Department", "width":10, "selected":true,"required":false,"order":8}]');


delete from "anv".itemtable_settings where section = 'CASH_RECEIPT_ITEM';
insert into "anv".itemtable_settings (section, settingsjsondata)
values ('CASH_RECEIPT_ITEM',
        '[{"code": "ACCOUNT","title": "Account", "width":15, "selected": true,"required": true,"order": 1},'
            '{"code": "DESCRIPTION","title": "Description", "width":20, "selected": true,"required": false,"order": 2},'
            '{"code": "REFERENCE","title": "Reference", "width":15, "selected": true,"required": false,"order": 3},'
            '{"code": "AMOUNT","title": "Amount", "width":10, "selected": true,"required": true,"order": 4},'
            '{"code": "TAX_RATE","title": "Tax Rate", "width":10, "selected": true,"required": false,"order": 5},'
            '{"code": "NAME","title":"Name", "width":10, "selected":true,"required":false,"order":6},'
            '{"code": "PROJECT","title":"Project", "width":10, "selected":true,"required":false,"order":7},'
            '{"code": "DEPARTMENT","title":"Department", "width":10, "selected":true,"required":false,"order":8}]');