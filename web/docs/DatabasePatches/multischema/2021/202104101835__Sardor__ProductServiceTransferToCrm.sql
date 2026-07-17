DELETE
FROM "anv".container_item
WHERE modulecode = 'crm'
  and moduleid = (select id
                  from "anv".mymodule
                  where code = 'PRODUCTS_SERVICES_CRM'
                    and section = 'crm'
                    and name = 'Products services')
  and propertyid = (select id
                    from "anv".property
                    where defaultname = 'Product/Service'
                      and modulecode = 'accounting'
                      and objectname = 'productsOrServices');

DELETE
FROM "anv".mymodule
WHERE code = 'PRODUCTS_SERVICES_CRM'
  and section = 'crm';

DELETE
FROM permission
WHERE code = 'CRM_PRODUCT_LIST'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('CRM_PRODUCT_LIST', 'CRM', 'Products/Services List', 15,
        (select id from permission where code = 'CRM_SALES_TAB' and context = 'CRM' and modulecode = 'CRM_MODULE'),
        'PRODUCTS_SERVICES_CRM');

INSERT INTO "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
VALUES (true, 'crm', 8, (select id
                         from "anv".container
                         where code = 'crmWelcome'
                           and defaultname = 'sales'
                           and modulecode = 'crm'
                           and preparedview = 'leadList'), (select id
                                                            from "anv".mymodule
                                                            where code = 'PRODUCTS_SERVICES_CRM'
                                                              and name = 'Products services'
                                                              and section = 'crm'), (select id
                                                                                     from "anv".property
                                                                                     where defaultname = 'Product/Service'
                                                                                       and modulecode = 'accounting'
                                                                                       and objectname = 'productsOrServices'));

INSERT INTO "anv".mymodule(code, active, name, section, sorder)
VALUES ('PRODUCTS_SERVICES_CRM', true, 'Products services', 'crm', 0);

delete
from permission
where code = 'ACCOUNTING_PRODUCT_ADD'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_PRODUCT_ADD', 'CRM', 'Add', 1, (select id from permission where code = 'CRM_PRODUCT_LIST'),
        'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_PRODUCT_QUICK_ADD'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_PRODUCT_QUICK_ADD', 'CRM', 'Quick Add', 2,
        (select id from permission where code = 'CRM_PRODUCT_LIST'), 'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_PRODUCT_EDIT'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_PRODUCT_EDIT', 'CRM', 'Edit', 3, (select id from permission where code = 'CRM_PRODUCT_LIST'),
        'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_PRODUCT_DELETE'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_PRODUCT_DELETE', 'CRM', 'Delete', 4, (select id from permission where code = 'CRM_PRODUCT_LIST'),
        'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_PRODUCT_SUMMARY'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_PRODUCT_SUMMARY', 'CRM', 'Summary', 5, (select id from permission where code = 'CRM_PRODUCT_LIST'),
        'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'HIDE_PRODUCT_PRICE'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HIDE_PRODUCT_PRICE', 'CRM', 'Hide Price', 6, (select id from permission where code = 'CRM_PRODUCT_LIST'),
        'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_VARIATION_ADD'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_VARIATION_ADD', 'CRM', 'Variation Add', 7,
        (select id from permission where code = 'CRM_PRODUCT_LIST'), 'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_VARIATION_DELETE'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_VARIATION_DELETE', 'CRM', 'Variation Delete', 8,
        (select id from permission where code = 'CRM_PRODUCT_LIST'), 'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_INVENTORY_LIST'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_INVENTORY_LIST', 'CRM', 'Inventory List', 9,
        (select id from permission where code = 'CRM_PRODUCT_LIST'), 'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_BUILD_ASSEMBLY'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_BUILD_ASSEMBLY', 'CRM', 'Build Assembly', 10,
        (select id from permission where code = 'CRM_PRODUCT_LIST'), 'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_PRODUCT_COST'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_PRODUCT_COST', 'CRM', 'Cost', 11, (select id from permission where code = 'CRM_PRODUCT_LIST'),
        'PRODUCTS_SERVICES_CRM');

delete
from permission
where code = 'ACCOUNTING_PRODUCT_HISTORY_LIST'
  and context = 'CRM'
  and modulecode = 'PRODUCTS_SERVICES_CRM';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_PRODUCT_HISTORY_LIST', 'CRM', 'History List', 12,
        (select id from permission where code = 'CRM_PRODUCT_LIST'), 'PRODUCTS_SERVICES_CRM');

