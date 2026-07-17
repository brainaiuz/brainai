INSERT INTO "anv".reference_locale (arabic, english, russian, uzbek)
SELECT 'متأخر', 'Late', 'Опоздавший', 'Kech qolgan'
WHERE NOT EXISTS (SELECT "id" from "anv".reference_locale WHERE english='Late' AND russian='Опоздавший');

INSERT INTO "anv".reference_locale (arabic, english, russian, uzbek)
SELECT 'غادر مبكرًا', 'Early leave', 'Рано ушедший', 'Erta ketgan'
WHERE NOT EXISTS (SELECT "id" from "anv".reference_locale WHERE english='Early leave' AND russian='Рано ушедший');


insert into  "anv".leave_reason (attendancelr, autoapprove, code, color, deleted, description, gender, hasprorata, includedayoffs,
                                 includeholidays, isactive, issystemreference, leavedays, name, probationdays, shortname,
                                 typeoption, unittype, updateddate, mark_as_draft)
select false, false, 'LATE', '#e73532', false, 'LATE',
       null, false, false, false, true, true, 0.00, 'Late', 0.00, 'L', 'NOT_ALLOW_EXCEED_ALLOWANCE', null, null, false
where NOT EXISTS (SELECT id FROM  "anv".leave_reason where code='LATE');

UPDATE "anv".leave_reason
SET localeid = (SELECT "id" FROM "anv".reference_locale where "english" = 'Late' and "russian" = 'Опоздавший')
WHERE "code" = 'LATE';



insert into  "anv".leave_reason (attendancelr, autoapprove, code, color, deleted, description, gender, hasprorata, includedayoffs,
                                 includeholidays, isactive, issystemreference, leavedays, name, probationdays, shortname,
                                 typeoption, unittype, updateddate, mark_as_draft)
select false, false, 'EARLY_LEAVE', '#e73532', false, 'EARLY_LEAVE',
       null, false, false, false, true, true, 0.00, 'Late', 0.00, 'EL', 'NOT_ALLOW_EXCEED_ALLOWANCE', null, null, false
where NOT EXISTS (SELECT id FROM  "anv".leave_reason where code='EARLY_LEAVE');

UPDATE "anv".leave_reason
SET localeid = (SELECT "id" FROM "anv".reference_locale where "english" = 'Early leave' and "russian" = 'Рано ушедший')
WHERE "code" = 'EARLY_LEAVE';