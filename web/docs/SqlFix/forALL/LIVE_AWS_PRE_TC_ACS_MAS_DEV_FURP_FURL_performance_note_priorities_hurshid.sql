
delete from "0".reference where code in ('_PERFORMANCE_NOTE_PRIORITIES', 'PN_CRITICAL', 'PN_HIGH', 'PN_MEDIUM', 'PN_LOW');
insert into "0".reference (code, name, isremovable, issystemreference) values ('_PERFORMANCE_NOTE_PRIORITIES', 'Performance Note Priorities', false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('PN_CRITICAL', 'Critical', (select id from "0".reference r where r.code = '_PERFORMANCE_NOTE_PRIORITIES'), 1, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('PN_HIGH', 'High', (select id from "0".reference r where r.code = '_PERFORMANCE_NOTE_PRIORITIES'), 2, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('PN_MEDIUM', 'Medium', (select id from "0".reference r where r.code = '_PERFORMANCE_NOTE_PRIORITIES'), 3, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('PN_LOW', 'Low', (select id from "0".reference r where r.code = '_PERFORMANCE_NOTE_PRIORITIES'), 4, false, true);



delete from "anv".reference where code in ('_PERFORMANCE_NOTE_PRIORITIES', 'PN_CRITICAL', 'PN_HIGH', 'PN_MEDIUM', 'PN_LOW');
insert into "anv".reference (code, name, isremovable, issystemreference) values ('_PERFORMANCE_NOTE_PRIORITIES', 'Performance Note Priorities', false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('PN_CRITICAL', 'Critical', (select id from "anv".reference r where r.code = '_PERFORMANCE_NOTE_PRIORITIES'), 1, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('PN_HIGH', 'High', (select id from "anv".reference r where r.code = '_PERFORMANCE_NOTE_PRIORITIES'), 2, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('PN_MEDIUM', 'Medium', (select id from "anv".reference r where r.code = '_PERFORMANCE_NOTE_PRIORITIES'), 3, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('PN_LOW', 'Low', (select id from "anv".reference r where r.code = '_PERFORMANCE_NOTE_PRIORITIES'), 4, false, true);


