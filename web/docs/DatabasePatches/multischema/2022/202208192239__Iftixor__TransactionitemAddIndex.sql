
DROP INDEX "anv".transaction_item_account_idx;
CREATE INDEX transactionitem_transaction_idx ON  "anv".transactionitem (transactionid);