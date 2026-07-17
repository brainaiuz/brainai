
INSERT INTO "anv".reference_locale (arabic, english, russian, uzbek)
SELECT 'لم يبدأ الوقت المحدد', 'Timeslot not started', 'Рабочий график не начался', 'Ish grafigi boshlanmagan'
WHERE NOT EXISTS (SELECT "id" from "anv".reference_locale WHERE english='Timeslot not started' AND russian='Рабочий график не начался');


insert into  "anv".leave_reason (attendancelr, autoapprove, code, color, deleted, description, gender, hasprorata, includedayoffs,
                                   includeholidays, isactive, issystemreference, leavedays, name, probationdays, shortname,
                                   typeoption, unittype, updateddate, mark_as_draft)
select false, false, 'TIMESLOT_NOT_STARTED', '#22DA93', false, 'TIMESLOT_NOT_STARTED',
       null, false, false, false, true, true, 0.00, 'Timeslot not started', 0.00, 'TNS', 'NOT_ALLOW_EXCEED_ALLOWANCE', null, null, false
where NOT EXISTS (SELECT id FROM  "anv".leave_reason where code='TIMESLOT_NOT_STARTED');

UPDATE "anv".leave_reason
SET localeid = (SELECT "id" FROM "anv".reference_locale where "english" = 'Timeslot not started' and "russian" = 'Рабочий график не начался')
WHERE "code" = 'TIMESLOT_NOT_STARTED';
