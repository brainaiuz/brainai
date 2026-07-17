delete from "0".reference where code='PAYMENT_PARTIAL_PAID';
insert into "0".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PAYMENT_PARTIAL_PAID', false, true, false, true, true, 'Partially paid', true, 5, (select id from "0".reference where code = 'PAYMENT_STATUS'), 0.00, false, false);

delete from "anv".reference where code='PAYMENT_PARTIAL_PAID';
insert into "anv".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PAYMENT_PARTIAL_PAID', false, true, false, true, true, 'Partially paid', true, 5, (select id from "anv".reference where code = 'PAYMENT_STATUS'), 0.00, false, false);



delete from "0".reference where code='PAYMENT_PAID';
insert into "0".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PAYMENT_PAID', false, true, false, true, true, 'Paid', true, 6, (select id from "0".reference where code = 'PAYMENT_STATUS'), 0.00, false, false);

delete from "anv".reference where code='PAYMENT_PAID';
insert into "anv".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PAYMENT_PAID', false, true, false, true, true, 'Paid', true, 6, (select id from "anv".reference where code = 'PAYMENT_STATUS'), 0.00, false, false);