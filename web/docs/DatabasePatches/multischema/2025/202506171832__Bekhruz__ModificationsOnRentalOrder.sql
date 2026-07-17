delete from "0".itemtable_settings where section = 'RENTAL_ORDER_ITEM';
insert into "0".itemtable_settings (section,settingsjsondata) values ('RENTAL_ORDER_ITEM', '[
	{
    "code": "BRAND",
    "title": "Brand",
    "selected": true,
    "required": false,
    "order": 1
  },
  {
    "code": "CATEGORY",
    "title": "Category",
    "selected": true,
    "required": false,
    "order": 2
  },
  {
    "code": "PRODUCT",
    "title": "Item",
    "selected": true,
    "required": true,
    "order": 3
  },
  {
    "code": "DESCRIPTION",
    "title": "Description",
    "selected": true,
    "required": false,
    "order": 4
  },
  {
    "code": "QTY",
    "title": "Qty",
    "selected": true,
    "required": true,
    "order": 5
  },
  {
    "code": "UNITPRICE",
    "title": "Price",
    "selected": true,
    "required": true,
    "order": 6
  },
  {
    "code": "TAX_LIST",
    "title": "Tax Rate",
    "selected": true,
    "required": false,
    "order": 7
  },
  {
    "code": "NET_AMT",
    "title": "Net",
    "selected": true,
    "required": false,
    "order": 8
  },
  {
    "code": "TOTAL_AMT",
    "title": "Total",
    "selected": true,
    "required": false,
    "order": 9
  }
]');




delete from "anv".itemtable_settings where section = 'RENTAL_ORDER_ITEM';
insert into "anv".itemtable_settings (section,settingsjsondata) values ('RENTAL_ORDER_ITEM', '[
	{
    "code": "BRAND",
    "title": "Brand",
    "selected": true,
    "required": false,
    "order": 1
  },
  {
    "code": "CATEGORY",
    "title": "Category",
    "selected": true,
    "required": false,
    "order": 2
  },
  {
    "code": "PRODUCT",
    "title": "Item",
    "selected": true,
    "required": true,
    "order": 3
  },
  {
    "code": "DESCRIPTION",
    "title": "Description",
    "selected": true,
    "required": false,
    "order": 4
  },
  {
    "code": "QTY",
    "title": "Qty",
    "selected": true,
    "required": true,
    "order": 5
  },
  {
    "code": "UNITPRICE",
    "title": "Price",
    "selected": true,
    "required": true,
    "order": 6
  },
  {
    "code": "TAX_LIST",
    "title": "Tax Rate",
    "selected": true,
    "required": false,
    "order": 7
  },
  {
    "code": "NET_AMT",
    "title": "Net",
    "selected": true,
    "required": false,
    "order": 8
  },
  {
    "code": "TOTAL_AMT",
    "title": "Total",
    "selected": true,
    "required": false,
    "order": 9
  }
]');