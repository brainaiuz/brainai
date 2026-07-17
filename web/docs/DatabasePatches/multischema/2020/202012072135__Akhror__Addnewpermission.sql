insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('STOCK_TRANSFER_DRAFT_ADD',   'ACCOUNTING', false, 'Add Draft', 15, (select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'), true, 'INVENTORY_MANAGEMENT');

insert into "anv".permission_context(permissioncode,contextcode) values ('STOCK_TRANSFER_DRAFT_ADD','ACCOUNTING');
