
delete from "0".reference where code in ('N/A', 'UNA', 'VWK', 'WK', 'SAT', 'GD', 'VGD', 'EXL') and parentid in (select id from "0".reference r where r.code = '_ASSASSMENT_RATINGS');
delete from "0".reference where code='_ASSASSMENT_RATINGS';

insert into "0".reference (code, name, isremovable, issystemreference) values ('_ASSASSMENT_RATINGS', 'Assassment ratings', false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('N/A', 'N/A',          (select id from "0".reference r where r.code = '_ASSASSMENT_RATINGS'), 1, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('UNA', 'Unacceptable', (select id from "0".reference r where r.code = '_ASSASSMENT_RATINGS'), 2, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('VWK', 'Very weak',    (select id from "0".reference r where r.code = '_ASSASSMENT_RATINGS'), 3, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('WK', 'Weak',          (select id from "0".reference r where r.code = '_ASSASSMENT_RATINGS'), 4, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('SAT', 'Satisfactory', (select id from "0".reference r where r.code = '_ASSASSMENT_RATINGS'), 5, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('GD', 'Good',          (select id from "0".reference r where r.code = '_ASSASSMENT_RATINGS'), 6, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('VGD', 'Very good',    (select id from "0".reference r where r.code = '_ASSASSMENT_RATINGS'), 7, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('EXL', 'Excellent',    (select id from "0".reference r where r.code = '_ASSASSMENT_RATINGS'), 8, false, true);


delete from "anv".reference where code in ('N/A', 'UNA', 'VWK', 'WK', 'SAT', 'GD', 'VGD', 'EXL') and parentid in (select id from "anv".reference r where r.code = '_ASSASSMENT_RATINGS');
delete from "anv".reference where code='_ASSASSMENT_RATINGS';

insert into "anv".reference (code, name, isremovable, issystemreference) values ('_ASSASSMENT_RATINGS', 'Assassment ratings', false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('N/A', 'N/A',          (select id from "anv".reference r where r.code = '_ASSASSMENT_RATINGS'), 1, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('UNA', 'Unacceptable', (select id from "anv".reference r where r.code = '_ASSASSMENT_RATINGS'), 2, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('VWK', 'Very weak',    (select id from "anv".reference r where r.code = '_ASSASSMENT_RATINGS'), 3, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('WK', 'Weak',          (select id from "anv".reference r where r.code = '_ASSASSMENT_RATINGS'), 4, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('SAT', 'Satisfactory', (select id from "anv".reference r where r.code = '_ASSASSMENT_RATINGS'), 5, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('GD', 'Good',          (select id from "anv".reference r where r.code = '_ASSASSMENT_RATINGS'), 6, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('VGD', 'Very good',    (select id from "anv".reference r where r.code = '_ASSASSMENT_RATINGS'), 7, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('EXL', 'Excellent',    (select id from "anv".reference r where r.code = '_ASSASSMENT_RATINGS'), 8, false, true);
