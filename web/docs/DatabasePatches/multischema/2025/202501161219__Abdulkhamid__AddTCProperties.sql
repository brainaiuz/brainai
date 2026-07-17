INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'scheduledcourses', 'Scheduled Courses', 'Scheduled Course', 'Scheduled Courses', 'CS', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'course', 'Courses', 'Course', 'Courses', 'C', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'coursesubject', 'Course Subjects', 'Course Subject', 'Course Subjects', 'CS', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'bookingItemsList', 'Booking Items', 'Booking Item', 'Booking Items', 'BI', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'tc_employee', 'Employee', 'Staff/Trainer', 'Staff/Trainers', 'ST', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'students', 'Students', 'Student', 'Students', 'STD', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'attendenceSheet', 'Attendance Sheet', 'Attendance Sheet', 'Attendance Sheets', 'ASH', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'instructorReassignList', 'Instructor Reassign', 'Instructor Reassign', 'Instructor Reassign', 'IR', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'courseBooking', 'Course Booking', 'Course Booking', 'Course Booking', 'CB', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'trainingContract', 'Training Contract', 'Training Contract', 'Training Contracts', 'TC', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'invoicegenerator', 'Invoice Generator', 'Invoice Generator', 'Invoice Generator', 'IG', 'trainingcenter', true
       );

INSERT INTO "anv".property (id, objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
VALUES (
           (SELECT COALESCE(MAX(id), 0) + 1 FROM "anv".property),
           'scheduleinvoice', 'Schedule invoice', 'Schedule invoice', 'Schedule invoice', 'SI', 'trainingcenter', true
       );
