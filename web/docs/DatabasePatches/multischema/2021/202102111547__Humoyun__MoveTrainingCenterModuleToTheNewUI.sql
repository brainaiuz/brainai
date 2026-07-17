

delete from "anv".model where formid = 'SCHEDULED_COURSE_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'SCHEDULED_COURSE_FORM',  'Course Schedules Edit View Form' );

delete from "anv".customformsection where form_id = 'SCHEDULED_COURSE_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',   1),
('SCHEDULED_COURSE_FORM',	  'COURSE_REQUIREMENT_DETAILS', 2);

delete from "anv".modelfield where form_id = 'SCHEDULED_COURSE_FORM';
insert into "anv".modelfield
(form_id,                   fsection,                        section,                       nolabelfor,   fieldstyle,      columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,           forder,     field_id) values
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_1',	     '',               '',          false,        '',              'DataListBox',     1,	       'location'),
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_1',	     '',               '',          false,        '',              'DataListBox',     2,        'course'),
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_1',	     '',               '',          false,        '',              'DataListBox',     3,        'language'),
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_1',	     '',               '',          false,        '',              'KpiCheckBox',     4,        'overtime'),
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_2',	     '',               '',          false,        '',              'DataListBox',     5,        'instructor'),
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_2',	     '',               '',          false,        '',              'DataListBox',     6,        'assessor'),
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_2',	     '',               '',          false,        '',              'KpiDatePicker',   7,        'startDate'),
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_3',	     '',               '',          false,        '',              'TextArea2',       8,        'duration'),
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_3',	     '',               '',          false,        '',              'TextBox',         9,        'numberOfSeats'),
('SCHEDULED_COURSE_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',           'field',         'COL_3',	     '',               '',          false,        '',              'FlowPanel',       10,       'courserequirements');




delete from "anv".model where formid = 'SCHEDULED_COURSE_VIEW_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'SCHEDULED_COURSE_VIEW_FORM',  'Course Schedules View Form');

delete from "anv".customformsection where form_id = 'SCHEDULED_COURSE_VIEW_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',   1);

delete from "anv".modelfield where form_id = 'SCHEDULED_COURSE_VIEW_FORM' ;
insert into "anv".modelfield
(form_id,                         fsection,                        section,                       nolabelfor,      fieldstyle,      columntype,   fieldsetstyle, rowstyle,  mandatory,    sectionstyle,    widget,           forder,     field_id) values
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_1',	     '',           '',        false,        '',              'DataListBox',     1,	      'customer'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_1',	     '',           '',        false,        '',              'DataListBox',     2,        'quote'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_1',	     '',           '',        false,        '',              'DataListBox',     3,        'course'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_1',	     '',           '',        false,        '',              'KpiCheckBox',     4,        'number'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_1',	     '',           '',        false,        '',              'DataListBox',     5,        'startDate'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_1',	     '',           '',        false,        '',              'DataListBox',     6,        'endDate'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_2',	     '',           '',        false,        '',              'KpiDatePicker',   7,        'duration'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_2',	     '',           '',        false,        '',              'TextArea2',       8,        'location'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_2',	     '',           '',        false,        '',              'TextBox',         9,        'session'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_2',	     '',           '',        false,        '',              'FlowPanel',       10,       'instructor'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_2',	     '',           '',        false,        '',              'FlowPanel',       11,       'numberOfSeats'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_3',	     '',           '',        false,        '',              'FlowPanel',       12,       'venue'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_3',	     '',           '',        false,        '',              'FlowPanel',       13,       'visibility'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_3',	     '',           '',        false,        '',              'FlowPanel',       14,       'language'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_3',	     '',           '',        false,        '',              'FlowPanel',       15,       'assessor'),
('SCHEDULED_COURSE_VIEW_FORM',	  'SCHEDULED_COURSE_DETAILS',      'SCHEDULED_COURSE_DETAILS',    '',              'field',         'COL_3',	     '',           '',        false,        '',              'FlowPanel',       16,       'invoices');




delete from "anv".model where formid = 'CLONE_SCHEDULED_COURSE_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'CLONE_SCHEDULED_COURSE_FORM',  'Course Schedules Clone View');

delete from "anv".customformsection where form_id = 'CLONE_SCHEDULED_COURSE_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('CLONE_SCHEDULED_COURSE_FORM',	  'COURSE_SCHEDULE_REQUIRING_DATAILS',   1);

