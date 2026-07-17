INSERT INTO "anv".reference_locale (arabic, english, russian, uzbek)
SELECT '• لا يوجد تسجيل دخول', 'No Check-in', 'Нет отметки входа', 'Kirish qayd etilmagan'
WHERE NOT EXISTS (SELECT "id" from "anv".reference_locale WHERE english='No Check-in' AND russian='Нет отметки входа');


insert into  "anv".leave_reason (attendancelr, autoapprove, code, color, deleted, description, gender, hasprorata, includedayoffs,
                                   includeholidays, isactive, issystemreference, leavedays, name, probationdays, shortname,
                                   typeoption, unittype, updateddate, mark_as_draft)
select false, false, 'NO_CHECK_IN', '#F59E0B', false, 'NO_CHECK_IN',
       null, false, false, false, true, true, 0.00, 'No Check-in', 0.00, 'NC', 'NOT_ALLOW_EXCEED_ALLOWANCE', null, null, false
where NOT EXISTS (SELECT id FROM  "anv".leave_reason where code='NO_CHECK_IN');

UPDATE "anv".leave_reason
SET localeid = (SELECT "id" FROM "anv".reference_locale where "english" = 'No Check-in' and "russian" = 'Нет отметки входа')
WHERE "code" = 'NO_CHECK_IN';