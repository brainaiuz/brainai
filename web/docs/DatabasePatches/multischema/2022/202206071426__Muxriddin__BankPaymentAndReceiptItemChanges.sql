UPDATE "anv".itemtable_settings
SET settingsjsonData = settingsjsonData::jsonb || '[{"code": "PROJECT","title":"Project", "width":10, "selected":true,"required":false,"order":6},{"code": "DEPARTMENT","title":"Department", "width":10, "selected":true,"required":false,"order":7}]'::jsonb
WHERE section = 'BANK_RECEIPT_ITEM' and settingsjsonData::text not ilike ANY (ARRAY['%Project%'::text]);

UPDATE "anv".itemtable_settings
SET settingsjsonData = settingsjsonData::jsonb || '[{"code": "PROJECT","title":"Project", "width":10, "selected":true,"required": false,"order":6},{"code": "DEPARTMENT","title":"Department", "width":10, "selected":true,"required": false,"order":7}]'::jsonb
WHERE section = 'BANK_PAYMENT_ITEM' and settingsjsonData::text not ilike ANY (ARRAY['%Project%'::text]);