delete from "anv".modelfield where form_id = 'CLONE_SCHEDULED_COURSE_FORM' ;
insert into "anv".modelfield
(form_id,                          fsection,                                section,                                nolabelfor,             fieldstyle,      columntype,   fieldsetstyle,  rowstyle,   mandatory,    sectionstyle,  widget,           forder,    field_id) values
('CLONE_SCHEDULED_COURSE_FORM',	  'COURSE_SCHEDULE_REQUIRING_DATAILS',      'COURSE_SCHEDULE_REQUIRING_DATAILS',    '',                     'field',         'COL_1',	     '',             '',         false,        '',            '',               1,	       'courseschedulerequiring');




delete from "anv".model where formid = 'COURSE_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'COURSE_FORM',  'Course View');

delete from "anv".customformsection where form_id = 'COURSE_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('COURSE_FORM',	  'GENERAL_DETAILS',   1),
('COURSE_FORM',	  'INSTRUCTOR_DETAILS',   2);

delete from "anv".modelfield where form_id = 'COURSE_FORM' ;
insert into "anv".modelfield
(form_id,          fsection,              section,             nolabelfor,             fieldstyle,      columntype,   fieldsetstyle,          rowstyle,    mandatory,    sectionstyle,  widget,         forder,     field_id) values
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_1',	     '',                    '',          false,        '',            '',              1,	       'SUBJECT'),
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_1',	     '',                    '',          false,        '',            '',              2,   	   'CODE'),
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_1',	     '',                    '',          false,        '',            'TextBox',       3,	       'NAME'),
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_3',	     '',                    '',          false,        '',            'TextArea2',     4,	       'DESCRIPTION'),
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_2',	     '',                    '',          false,        '',            '',              5,	       'DURATION'),
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_2',	     '',                    '',          false,        '',            '',              6,	       'VALIDITY'),
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_2',	     '',                    '',          false,        '',            '',              7,	       'PRICEPERSTUDENT'),
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_1',	     '',                    '',          false,        '',            '',              8,	       'PREREQUISITE'),
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_3',	     '',                    '',          false,        '',            'TextArea2',     9,	       'OTHER_PREREQUISITE'),
('COURSE_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',                  'field',         'COL_2',	     '',                    '',          false,        '',            'CustomList',    10,	     'COURSEREQUIREMENTS'),
('COURSE_FORM',	  'INSTRUCTOR_DETAILS',   'INSTRUCTOR_DETAILS',    '',                  'field',         'COL_1',	     '',                    '',          false,        '',            '',              11,	     'INSTRUCTORS');




delete from "anv".model where formid = 'ADD_COURSE_SUBJECT_VIEW';
insert into "anv".model (active, formid, title)  values
(true,  'ADD_COURSE_SUBJECT_VIEW',  'Course Subject View');

delete from "anv".customformsection where form_id = 'ADD_COURSE_SUBJECT_VIEW';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('ADD_COURSE_SUBJECT_VIEW',	  'INFORMATION',   1);

delete from "anv".modelfield where form_id = 'ADD_COURSE_SUBJECT_VIEW' ;
insert into "anv".modelfield
(form_id,                     fsection,           section,             nolabelfor,          fieldstyle,      columntype,   fieldsetstyle,        rowstyle,    mandatory,    sectionstyle,  widget,         forder,     field_id) values
('ADD_COURSE_SUBJECT_VIEW',	  'INFORMATION',      'INFORMATION',       '',                  'field',         'COL_1',	     '',                    '',          true,        '',            'TextBox',     1,	       'NAME'),
('ADD_COURSE_SUBJECT_VIEW',	  'INFORMATION',      'INFORMATION',       '',                  'field',         'COL_1',	     '',                    '',          false,        '',            'DataListBox', 2,   	   'SUBJECT_PARENT'),
('ADD_COURSE_SUBJECT_VIEW',	  'INFORMATION',      'INFORMATION',       '',                  'field',         'COL_1',	     '',                    '',          false,        '',            'TextArea',    3,	       'DESCRIPTION');




delete from "anv".model where formid = 'BOOKING_ITEMS_ADD_VIEW';
insert into "anv".model (active, formid, title)  values
(true,  'BOOKING_ITEMS_ADD_VIEW',  'Booking Item View');

delete from "anv".customformsection where form_id = 'BOOKING_ITEMS_ADD_VIEW';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('BOOKING_ITEMS_ADD_VIEW',	  'INFORMATION',   1),
('BOOKING_ITEMS_ADD_VIEW',	  'RESERVATION_HISTORY',   2);

