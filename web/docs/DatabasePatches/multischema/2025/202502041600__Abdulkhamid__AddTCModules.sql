INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'certificate', 'Certificate', 'Certificate', 'Certificates', 'SI', 'trainingcenter', true
       );

insert into "anv".mymodule (code,name,section,active) values ('CERTIFICATES','Certificate','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('COURSE_BOOKING','Course Booking','operations',true);

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CERTIFICATES' limit 1), (select id from "anv".property where objectName='certificate' limit 1), (select id from "anv".container where code='operations' limit 1), 11, 'trainingcenter');




INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'passport', 'Passport', 'Passport', 'Passports', 'SI', 'trainingcenter', true
       );

insert into "anv".mymodule (code,name,section,active) values ('PASSPORT','Passport','operations',true);

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PASSPORT' limit 1), (select id from "anv".property where objectName='passport' limit 1), (select id from "anv".container where code='operations' limit 1), 12, 'trainingcenter');




insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('assessment', 'assessment', 'trainingcenter', 3, false, 'assessment');


INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'confirmedscheduledcourses', 'Confirmed Scheduled Courses', 'Confirmed Scheduled Course', 'Confirmed Scheduled Courses', 'CSC', 'trainingcenter', true
       );

insert into "anv".mymodule (code,name,section,active) values ('CONFIRMED_SCHEDULED_COURSES','Passport','assessment',true);

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CONFIRMED_SCHEDULED_COURSES' limit 1), (select id from "anv".property where objectName='confirmedscheduledcourses' limit 1), (select id from "anv".container where code='assessment' limit 1), 0, 'trainingcenter');
