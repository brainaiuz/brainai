

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
    VALUES ('RENTAL_STATUS', 'Rental status', true, true,true,false, null );

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
    VALUES ('RENTAL_APPROVED', 'Rental approved', true, true,true,false,(select id from "anv".reference where code = 'RENTAL_STATUS'));

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
    VALUES ('RENTAL_DELIVERY', 'Rental delivery', true, true,true,false,(select id from "anv".reference where code = 'RENTAL_STATUS'));

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
    VALUES ('RENTAL_RETURNED', 'Rental returned', true, true,true,false,(select id from "anv".reference where code = 'RENTAL_STATUS'));