delete from "anv".modelfield where form_id = 'BOOKING_ITEMS_ADD_VIEW' ;
insert into "anv".modelfield
(form_id,                     fsection,               section,               nolabelfor,                  fieldstyle,      columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,  widget,         forder,     field_id) values
('BOOKING_ITEMS_ADD_VIEW',	  'INFORMATION',          'INFORMATION',         '',                          'field',         'COL_1',	     '',               '',          true,         '',            'DataListBox',     1,	       'LOCATION'),
('BOOKING_ITEMS_ADD_VIEW',	  'INFORMATION',          'INFORMATION',         '',                          'field',         'COL_1',	     '',               '',          true,         '',            'TextBox',         2,	       'NAME'),
('BOOKING_ITEMS_ADD_VIEW',	  'INFORMATION',          'INFORMATION',         '',                          'field',         'COL_1',	     '',               '',          false,        '',            'TextArea',        3,	       'DESCRIPTION'),
('BOOKING_ITEMS_ADD_VIEW',	  'INFORMATION',          'INFORMATION',         '',                          'field',         'COL_2',	     '',               '',          false,        '',            'Numbering',       4,	       'NUMBER'),
('BOOKING_ITEMS_ADD_VIEW',	  'INFORMATION',          'INFORMATION',         '',                          'field',         'COL_2',	     '',               '',          true,         '',            'DataListBox',     5,	       'CATEGORY'),
('BOOKING_ITEMS_ADD_VIEW',	  'INFORMATION',          'INFORMATION',         '',                          'field',         'COL_2',	     '',               '',          false,        '',            'HTML',            6,	       'STATUS'),
('BOOKING_ITEMS_ADD_VIEW',	  'RESERVATION_HISTORY',  'RESERVATION_HISTORY', 'viewForm,editForm,addForm', 'field',         'COL_1',	     '',               '',          false,        '',            'TextBox',         1,	       'RESERVATION_HISTORY');




delete from "anv".model where formid = 'BOOKING_ITEM_RESERVATION_VIEW';
insert into "anv".model (active, formid, title)  values
(true,  'BOOKING_ITEM_RESERVATION_VIEW',  'Booking Item Reservation View');

delete from "anv".customformsection where form_id = 'BOOKING_ITEM_RESERVATION_VIEW';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('BOOKING_ITEM_RESERVATION_VIEW',	  'INFORMATION',   1),
('BOOKING_ITEM_RESERVATION_VIEW',	  'RESERVATION_HISTORY',   2);

delete from "anv".modelfield where form_id = 'BOOKING_ITEM_RESERVATION_VIEW' ;
insert into "anv".modelfield
(form_id,                           fsection,               section,               nolabelfor,                      fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,         forder,     field_id) values
('BOOKING_ITEM_RESERVATION_VIEW',	  'INFORMATION',          'INFORMATION',         '',                              'field',         'COL_1',	     '',               '',          true,         '',            'DataListBox',     1,	       'RESERVED_BY'),
('BOOKING_ITEM_RESERVATION_VIEW',	  'INFORMATION',          'INFORMATION',         '',                              'field',         'COL_1',	     '',               '',          true,         '',            'TextBox',         2,	       'CATEGORY'),
('BOOKING_ITEM_RESERVATION_VIEW',	  'INFORMATION',          'INFORMATION',         '',                              'field',         'COL_1',	     '',               '',          false,        '',            'TextArea',        3,	       'ITEMS'),
('BOOKING_ITEM_RESERVATION_VIEW',	  'INFORMATION',          'INFORMATION',         '',                              'field',         'COL_2',	     '',               '',          false,        '',            'Numbering',       4,	       'START_DATE'),
('BOOKING_ITEM_RESERVATION_VIEW',	  'INFORMATION',          'INFORMATION',         '',                              'field',         'COL_2',	     '',               '',          true,         '',            'DataListBox',     5,	       'END_DATE'),
('BOOKING_ITEM_RESERVATION_VIEW',	  'INFORMATION',          'INFORMATION',         '',                              'field',         'COL_2',	     '',               '',          true,         '',            'CheckBox',        6,	       'ALL_DATE'),
('BOOKING_ITEM_RESERVATION_VIEW',	  'RESERVATION_HISTORY',  'RESERVATION_HISTORY', 'viewForm,editForm,addForm',     'field',         'COL_1',	     '',               '',          false,        '',            'HTML',            7,	       'RESERVATION_HISTORY');




delete from "anv".model where formid = 'STUDENT_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'STUDENT_FORM',  'Student View');

