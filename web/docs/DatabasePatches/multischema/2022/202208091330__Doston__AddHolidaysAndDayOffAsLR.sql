-- Insert holiday leave type

insert into  "anv".leave_reason (attendancelr, autoapprove, code, color, deleted, description, gender, hasprorata, includedayoffs,
includeholidays, isactive, issystemreference, leavedays, name, probationdays, shortname,
typeoption, unittype, updateddate, mark_as_draft)
select false, false, 'LR_TYPE_HOLIDAY', 'f4a523', false, 'LR_TYPE_HOLIDAY',
null, false, null, null, true, true, 0.00, 'Holiday', 0.00, 'H', 'NOT_ALLOW_EXCEED_ALLOWANCE', null, null, true
where NOT EXISTS (SELECT id FROM  "anv".leave_reason where code='LR_TYPE_HOLIDAY');

-- Insert day off leave type

insert into  "anv".leave_reason (attendancelr, autoapprove, code, color, deleted, description, gender, hasprorata, includedayoffs,
includeholidays, isactive, issystemreference, leavedays, name, probationdays, shortname,
typeoption, unittype, updateddate, mark_as_draft)
select false, false, 'LR_TYPE_DAY_OFF', '000000', false, 'LR_TYPE_DAY_OFF',
null, false, null, null, true, true, 0.00, 'Day off', 0.00, 'DO', 'NOT_ALLOW_EXCEED_ALLOWANCE', null, null, true
where NOT EXISTS (SELECT id FROM  "anv".leave_reason where code='LR_TYPE_DAY_OFF');

-- Insert translation for holiday leave type

INSERT INTO "anv".reference_locale (arabic, english, russian, uzbek)
SELECT 'يوم الاجازة', 'Holiday', 'Праздник', 'Bayram' WHERE NOT EXISTS (SELECT "id" from "anv".reference_locale WHERE english='Holiday' AND russian='Праздник');

UPDATE "anv".leave_reason
SET localeid = (SELECT "id" FROM "anv".reference_locale where "english" = 'Holiday' and "russian" = 'Праздник')
WHERE "code" = 'LR_TYPE_HOLIDAY';

-- Insert translation for day off

INSERT INTO "anv".reference_locale (arabic, english, russian, uzbek)
SELECT 'يوم عطلة', 'Day off', 'Выходной', 'Dam kuni' WHERE NOT EXISTS (SELECT "id" from "anv".reference_locale WHERE english='Day off' AND russian='Выходной');

UPDATE "anv".leave_reason
SET localeid = (SELECT "id" FROM "anv".reference_locale where "english" = 'Day off' and "russian" = 'Выходной')
WHERE "code" = 'LR_TYPE_DAY_OFF';