delete from "anv".itemtable_settings where section = 'MANUAL_JOURNAL_ITEM';
insert into "anv".itemtable_settings (section, settingsjsondata)
values ('MANUAL_JOURNAL_ITEM',
        '[{"code": "ACCOUNT","title": "Account", "width":15, "selected": true,"required": true,"order": 1},'
        '{"code": "DEBIT","title": "Debit", "width":12, "selected": true,"required": true,"order": 2},'
        '{"code": "CREDIT","title": "Credit", "width":12, "selected": true,"required": true,"order": 3},'
        '{"code": "DESCRIPTION","title": "Description", "width":20, "selected": true,"required": false,"order": 4},'
        '{"code": "NAME","title":"Name", "width":11, "selected":true,"required":false,"order":5},'
        '{"code": "BILLING","title":"Billing", "width":10, "selected":true,"required":false,"order":6},'
        '{"code": "PROJECT","title":"Project", "width":10, "selected":true,"required":false,"order":7},'
        '{"code": "DEPARTMENT","title":"Department", "width":10, "selected":true,"required":false,"order":8}]');