delete from "anv".customformsection where form_id = 'STUDENT_FORM';
insert into "anv".customformsection
(form_id,            section,             sorder) values
('STUDENT_FORM',	  'STUDENT_DETAILS',       1),
('STUDENT_FORM',	  'ADDRESS_INFORMATION',   2);

delete from "anv".modelfield where form_id = 'STUDENT_FORM' ;
insert into "anv".modelfield
(form_id,           fsection,                 section,                nolabelfor,  fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,         forder,     field_id) values
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_1',	     '',               '',          true,         '',            'DataListBox',     1,	       'studentCustomer'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_1',	     '',               '',          true,         '',            'TextBox',         2,	       'studentFirstName'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_1',	     '',               '',          false,        '',            'TextArea',        3,	       'studentLastName'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_1',	     '',               '',          false,        '',            'Numbering',       4,	       'studentEMail'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_1',	     '',               '',          true,         '',            'DataListBox',     5,	       'studentPhone'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_1',	     '',               '',          true,         '',            'CheckBox',        6,	       'studentCompanyEmployeeNumber'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_1',	     '',               '',          false,        '',            'HTML',            7,	       'studentDepartmentCode'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_2',	     '',               '',          true,         '',            'DataListBox',     8,	       'studentSafetyPP'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_2',	     '',               '',          true,         '',            'TextBox',         9,	       'refIndNumber'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_2',	     '',               '',          false,        '',            'TextArea',        10,	       'studentStatus'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_2',	     '',               '',          false,        '',            'Numbering',       11,	       'studentGender'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_2',	     '',               '',          true,         '',            'DataListBox',     12,	       'nationality'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_2',	     '',               '',          true,         '',            'CheckBox',        13,	       'studentDateOfBirth'),
('STUDENT_FORM',	  'STUDENT_DETAILS',        'STUDENT_DETAILS',      '',          'field',         'COL_2',	     '',               '',          false,        '',            'HTML',            14,	       'studentUploadImg'),
('STUDENT_FORM',	  'ADDRESS_INFORMATION',    'ADDRESS_INFORMATION',  '',          'field',         'COL_1',	     '',               '',          false,        '',            'HTML',            15,	       'studentAddress');




delete from "anv".model where formid = 'ATTENDENCE_SHEET_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'ATTENDENCE_SHEET_FORM',  'Attendance Sheet');

delete from "anv".customformsection where form_id = 'ATTENDENCE_SHEET_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('ATTENDENCE_SHEET_FORM',	  'INFORMATION',   1),
('ATTENDENCE_SHEET_FORM',	  'INSTRUCTOR_ATTENDENCE',   2),
('ATTENDENCE_SHEET_FORM',	  'STUDENTS_ATTENDENCE',   3);

delete from "anv".modelfield where form_id = 'ATTENDENCE_SHEET_FORM' ;
insert into "anv".modelfield
(form_id,                   fsection,                   section,                  nolabelfor,  fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,         forder,     field_id) values
('ATTENDENCE_SHEET_FORM',	  'INFORMATION',              'INFORMATION',            '',          'field',         'COL_1',	     '',               '',          true,         '',            'DataListBox',  1,	       'LOCATION'),
('ATTENDENCE_SHEET_FORM',	  'INFORMATION',              'INFORMATION',            '',          'field',         'COL_1',	     '',               '',          true,         '',            'DataListBox',  2,	       'DATE'),
('ATTENDENCE_SHEET_FORM',	  'INFORMATION',              'INFORMATION',            '',          'field',         'COL_1',	     '',               '',          false,        '',            'DataListBox',  3,	       'INSTRUCTOR'),
('ATTENDENCE_SHEET_FORM',	  'INFORMATION',              'INFORMATION',            '',          'field',         'COL_1',	     '',               '',          false,        '',            'DataListBox',  4,	       'COURSE'),
('ATTENDENCE_SHEET_FORM',	  'INSTRUCTOR_ATTENDENCE',    'INSTRUCTOR_ATTENDENCE',  'viewForm',  'field',         'COL_1',	     '',               '',          true,         '',            'KpiDataGrid',  5,	       'INSTRUCTOR_ATTENDENCE'),
('ATTENDENCE_SHEET_FORM',	  'STUDENTS_ATTENDENCE',      'STUDENTS_ATTENDENCE',    'viewForm',  'field',         'COL_1',	     '',               '',          true,         '',            'KpiDataGrid',  6,	       'STUDENTS_ATTENDENCE');




