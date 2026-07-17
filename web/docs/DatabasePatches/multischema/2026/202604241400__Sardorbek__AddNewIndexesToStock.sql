create index if not exists idx_item_stock_item_id on "anv".item_stock(item_id);
create index if not exists idx_item_stock_warehouse on "anv".item_stock(item_id, warehouseid);
create index if not exists idx_discount_applied_products_product on "anv".discount_applied_products(product_id);