delete from "anv".reference  where code = '_WORKFLOW_MODULE_PRODUCT_CATEGORY' and parentid = (select id from "anv".reference where code='_WORKFLOW_MODULE');

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_PRODUCT_CATEGORY', false, true, 'Product Category', true, 6, (select id from "anv".reference where code='_WORKFLOW_MODULE'), true);
