delete from  "anv".leave_reason where code='LR_TYPE_RECALL_LEAVE';
INSERT INTO "anv".leave_reason
(name, code, shortname, description, attendancelr, autoapprove, color, deleted, gender, hasprorata, includedayoffs, includeholidays, isactive, issystemreference, leavedays, probationdays,  typeoption, unittype, updateddate)
select 'Отозвать сотрудника', 'LR_TYPE_RECALL_LEAVE', 'REC', 'Отозвать сотрудника', attendancelr, autoapprove, color, deleted, gender, hasprorata, includedayoffs, includeholidays, isactive, issystemreference, leavedays, probationdays,  typeoption, unittype, updateddate
from  "anv".leave_reason where code='LR_TYPE_ANNUAL_LEAVE';