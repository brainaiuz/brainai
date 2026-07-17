
delete from "anv".reference  where code = '_UAE_TAX_PERIOD';
insert into "anv".reference(code, deleted, isremovable, name, shared, isactive)
							values('_UAE_TAX_PERIOD', false, true, 'UAE Tax Period', true, true);
insert into "anv".reference(code, deleted, isremovable, name, shared, isactive, parentid)
							values('MONTHLY', false, true, 'Monthly', true, true, (select id from "anv".reference where code = '_UAE_TAX_PERIOD'));
insert into "anv".reference(code, deleted, isremovable, name, shared, isactive, parentid)
							values('CUSTOM', false, true, 'Custom', true, true, (select id from "anv".reference where code = '_UAE_TAX_PERIOD'));


delete from "0".reference  where code = '_UAE_TAX_PERIOD';
insert into "0".reference(code, deleted, isremovable, name, shared, isactive)
							values('_UAE_TAX_PERIOD', false, true, 'UAE Tax Period', true, true);
insert into "0".reference(code, deleted, isremovable, name, shared, isactive, parentid)
							values('MONTHLY', false, true, 'Monthly', true, true, (select id from "0".reference where code = '_UAE_TAX_PERIOD'));
insert into "0".reference(code, deleted, isremovable, name, shared, isactive, parentid)
							values('CUSTOM', false, true, 'Custom', true, true, (select id from "0".reference where code = '_UAE_TAX_PERIOD'));


