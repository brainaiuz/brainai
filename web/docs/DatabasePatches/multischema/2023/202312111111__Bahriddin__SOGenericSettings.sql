
delete from "anv".genericsettings where key ='ENABLE_ADD_CUSTOMER_PREPAYMENT_FROM_SO';
insert into "anv".genericsettings(key,value) values ('ENABLE_ADD_CUSTOMER_PREPAYMENT_FROM_SO', 'NO');