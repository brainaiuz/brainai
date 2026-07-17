---For ZERO Schema---
INSERT INTO "0".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
    VALUES ('PARTIAL_SHIPPED', 'Partial Shipped', true, true,true,false,(select id from "0".reference where code = 'INVOICE_STATUS'));

---For All Schema---
INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
    VALUES ('PARTIAL_SHIPPED', 'Partial Shipped', true, true,true,false,(select id from "anv".reference where code = 'INVOICE_STATUS'));