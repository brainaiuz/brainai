INSERT INTO "anv".customformsection (active, custom, form_id, section, sorder)
VALUES (false, false, 'SCHEDULED_COURSE_FORM', 'ADVANCED_OPTIONS', 3);


INSERT INTO "anv".modelfield ( columntype, customlabel, customizabletable, defaultvalue, deleted, disableupdate, fieldsetstyle, fieldstyle, field_id, forder, form_id, fsection, fullwidth, gridheight, gridwidth, gridx, gridy, halfsetstyle, helpmessage, hide, hideincustomizeform, iscustomfield, isentityfield, isworkflowattribute, label, mandatory, nolabelfor, nowrapperfor, place, rowstyle, section, sectionstyle, sorder, source, split, systemdisable, systemmandatory, type, usablebyworkflow, widget, widgetforbm, customformlocalizationid, referenceid)
VALUES ('COL_1', null, false, null, false, false, null, null, 'RECURRENING', 0, 'SCHEDULED_COURSE_FORM', 'ADVANCED_OPTIONS', false, 1, 4, 0, 0, null, null, false, false, false, false, false, null, false, '', null, 0, null, null, null, null, null, false, false, false, 'text', false, 'UNKNOWN', null, null, null);

insert into myupdatetype (code, description)
values ('COURSE_SCHEDULE', 'All course schedules related updates');
insert into myupdatetype (code, description, parentid)
values ('COURSE_SCHEDULE_ADD', 'Records when user has added course scheduled', (select id from myupdatetype where code = 'COURSE_SCHEDULE'));

INSERT INTO recurrencejob (id, name) VALUES (34, 'Recurring Course Schedule');
