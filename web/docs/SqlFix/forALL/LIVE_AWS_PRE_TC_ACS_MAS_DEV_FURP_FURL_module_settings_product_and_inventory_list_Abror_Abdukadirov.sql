delete from "anv".mymodule where code = 'PRODUCTS_SERVICES';
delete from "anv".mymodule where code = 'PRODUCT_INVENTORY_ITEMS';
insert into "anv".mymodule(code) values('PRODUCTS_SERVICES');
insert into "anv".mymodule(code) values('PRODUCT_INVENTORY_ITEMS');

delete from "0".mymodule where code = 'PRODUCTS_SERVICES';
delete from "0".mymodule where code = 'PRODUCT_INVENTORY_ITEMS';
insert into "0".mymodule(code) values('PRODUCTS_SERVICES');
insert into "0".mymodule(code) values('PRODUCT_INVENTORY_ITEMS');


update permission set modulecode = 'PRODUCT_INVENTORY_ITEMS' where code = 'ACCOUNTING_INVENTORY_LIST';

update permission set modulecode = 'PRODUCTS_SERVICES' where code = 'ACCOUNTING_PRODUCT_LIST';
update permission set modulecode = 'PRODUCTS_SERVICES' where code = 'ACCOUNTING_PRODUCT_ADD';
update permission set modulecode = 'PRODUCTS_SERVICES' where code = 'ACCOUNTING_PRODUCT_GROUP_ADD';
update permission set modulecode = 'PRODUCTS_SERVICES' where code = 'ACCOUNTING_PRODUCT_EDIT';
update permission set modulecode = 'PRODUCTS_SERVICES' where code = 'ACCOUNTING_PRODUCT_DELETE';
update permission set modulecode = 'PRODUCTS_SERVICES' where code = 'ACCOUNTING_PRODUCT_SUMMARY';
update permission set modulecode = 'PRODUCTS_SERVICES' where code = 'ACCOUNTING_VARIATION_ADD';
update permission set modulecode = 'PRODUCTS_SERVICES' where code = 'ACCOUNTING_VARIATION_DELETE';
update permission set modulecode = 'PRODUCTS_SERVICES' where code = 'ACCOUNTING_BUILD_ASSEMBLY';