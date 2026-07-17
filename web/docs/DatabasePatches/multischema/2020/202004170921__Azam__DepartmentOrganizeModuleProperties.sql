delete from "0".property where objectName = 'departmentList';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('departmentList', 'Departments', 'Department', 'Departments', 'D', 'hrms', false);

delete from "0_template".property where objectName = 'departmentList';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('departmentList', 'Departments', 'Department', 'Departments', 'D', 'hrms', false);

delete from "anv".property where objectName = 'departmentList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('departmentList', 'Departments', 'Department', 'Departments', 'D', 'hrms', false);