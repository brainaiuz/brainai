
update "anv".salequote set issalesorder = true where id in (select id from "anv".quote
where status_id=(select id from "anv".reference where code='SALE_ORDER')
      or status_id=(select id from "anv".reference where code='PICKED')
      or status_id=(select id from "anv".reference where code='PACKED')
      or status_id=(select id from "anv".reference where code='SHIPPED')
      or status_id=(select id from "anv".reference where code='PARTIAL_SHIPPED'));


update "anv".salequote
set issalesorder = true
where id in (
  select q.id
  from "anv".quote q
         left join "anv".shipping_data shp on shp.quoteid = q.id
         left join "anv".converted_shipping_data csh on shp.id = csh.shipping_data_id
  where q.status_id = (select id from "anv".reference where code = 'INVOICE_STATUS_INVOICED')
  and csh.invoice_id is not null
);