delete from "anv".model where formid = 'INSTRUCTOR_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'INSTRUCTOR_FORM',  'TC Instructor Form');

delete from "anv".customformsection where form_id = 'INSTRUCTOR_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('INSTRUCTOR_FORM',	  'EMPLOYEE_INFORMATION',   1),
('INSTRUCTOR_FORM',	  'CONTACT_INFORMATION',   2),
('INSTRUCTOR_FORM',	  'ADDRESS_INFORMATION',   3),
('INSTRUCTOR_FORM',	  'INSTRUCTOR_COURSES',   4),
('INSTRUCTOR_FORM',	  'ACCOUNT_INFORMATION',   5),
('INSTRUCTOR_FORM',	  'ATTACHMENTS',   6);

delete from "anv".modelfield where form_id = 'INSTRUCTOR_FORM' ;
insert into "anv".modelfield
(form_id,                   fsection,                   section,                  nolabelfor,    fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,         forder,    field_id) values
('INSTRUCTOR_FORM',	       'EMPLOYEE_INFORMATION',      'EMPLOYEE_INFORMATION',   '',            'field',         'COL_1',	     '',               '',          true,         '',             'UNKNOWN',       1,	     'FIRST_NAME'),
('INSTRUCTOR_FORM',	       'EMPLOYEE_INFORMATION',      'EMPLOYEE_INFORMATION',   '',            'field',         'COL_1',	     '',               '',          false,         '',            'TextBox',      2,	     'LAST_NAME'),
('INSTRUCTOR_FORM',	       'EMPLOYEE_INFORMATION',      'EMPLOYEE_INFORMATION',   '',            'field',         'COL_1',	     '',               '',          false,         '',            'TextBox',      3,	     'MIDDLE_NAME'),
('INSTRUCTOR_FORM',	       'EMPLOYEE_INFORMATION',      'EMPLOYEE_INFORMATION',   '',            'field',         'COL_1',	     '',               '',          false,         '',            'UNKNOWN',      4,	     'BIRTH_DAY'),
('INSTRUCTOR_FORM',	       'EMPLOYEE_INFORMATION',      'EMPLOYEE_INFORMATION',   '',            'field',         'COL_2',	     '',               '',          false,         '',            'UNKNOWN',      5,	     'GENDER'),
('INSTRUCTOR_FORM',	       'EMPLOYEE_INFORMATION',      'EMPLOYEE_INFORMATION',   '',            'field',         'COL_2',	     '',               '',          false,         '',            'DropDown',     6,	     'MARTIAL_STATUS'),
('INSTRUCTOR_FORM',	       'EMPLOYEE_INFORMATION',      'EMPLOYEE_INFORMATION',   '',            'field',         'COL_2',	     '',               '',          false,         '',            'UNKNOWN',      7,	     'LANGUAGE'),
('INSTRUCTOR_FORM',	       'EMPLOYEE_INFORMATION',      'EMPLOYEE_INFORMATION',   '',            'field',         'COL_2',	     '',               '',          false,         '',            'UNKNOWN',      8,	     'LOCATION'),
('INSTRUCTOR_FORM',	       'CONTACT_INFORMATION',       'CONTACT_INFORMATION',    '',            'field',         'COL_1',	     '',               '',          true,         '',             'MULTITABLE',    9,	     'EMAIL'),
('INSTRUCTOR_FORM',	       'CONTACT_INFORMATION',       'CONTACT_INFORMATION',    '',            'field',         'COL_1',	     '',               '',          false,         '',            'MULTITABLE',   10,	     'PHONE'),
('INSTRUCTOR_FORM',	       'CONTACT_INFORMATION',       'CONTACT_INFORMATION',    '',            'field',         'COL_2',	     '',               '',          false,         '',            'MULTITABLE',   11,	     'IM_ADDRESS'),
('INSTRUCTOR_FORM',	       'CONTACT_INFORMATION',       'CONTACT_INFORMATION',    '',            'field',         'COL_2',	     '',               '',          false,         '',            'MULTITABLE',   12,	     'WEB_ADDRESS'),
('INSTRUCTOR_FORM',	       'INSTRUCTOR_COURSES',        'INSTRUCTOR_COURSES',     '',            'field',         'COL_1',	     '',               '',          false,         '',            'UNKNOWN',      13,	     'COURSES'),
('INSTRUCTOR_FORM',	       'ADDRESS_INFORMATION',       'ADDRESS_INFORMATION',    '',            'field',         'COL_1',	     '',               '',          false,         '',            'UNKNOWN',      14,	     'ADDRESS'),
('INSTRUCTOR_FORM',	       'ACCOUNT_INFORMATION',       'ACCOUNT_INFORMATION',    '',            'field',         'COL_1',	     '',               '',          false,         '',            'DataListBox',  15,	     'ACCOUNT_STATUS'),
('INSTRUCTOR_FORM',	       'ATTACHMENTS',               'ATTACHMENTS',            '',            'field',         'COL_1',	     '',               '',          false,         '',            'UNKNOWN',      16,	     'ATTACHMENTS');




