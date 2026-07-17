--  New generic settings for product add/edit action
delete from "anv".genericsettings where key ='PRODUCT_MATERIALS_BY_LOCATION_IN_ADD_VIEW';
insert into "anv".genericsettings(key,value) values ('PRODUCT_MATERIALS_BY_LOCATION_IN_ADD_VIEW', 'NO');