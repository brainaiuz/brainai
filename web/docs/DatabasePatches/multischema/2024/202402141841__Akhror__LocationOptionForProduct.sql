insert into "anv".modelfield(columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory, hide)
values ('COL_1', 'LOCATION',
        (select max(forder) + 1 from "anv".modelfield where form_id = 'PRODUCT' and fsection = 'MORE_OPTIONS'),
        'PRODUCT', 'MORE_OPTIONS', false, false, true);

delete
from permission
where code in ('PRODUCT_SEE_ALL', 'PRODUCT_SEE_OWN_LOCATION');
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PRODUCT_SEE_ALL', 'ACCOUNTING', false, 'See All',
        (select count(id)
         from permission
         where parent = (select id from permission where code = 'PRODUCTS_SERVICES')) +
        1,
        (select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), false,
        'PRODUCTS_SERVICES'),
       ('PRODUCT_SEE_OWN_LOCATION', 'ACCOUNTING', false, 'See Own Location',
        (select count(id)
         from permission
         where parent = (select id from permission where code = 'PRODUCTS_SERVICES')) +
        1,
        (select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), false,
        'PRODUCTS_SERVICES');

delete
from "anv".permission_context
where permissioncode in ('PRODUCT_SEE_ALL', 'PRODUCT_SEE_OWN_LOCATION');
insert into "anv".permission_context(permissioncode, contextcode)
values ('PRODUCT_SEE_OWN_LOCATION', 'ACCOUNTING'),
       ('PRODUCT_SEE_ALL', 'ACCOUNTING');