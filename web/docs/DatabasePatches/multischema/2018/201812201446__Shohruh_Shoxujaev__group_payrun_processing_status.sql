delete from "0".reference where code='PY_PROCESSING';

insert into "0".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PY_PROCESSING', false, true, false, true, true, 'Processing', true, 5, (select id from "0".reference where code = 'PAYRUN_STATUS'), 0.00, false, false);

delete from "anv".reference where code='PY_PROCESSING';

insert into "anv".reference (code, deleted, isactive, iscustombutton, isremovable, isSystemReference, name, shared, sorder, parentid, leavedays, includedayoffs, includeholidays)
values ('PY_PROCESSING', false, true, false, true, true, 'Processing', true, 5, (select id from "anv".reference where code = 'PAYRUN_STATUS'), 0.00, false, false);