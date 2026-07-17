delete from "anv".itemtable_settings where section = 'RENTAL_ORDER_ITEM';
insert into "anv".itemtable_settings (section,settingsjsondata) values ('RENTAL_ORDER_ITEM', '[
  {
    "code": "PRODUCT",
    "title": "Item",
    "selected": true,
    "required": true,
    "order": 1
  },
  {
    "code": "DESCRIPTION",
    "title": "Description",
    "selected": true,
    "required": false,
    "order": 2
  },
  {
    "code": "QTY",
    "title": "Qty",
    "selected": true,
    "required": true,
    "order": 3
  },
  {
    "code": "UNITPRICE",
    "title": "Price",
    "selected": true,
    "required": true,
    "order": 4
  },
  {
    "code": "TAX_LIST",
    "title": "Tax Rate",
    "selected": true,
    "required": false,
    "order": 5
  },
  {
    "code": "NET_AMT",
    "title": "Net",
    "selected": true,
    "required": false,
    "order": 6
  },
  {
    "code": "TOTAL_AMT",
    "title": "Total",
    "selected": true,
    "required": false,
    "order": 7
  }
]');