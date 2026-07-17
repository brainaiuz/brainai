
update "0".leave_reason set deleted=true where code='LR_TYPE_LATE';
update "0".leave_reason set deleted=true where code='LR_TYPE_OTHER_LEAVE';
update "0".leave_reason set deleted=true where code='LR_TYPE_SPECIAL';
update "0".leave_reason set deleted=true where code='LR_TYPE_STUDY_LEAVE';
update "0".leave_reason set name='Absent', shortName='AB', color='#f44336', typeoption='NOT_ALLOW_EXCEED_ALLOWANCE' where code='LR_TYPE_UNAUTHORIZED_LEAVE';
update "0".leave_reason set leavedays=30, color='#65B763', typeoption='ALLOW_AS_PAID' where code='LR_TYPE_ANNUAL_LEAVE';
update "0".leave_reason set leavedays=15, color='#337BE2', typeoption='NOT_ALLOW_EXCEED_ALLOWANCE' where code='LR_TYPE_SICK_LEAVE';

delete from "0".leave_reason where code='LR_TYPE_MATERNITY';
INSERT INTO "0".leave_reason(attendancelr, autoapprove, code, color, deleted, description,
            gender, hasprorata, includedayoffs, includeholidays, isactive,
            issystemreference, leavedays, name, probationdays, shortname,
            typeoption, unittype, updateddate)
    VALUES (false, false, 'LR_TYPE_MATERNITY', '#2C74DB', false, 'LR_TYPE_MATERNITY',
            'FEMALE', false, true, false, true,
            true, 45, 'Maternity', 0, 'ML',
            'ALLOW_AS_PAID', 'DAILY', null);
delete from "0".leave_reason_relation where reason_code='LR_TYPE_MATERNITY';
INSERT INTO "0".leave_reason_relation(reason_code, relatedtype, relationid) VALUES ('LR_TYPE_MATERNITY', 'ROLE', 6);





----------------------------------------------
update "64646".leave_reason set deleted=true where code='LR_TYPE_LATE';
update "64646".leave_reason set deleted=true where code='LR_TYPE_OTHER_LEAVE';
update "64646".leave_reason set deleted=true where code='LR_TYPE_SPECIAL';
update "64646".leave_reason set deleted=true where code='LR_TYPE_STUDY_LEAVE';
update "64646".leave_reason set name='Absent', shortName='AB', color='#f44336', typeoption='NOT_ALLOW_EXCEED_ALLOWANCE' where code='LR_TYPE_UNAUTHORIZED_LEAVE';
update "64646".leave_reason set leavedays=30, color='#65B763', typeoption='ALLOW_AS_PAID' where code='LR_TYPE_ANNUAL_LEAVE';
update "64646".leave_reason set leavedays=15, color='#337BE2', typeoption='NOT_ALLOW_EXCEED_ALLOWANCE' where code='LR_TYPE_SICK_LEAVE';

delete from "64646".leave_reason where code='LR_TYPE_MATERNITY';
INSERT INTO "64646".leave_reason(attendancelr, autoapprove, code, color, deleted, description,
            gender, hasprorata, includedayoffs, includeholidays, isactive,
            issystemreference, leavedays, name, probationdays, shortname,
            typeoption, unittype, updateddate)
    VALUES (false, false, 'LR_TYPE_MATERNITY', '#2C74DB', false, 'LR_TYPE_MATERNITY',
            'FEMALE', false, true, false, true,
            true, 45, 'Maternity', 0, 'ML',
            'ALLOW_AS_PAID', 'DAILY', null);

delete from "64646".leave_reason_relation where reason_code='LR_TYPE_MATERNITY';
INSERT INTO "64646".leave_reason_relation(reason_code, relatedtype, relationid) VALUES ('LR_TYPE_MATERNITY', 'ROLE', 6);