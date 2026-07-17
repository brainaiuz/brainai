--  In the future we might need this that is why i added
delete from "anv".genericsettings where key ='CAN_EDIT_SALES_ORDER_IF_HAS_INVOICE_WORKAROUND';
insert into "anv".genericsettings(key,value) values ('CAN_EDIT_SALES_ORDER_IF_HAS_INVOICE_WORKAROUND', 'NO');