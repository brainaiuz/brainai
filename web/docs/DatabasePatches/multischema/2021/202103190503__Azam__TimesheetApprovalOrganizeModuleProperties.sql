delete from "anv".property where objectName = 'timesheetApproval';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('timesheetApproval', 'Timesheet Approval', 'Timesheet Approval', 'Timesheet Approvals', 'TI', 'pm', false, false);