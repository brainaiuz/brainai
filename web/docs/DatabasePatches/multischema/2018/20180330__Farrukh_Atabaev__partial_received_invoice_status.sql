update "0".reference  set name='Partial Received' where code='PARTIAL_RECEIVED' and parentid=(select id from "0".reference where code='INVOICE_STATUS');

update "anv".reference  set name='Partial Received' where code='PARTIAL_RECEIVED' and parentid=(select id from "anv".reference where code='INVOICE_STATUS');