insert into "anv".reference(code, isactive, issystemreference, name, shared, sorder, parentid)
values ('PENDING', true, true, 'Pending', true, 5,
        (select id from "anv".reference where code = 'STOCK_ADJUSTMENT_STATUS'));

insert into "anv".reference(code, isactive, issystemreference, name, shared, sorder, parentid)
values ('FAILED', true, true, 'Failed', true, 6,
        (select id from "anv".reference where code = 'STOCK_ADJUSTMENT_STATUS'));

insert into "anv".reference(code, isactive, issystemreference, name, shared, sorder, parentid)
values ('PENDING', true, true, 'Pending', true, 6,
        (select id from "anv".reference where code = 'STOCK_TRANSFER_STATUS'));

insert into "anv".reference(code, isactive, issystemreference, name, shared, sorder, parentid)
values ('FAILED', true, true, 'Failed', true, 7, (select id from "anv".reference where code = 'STOCK_TRANSFER_STATUS'));