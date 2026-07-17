

insert into "anv".itemtable_settings (section, settingsJSONData)
select 'SALE_ORDER_ITEM', t.settingsJSONData from "anv".itemtable_settings t where t.section='SALE_QUOTE_ITEM';


insert into "0_template".itemtable_settings (section, settingsJSONData)
select 'SALE_ORDER_ITEM', t.itemtable_settings from "0_template".itemtable_settings t where t.section='SALE_QUOTE_ITEM';



insert into "0".itemtable_settings (section, settingsJSONData)
values ('SALE_ORDER_ITEM','[{"code":"PRODUCT","title":"Item","selected":true,"required":true,"order":1},{"code":"string_value1","title":"Category","selected":false,"required":false,"order":999},{"code":"DESCRIPTION"
,"title":"Description","selected":true,"required":false,"order":2},{"code":"QTY","title":"Qty","selected":true,"required":true,"order":3},{"code":"UNITPRICE","title":"Price","selected":true
,"required":true,"order":4},{"code":"DISCOUNT_AMT","title":"Discount","selected":true,"required":false,"order":5},{"code":"NET_AMT","title":"Net Amount","selected":true,"required":true,"ord
er":6},{"code":"TAX_LIST","title":"Tax Rate","selected":true,"required":false,"order":7}]');
