delete from "0".reference where code='PY_PARTIAL_PAID';

insert into "0".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PY_PARTIAL_PAID', false, true, false, true, true, 'Partially paid', true, 6, (select id from "0".reference where code = 'PAYRUN_STATUS'), 0.00, false, false);

delete from "anv".reference where code='PY_PARTIAL_PAID';

insert into "anv".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PY_PARTIAL_PAID', false, true, false, true, true, 'Partially paid', true, 6, (select id from "anv".reference where code = 'PAYRUN_STATUS'), 0.00, false, false);



delete from "0".reference where code='PY_PAID';

insert into "0".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PY_PAID', false, true, false, true, true, 'Paid', true, 7, (select id from "0".reference where code = 'PAYRUN_STATUS'), 0.00, false, false);

delete from "anv".reference where code='PY_PAID';

insert into "anv".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PY_PAID', false, true, false, true, true, 'Paid', true, 7, (select id from "anv".reference where code = 'PAYRUN_STATUS'), 0.00, false, false);