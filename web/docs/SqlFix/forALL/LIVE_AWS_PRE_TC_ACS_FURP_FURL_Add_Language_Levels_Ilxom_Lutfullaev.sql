insert into "0".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder, parentid, iscustombutton, isactive)
values('_LANGUAGE_LEVELS', false, 'Language levels', 'Language levels', false, false, true, 0, null, false, true);

insert into "0".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder, parentid, iscustombutton, isactive) values
('BEGINNER', false, 'Beginner', 'Beginner', false, false, true, 1,
(select r.id from "0".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('ELEMENTARY', false, 'Elementary', 'Elementary', false, false, true, 2,
(select r.id from "0".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('INTERMEDIATE', false, 'Intermediate', 'Intermediate', false, false, true, 3,
(select r.id from "0".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('UPPER_INTERMEDIATE', false, 'Upper Intermediate', 'Upper Intermediate', false, false, true, 4,
(select r.id from "0".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('ADVANCED', false, 'Advanced', 'Advanced', false, false, true, 5,
(select r.id from "0".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('PROFICIENT', false, 'Proficient', 'Proficient', false, false, true, 6,
(select r.id from "0".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true);



------ For All Schema -------------------------------
insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder, parentid, iscustombutton, isactive)
values('_LANGUAGE_LEVELS', false, 'Language levels', 'Language levels', false, false, true, 0, null, false, true);

insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder, parentid, iscustombutton, isactive) values
('BEGINNER', false, 'Beginner', 'Beginner', false, false, true, 1,
(select r.id from "anv".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('ELEMENTARY', false, 'Elementary', 'Elementary', false, false, true, 2,
(select r.id from "anv".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('INTERMEDIATE', false, 'Intermediate', 'Intermediate', false, false, true, 3,
(select r.id from "anv".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('UPPER_INTERMEDIATE', false, 'Upper Intermediate', 'Upper Intermediate', false, false, true, 4,
(select r.id from "anv".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('ADVANCED', false, 'Advanced', 'Advanced', false, false, true, 5,
(select r.id from "anv".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true),
('PROFICIENT', false, 'Proficient', 'Proficient', false, false, true, 6,
(select r.id from "anv".reference r where r.code = '_LANGUAGE_LEVELS' order by r.id desc limit 1), false, true);