delete from "anv".model where formid = 'INSTRUCTOR_REASSIGN_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'INSTRUCTOR_REASSIGN_FORM',  'Instructor Reassign');

delete from "anv".customformsection where form_id = 'INSTRUCTOR_REASSIGN_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('INSTRUCTOR_REASSIGN_FORM',	  'DETAIL',   1);

delete from "anv".modelfield where form_id = 'INSTRUCTOR_REASSIGN_FORM' ;
insert into "anv".modelfield
(form_id,                        fsection,      section,    nolabelfor,    fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,         forder,    field_id) values
('INSTRUCTOR_REASSIGN_FORM',	   'DETAIL',      'DETAIL',   '',            'field',       'COL_1',	    '',               '',          true,         '',              'UNKNOWN',      1,	       'INSTRUCTOR_REASSIGN');




delete from "anv".model where formid = 'COURSE_BOOKING_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'COURSE_BOOKING_FORM',  'Course Booking');

delete from "anv".customformsection where form_id = 'COURSE_BOOKING_FORM';
insert into "anv".customformsection
(form_id,                 section,             sorder) values
('COURSE_BOOKING_FORM',	  'CUSTOMER_DETAILS',   1);

delete from "anv".modelfield where form_id = 'COURSE_BOOKING_FORM' ;
insert into "anv".modelfield
(form_id,                   fsection,              section,                  nolabelfor,  fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,         forder,   field_id) values
('COURSE_BOOKING_FORM',	  'CUSTOMER_DETAILS',      'CUSTOMER_DETAILS',       '',          'field',         'COL_1',	     '',               '',          true,         '',            'DataListBox',  1,	       'COMPANY_NAME'),
('COURSE_BOOKING_FORM',	  'CUSTOMER_DETAILS',      'CUSTOMER_DETAILS',       '',          'field',         'COL_1',	     '',               '',          true,         '',            'DataListBox',  2,	       'COMPANY_NUMBER'),
('COURSE_BOOKING_FORM',	  'CUSTOMER_DETAILS',      'CUSTOMER_DETAILS',       '',          'field',         'COL_1',	     '',               '',          false,        '',            'DataListBox',  3,	       'PHONE_NUMBER'),
('COURSE_BOOKING_FORM',	  'CUSTOMER_DETAILS',      'CUSTOMER_DETAILS',       '',          'field',         'COL_1',	     '',               '',          false,        '',            'DataListBox',  4,	       'FAX_NUMBER'),
('COURSE_BOOKING_FORM',	  'CUSTOMER_DETAILS',      'CUSTOMER_DETAILS',       '',          'field',         'COL_2',	     '',               '',          false,         '',           'KpiDataGrid',  5,	     'CUSTOMER_EMAIL'),
('COURSE_BOOKING_FORM',	  'CUSTOMER_DETAILS',      'CUSTOMER_DETAILS',       '',          'field',         'COL_2',	     '',               '',          true,         '',            'KpiDataGrid',  6,	     'TRAINING_VENUE'),
('COURSE_BOOKING_FORM',	  'CUSTOMER_DETAILS',      'CUSTOMER_DETAILS',       '',          'field',         'COL_2',	     '',               '',          false,         '',           'KpiDataGrid',  7,	     'TYPE');




delete from "anv".model where formid = 'STUDENT_ATTENDED_COURSE_BOOKING';
insert into "anv".model (active, formid, title)  values
(true,  'STUDENT_ATTENDED_COURSE_BOOKING',  'Student Course Booking');

delete from "anv".customformsection where form_id = 'STUDENT_ATTENDED_COURSE_BOOKING';
insert into "anv".customformsection
(form_id,                           expanded,    section,                           sorder) values
('STUDENT_ATTENDED_COURSE_BOOKING',	 true,       'CUSTOMER_DETAILS',                  1),
('STUDENT_ATTENDED_COURSE_BOOKING',	 true,       'CLIENT_AUTH',                       2),
('STUDENT_ATTENDED_COURSE_BOOKING',	 true,       'STUDENT_COURSE_SCHEDULE_DETAILS',  3);

