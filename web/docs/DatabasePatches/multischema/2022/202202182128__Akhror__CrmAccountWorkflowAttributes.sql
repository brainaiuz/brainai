update "anv".modelfield
set usablebyworkflow = false
where form_id = 'ACCOUNT_FORM'
  and field_id in ('PAYMENT_METHOD', 'CRM_ACCOUNT_INDUSTRY');

INSERT INTO "anv".modelfield (customlabel, defaultvalue, disableupdate, fieldsetstyle, fieldstyle, field_id,
                                form_id, fullwidth, halfsetstyle, helpmessage, hide, hideincustomizeform, iscustomfield,
                                isentityfield, label, mandatory, nolabelfor, nowrapperfor, place, rowstyle, section,
                                sectionstyle, sorder, source, split, systemdisable, systemmandatory, type,
                                usablebyworkflow, widget, isworkflowattribute, columntype, forder, fsection, gridheight,
                                gridwidth, gridx, gridy, customizabletable, customformlocalizationid)
VALUES (null, '', false, 'slideDown-content group labelLine', 'field', 'CLIENT_CREDIT_LIMIT', 'ACCOUNT_FORM', false,
        'halfSet-1 left', '', true, false, false, false, null, false, '', '', 0, 'row hideCustomField',
        'CRM_ACCOUNT_FINANCIAL_INFORMATION', 'slideDown-box  group expand hideCustomField', 4, null, false, false,
        false, 'text', true, 'TextBox', false, 'COL_3', 1002, 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 1, 4, 0, 0, false,
        null);
INSERT INTO "anv".modelfield (customlabel, defaultvalue, disableupdate, fieldsetstyle, fieldstyle, field_id,
                                form_id, fullwidth, halfsetstyle, helpmessage, hide, hideincustomizeform, iscustomfield,
                                isentityfield, label, mandatory, nolabelfor, nowrapperfor, place, rowstyle, section,
                                sectionstyle, sorder, source, split, systemdisable, systemmandatory, type,
                                usablebyworkflow, widget, isworkflowattribute, columntype, forder, fsection, gridheight,
                                gridwidth, gridx, gridy, customizabletable, customformlocalizationid)
VALUES (null, 'MMM dd, yyyy', false, 'slideDown-content group labelLine', 'field', 'CLIENT_AS_OF_DATE',
        'ACCOUNT_FORM', false, 'halfSet-1 left', '', true, false, false, false, null, false, '', '', 0,
        'row hideCustomField', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 'slideDown-box  group expand hideCustomField', 3,
        null, false, false, false, 'text', true, 'DatePicker', false, 'COL_2', 1000,
        'CRM_ACCOUNT_FINANCIAL_INFORMATION', 1, 4, 0, 0, false, null);
INSERT INTO "anv".modelfield (customlabel, defaultvalue, disableupdate, fieldsetstyle, fieldstyle, field_id,
                                form_id, fullwidth, halfsetstyle, helpmessage, hide, hideincustomizeform, iscustomfield,
                                isentityfield, label, mandatory, nolabelfor, nowrapperfor, place, rowstyle, section,
                                sectionstyle, sorder, source, split, systemdisable, systemmandatory, type,
                                usablebyworkflow, widget, isworkflowattribute, columntype, forder, fsection, gridheight,
                                gridwidth, gridx, gridy, customizabletable, customformlocalizationid)
VALUES (null, '', false, 'slideDown-content group labelLine', 'field', 'CLIENT_AMOUNT', 'ACCOUNT_FORM', false,
        'halfSet-1 left', '', true, false, false, false, null, false, '', '', 0, 'row hideCustomField',
        'CRM_ACCOUNT_FINANCIAL_INFORMATION', 'slideDown-box  group expand hideCustomField', 3, null, false, false,
        false, 'text', true, 'TextBox', false, 'COL_2', 1001, 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 1, 4, 0, 0, false,
        null);
INSERT INTO "anv".modelfield (customlabel, defaultvalue, disableupdate, fieldsetstyle, fieldstyle, field_id,
                                form_id, fullwidth, halfsetstyle, helpmessage, hide, hideincustomizeform, iscustomfield,
                                isentityfield, label, mandatory, nolabelfor, nowrapperfor, place, rowstyle, section,
                                sectionstyle, sorder, source, split, systemdisable, systemmandatory, type,
                                usablebyworkflow, widget, isworkflowattribute, columntype, forder, fsection, gridheight,
                                gridwidth, gridx, gridy, customizabletable, customformlocalizationid)
VALUES (null, null, false, 'slideDown-content group labelLine', 'field', 'CLIENT_BANK_ACCOUNT', 'ACCOUNT_FORM',
        false, 'halfSet-1 left', '', true, false, false, false, null, false, '', '', 0, 'row hideCustomField',
        'CRM_ACCOUNT_FINANCIAL_INFORMATION', 'slideDown-box  group expand hideCustomField', 3, null, false, false,
        false, 'text', true, 'DropDown', false, 'COL_3', 1001, 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 1, 4, 0, 0, false,
        null);