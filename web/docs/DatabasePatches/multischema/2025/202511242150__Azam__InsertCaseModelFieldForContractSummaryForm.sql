INSERT INTO "anv".modelfield (columntype, customlabel, defaultvalue, disableupdate, fieldsetstyle, fieldstyle, field_id,
                                forder, form_id, fsection, fullwidth, halfsetstyle, helpmessage, hide, hideincustomizeform,
                                iscustomfield, isentityfield, isworkflowattribute, label, mandatory, nolabelfor, nowrapperfor,
                                place, rowstyle, section, sectionstyle, sorder, source, split, systemdisable, systemmandatory,
                                type, usablebyworkflow, widget, gridheight, gridwidth, gridx, gridy, customizabletable,
                                customformlocalizationid, referenceid, deleted, widgetforbm)
VALUES ('COL_2', null, null, false, null, null, 'CASE_ID', 0, 'CONTRACT_FORM',
        'DETAILS', false, null, null, false, false, false, false, false,
        null, false, '', null, 0, null, null, null, 3, null, false,
        false, false, 'text', false,
        'TextBox', 1, 4, 0, 0, false, null, null, false, null);

INSERT INTO public.modelfield (customlabel, defaultvalue, field_id, form_id, helpmessage, hide, iscustomfield, label,
                               labelless, mandatory, section, sorder, systemmandatory, widget, source, isentityfield,
                               nolabelfor, nowrapperfor, usablebyworkflow, type, fullwidth, place, split, fieldsetstyle,
                               fieldstyle, halfsetstyle, rowstyle, sectionstyle, disableupdate, hideincustomizeform, systemdisable,
                               isworkflowattribute, columntype, forder, fsection, gridheight, gridwidth, gridx, gridy,
                               customizabletable, deleted, widgetforbm, customformlocalizationid, referenceid)
VALUES (null, '', 'CASE_ID', 'CONTRACT_FORM', null, false,
        false, null, false, false, 'DETAILS', 3, false,
        'TextBox', null, false, '', '', false, 'text',
        false, 0, false, 'slideDown-content group labelLine', 'field', 'halfSet-1 left',
        'row hideCustomField', 'slideDown-box  group expand hideCustomField', false,
        false, false, false, null, 0, null,
        1, 4, 0, 0, false, false, null,
        null, null);