delete from "anv".modelfield where form_id = 'STUDENT_ATTENDED_COURSE_BOOKING' ;
insert into "anv".modelfield
(form_id,                             fsection,                            section,                           nolabelfor,  fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,      forder,   field_id) values
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_1',	     '',               '',          true,         '',             '',          1,	    'COMPANY_NAME'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_1',	     '',               '',          true,         '',             '',          2,	    'COURSE_BOOKING_NUMBER'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_1',	     '',               '',          false,        '',             '',          3,	    'COURSE_BOOKING_INVOICE_NUMBER'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_1',	     '',               '',          false,        '',             '',          4,	    'COMPANY_NUMBER'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_1',	     '',               '',          false,         '',            '',          5,	    'PHONE_NUMBER'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_2',	     '',               '',          true,         '',             '',          6,	    'FAX_NUMBER'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_2',	     '',               '',          false,         '',            '',          7,	    'CUSTOMER_EMAIL'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_2',	     '',               '',          false,         '',            '',          8,	    'TRAINING_VENUE'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_2',	     '',               '',          false,         '',            '',          9,	    'STATUS'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CUSTOMER_DETAILS',                  'CUSTOMER_DETAILS',                '',          'field',         'COL_2',	     '',               '',          false,         '',            '',          10,	    'TYPE'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CLIENT_AUTH',                       'CLIENT_AUTH',                     '',          'field',         'COL_1',	     '',               '',          false,         '',            'LOOKUP',    11,	    'CONTACT_NAME'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CLIENT_AUTH',                       'CLIENT_AUTH',                     '',          'field',         'COL_1',	     '',               '',          false,         '',            'TextBox',   12,	    'POSITION'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CLIENT_AUTH',                       'CLIENT_AUTH',                     '',          'field',         'COL_1',	     '',               '',          false,         '',            'TextBox',   13,	    'REF_IND'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CLIENT_AUTH',                       'CLIENT_AUTH',                     '',          'field',         'COL_2',	     '',               '',          false,         '',            'UNKNOWN',   14,	    'CONTACT_PHONE'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'CLIENT_AUTH',                       'CLIENT_AUTH',                     '',          'field',         'COL_2',	     '',               '',          false,         '',            'TextBox',   15,	    'CONTACT_EMAIL'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'STUDENT_COURSE_SCHEDULE_DETAILS',   'STUDENT_COURSE_SCHEDULE_DETAILS', '',          'field',         'COL_1',	     '',               '',          false,         '',            'UNKNOWN',   16,	    'STUDENT_COURSE_BOOKING'),
('STUDENT_ATTENDED_COURSE_BOOKING',	  'STUDENT_COURSE_SCHEDULE_DETAILS',   'STUDENT_COURSE_SCHEDULE_DETAILS', '',          'field',         'COL_1',	     '',               '',          false,         '',            'UNKNOWN',   17,	    'CALCULATION_TABLE');




delete from "anv".model where formid = 'TRAINING_CONTACT_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'TRAINING_CONTACT_FORM',  'Training Contract');

delete from "anv".customformsection where form_id = 'TRAINING_CONTACT_FORM';
insert into "anv".customformsection
(form_id,                 section,             sorder) values
('TRAINING_CONTACT_FORM',	  'GENERAL_DETAILS',   1);

delete from "anv".modelfield where form_id = 'TRAINING_CONTACT_FORM' ;
insert into "anv".modelfield
(form_id,                    fsection,              section,               nolabelfor,  fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,       forder,   field_id) values
('TRAINING_CONTACT_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',          'field',         'COL_1',	     '',               '',          true,      '',           'LOOKUP',       1,	       'contractAccount'),
('TRAINING_CONTACT_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',          'field',         'COL_1',	     '',               '',          true,      '',           'TextBox',      2,	       'contractName'),
('TRAINING_CONTACT_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',          'field',         'COL_1',	     '',               '',          true,      '',           'TextArea',     3,	       'contractDescription'),
('TRAINING_CONTACT_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',          'field',         'COL_1',	     '',               '',          false,     '',           'CheckBox',     4,	       'prepaid'),
('TRAINING_CONTACT_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',          'field',         'COL_2',	     '',               '',          true,      '',           'DatePicker',   5,	       'contractStartDate'),
('TRAINING_CONTACT_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',          'field',         'COL_2',	     '',               '',          true,      '',           'DatePicker',   6,	       'contractEndDate'),
('TRAINING_CONTACT_FORM',	  'GENERAL_DETAILS',      'GENERAL_DETAILS',       '',          'field',         'COL_2',	     '',               '',          true,      '',           'UNKNOWN',      7,	       'contractCourse');




