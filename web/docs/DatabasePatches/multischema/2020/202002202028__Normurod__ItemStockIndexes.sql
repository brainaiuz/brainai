
CREATE INDEX item_stock_search_idx ON "anv".item_stock(item_id, warehouseid, transactionid, transaction_code, date, transaction_date);