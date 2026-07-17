-- References added for Backups Employee Status
delete
from "anv".reference
where code = 'BACKUPS_EMPLOYEE';
insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('BACKUPS_EMPLOYEE_STATUS', false, 'Backup Employee', 'Backup Employee', false, false, true, 0, null, false, true);



insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('BACKUPS_EMPLOYEE_REJECTED', false, 'Backup Employee Rejected', 'Backup Employee Rejected', false, false, true, 1,
        (select r.id from "anv".reference r where r.code = 'BACKUPS_EMPLOYEE_STATUS' order by r.id desc limit 1), false, true),
        
('BACKUPS_EMPLOYEE_SUBMITTED', false, 'Backup Employee Submitted', 'Backup Employee Submitted', false, false, true, 2,
(select r.id from "anv".reference r where r.code = 'BACKUPS_EMPLOYEE_STATUS' order by r.id desc limit 1), false, true),

('BACKUPS_EMPLOYEE_APPROVED', false, 'Backup Employee Approved', 'Backup Employee Approved', false, false, true, 3,
(select r.id from "anv".reference r where r.code = 'BACKUPS_EMPLOYEE_STATUS' order by r.id desc limit 1), false, true),

('BACKUPS_EMPLOYEE_DRAFT', false, 'Backup Employee Draft', 'Backup Employee Draft', false, false, true, 4,
(select r.id from "anv".reference r where r.code = 'BACKUPS_EMPLOYEE_STATUS' order by r.id desc limit 1), false, true);