delete from "anv".model where formid = 'PRICE_CHANGE_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'PRICE_CHANGE_FORM',  'Change Prices');

delete from "anv".customformsection where form_id = 'PRICE_CHANGE_FORM';
insert into "anv".customformsection
(form_id,                 section,             sorder) values
('PRICE_CHANGE_FORM',	  'COURSE_PRICES',   1);

delete from "anv".modelfield where form_id = 'PRICE_CHANGE_FORM' ;
insert into "anv".modelfield
(form_id,               fsection,              section,               nolabelfor,  fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,   widget,       forder,   field_id) values
('PRICE_CHANGE_FORM',	  'COURSE_PRICES',      'COURSE_PRICES',       '',          'field',         'COL_1',	     '',               '',          true,         '',            'LOOKUP',       1,	     'PRICE_TABLE');




delete from "anv".model where formid = 'CERTIFICATE_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'CERTIFICATE_FORM',  'Certificates');

delete from "anv".customformsection where form_id = 'CERTIFICATE_FORM';
insert into "anv".customformsection
(form_id,             expanded,  section,      sorder) values
('CERTIFICATE_FORM',	true,    'TITLE',      1),
('CERTIFICATE_FORM',	true,    'CONTENT',    2);

delete from "anv".modelfield where form_id = 'CERTIFICATE_FORM' ;
insert into "anv".modelfield
(form_id,               fsection,     section,       nolabelfor,   fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,  widget,       forder,   field_id) values
('CERTIFICATE_FORM',	  'TITLE',      'TITLE',       '',           'field',         'COL_1',	     '',               '',          true,         '',          'TextBox',      1,	     'NUMBER'),
('CERTIFICATE_FORM',	  'TITLE',      'TITLE',       '',           'field',         'COL_1',	     '',               '',          true,         '',          'DataListBox',  2,	     'CERTIFICATE_TYPE'),
('CERTIFICATE_FORM',	  'TITLE',      'TITLE',       '',           'field',         'COL_2',	     '',               '',          true,         '',          'DataListBox',  3,	     'STUDENT'),
('CERTIFICATE_FORM',	  'CONTENT',    'CONTENT',     '',           'field',         'COL_1',	     '',               '',          true,         '',          'FlexTable',    1,	     'ITEMS_TABLE');




delete from "anv".model where formid = 'HSE_PASSPORT_FORM';
insert into "anv".model (active, formid, title)  values
(true,  'HSE_PASSPORT_FORM',  'Passport View');

delete from "anv".customformsection where form_id = 'HSE_PASSPORT_FORM';
insert into "anv".customformsection
(form_id,             section,      sorder) values
('HSE_PASSPORT_FORM',	'TITLE',      1);

delete from "anv".modelfield where form_id = 'HSE_PASSPORT_FORM' ;
insert into "anv".modelfield
(form_id,               fsection,     section,       nolabelfor,                   fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,  widget,       forder,   field_id) values
('HSE_PASSPORT_FORM',	  'TITLE',      'TITLE',       '',                           'field',         'COL_1',	     '',               '',          true,         '',          'DataListBox',  1,	     'TYPE'),
('HSE_PASSPORT_FORM',	  'TITLE',      'TITLE',       '',                           'field',         'COL_1',	     '',               '',          true,         '',          'TextBox',      2,	     'NUMBER'),
('HSE_PASSPORT_FORM',	  'TITLE',      'TITLE',       '',                           'field',         'COL_1',	     '',               '',          true,         '',          'DataListBox',  3,	     'LEVEL'),
('HSE_PASSPORT_FORM',	  'TITLE',      'TITLE',       '',                           'field',         'COL_2',	     '',               '',          true,         '',          'LOOKUP',       4,	     'STUDENT'),
('HSE_PASSPORT_FORM',	  'TITLE',      'TITLE',       '',                           'field',         'COL_2',	     '',               '',          true,         '',          'DataListBox',  5,	     'STATUS'),
('HSE_PASSPORT_FORM',	  'TITLE',      'TITLE',       '',                           'field',         'COL_1',	     '',               '',          true,         '',          'UNKNOWN',      6,	     'COURSES');

