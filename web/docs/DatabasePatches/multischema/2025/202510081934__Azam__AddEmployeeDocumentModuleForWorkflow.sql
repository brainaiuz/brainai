delete from "anv".reference  where code = '_WORKFLOW_MODULE_EMPLOYEE_DOCUMENTS' and parentid = (select id from "anv".reference where code='_WORKFLOW_MODULE');

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_EMPLOYEE_DOCUMENTS', false, true, 'Employee Documents', true, 7, (select id from "anv".reference where code='_WORKFLOW_MODULE'), true);

insert into "0".model(active, formID, title, viewname) values(true, 'EMPLOYEE_DOCUMENTS_FORM', 'Employee Documents Form', 'EmployeeDocuments');
insert into "anv".model(active, formID, title, viewname) values(true, 'EMPLOYEE_DOCUMENTS_FORM', 'Employee Documents Form', 'EmployeeDocuments');

INSERT INTO "anv".modelfield (columntype, customlabel, defaultvalue, disableupdate, fieldsetstyle, fieldstyle,
                                field_id, forder, form_id, fsection, fullwidth, halfsetstyle, helpmessage, hide,
                                hideincustomizeform, iscustomfield, isentityfield, isworkflowattribute, label,
                                mandatory, nolabelfor, nowrapperfor, place, rowstyle, section, sectionstyle, sorder,
                                source, split, systemdisable, systemmandatory, type, usablebyworkflow, widget,
                                gridheight, gridwidth, gridx, gridy, customizabletable, customformlocalizationid,
                                referenceid, deleted, widgetforbm)
VALUES ( null, null, null, true, null, null, 'ISSUED_DATE', 0, 'EMPLOYEE_DOCUMENTS_FORM', null, false, null, null,
         false, false, false, false, false, null, false, null, null, 0, null, null, null, 1, null, false, false, false,
         'Date', true, 'DatePicker', 1, 4, 0, 0, false, null, null, false, 'DATE');

INSERT INTO "anv".modelfield (columntype, customlabel, defaultvalue, disableupdate, fieldsetstyle, fieldstyle,
                                field_id, forder, form_id, fsection, fullwidth, halfsetstyle, helpmessage, hide,
                                hideincustomizeform, iscustomfield, isentityfield, isworkflowattribute, label,
                                mandatory, nolabelfor, nowrapperfor, place, rowstyle, section, sectionstyle, sorder,
                                source, split, systemdisable, systemmandatory, type, usablebyworkflow, widget,
                                gridheight, gridwidth, gridx, gridy, customizabletable, customformlocalizationid,
                                referenceid, deleted, widgetforbm)
VALUES ( null, null, null, true, null, null, 'EXPIRY_DATE', 0, 'EMPLOYEE_DOCUMENTS_FORM', null, false, null, null,
         false, false, false, false, false, null, false, null, null, 0, null, null, null, 2, null, false, false, false,
         'Date', true, 'DatePicker', 1, 4, 0, 0, false, null, null, false, 'DATE');

