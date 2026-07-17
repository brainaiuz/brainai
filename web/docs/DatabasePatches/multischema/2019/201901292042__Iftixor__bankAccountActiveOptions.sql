delete from "0".modelfield where form_id = 'BANK_ACCOUNT_FORM' and  section = 'ACCOUNT_INFORMATION' and field_id = 'ACTIVE';
INSERT INTO "0".modelfield (customlabel, defaultvalue, field_id, form_id, helpmessage, hide, iscustomfield,
                                label,  mandatory, section, sorder, widget, systemmandatory, source,
                                nolabelfor, nowrapperfor, isentityfield, usablebyworkflow, type, fullwidth, place,
                                split, fieldsetstyle, fieldstyle, halfsetstyle, rowstyle, sectionstyle, disableupdate,
                                hideincustomizeform, systemdisable, isworkflowattribute, columntype, forder, fsection)
VALUES (null, null, 'ACTIVE', 'BANK_ACCOUNT_FORM', null, false, false, null, false, 'ACCOUNT_INFORMATION',
        null, null, false, null, null, null, false, false, 'text', false, 0, false, null, null, null, null,
        null, false, false, false, false, 'COL_2', 2, 'ACCOUNT_INFORMATION');


delete from "anv".modelfield where form_id = 'BANK_ACCOUNT_FORM' and  section = 'ACCOUNT_INFORMATION' and field_id = 'ACTIVE';
INSERT INTO "anv".modelfield (customlabel, defaultvalue, field_id, form_id, helpmessage, hide, iscustomfield,
                                label,  mandatory, section, sorder, widget, systemmandatory, source,
                                nolabelfor, nowrapperfor, isentityfield, usablebyworkflow, type, fullwidth, place,
                                split, fieldsetstyle, fieldstyle, halfsetstyle, rowstyle, sectionstyle, disableupdate,
                                hideincustomizeform, systemdisable, isworkflowattribute, columntype, forder, fsection)
VALUES (null, null, 'ACTIVE', 'BANK_ACCOUNT_FORM', null, false, false, null, false, 'ACCOUNT_INFORMATION',
        null, null, false, null, null, null, false, false, 'text', false, 0, false, null, null, null, null,
        null, false, false, false, false, 'COL_2', 2, 'ACCOUNT_INFORMATION');