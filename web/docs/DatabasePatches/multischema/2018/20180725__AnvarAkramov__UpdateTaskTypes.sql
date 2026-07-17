UPDATE "0".reference SET shared=true where code='_TASK_TYPES' ;
UPDATE "anv".reference SET shared=true where code='_TASK_TYPES' ;

DELETE FROM "0".modelfield WHERE form_id='TASK_MAX_FORM' and field_id='TYPE';
INSERT INTO "0".modelfield(
            field_id, form_id,
            hide, iscustomfield, mandatory, section, fsection, sorder,
            systemmandatory, widget, isentityfield,
            usablebyworkflow, type, fullwidth, place, split, fieldsetstyle,
            fieldstyle, halfsetstyle, rowstyle, sectionstyle, disableupdate,
            hideincustomizeform, systemdisable, isworkflowattribute,
            forder)
    VALUES ( 'TYPE', 'TASK_MAX_FORM',
            true, false, false, 'TASK_DETAILS', 'TASK_DETAILS', 8,
            false, 'DropDown', false, false, 'text', false, 0, false,
            'slideDown-content group labelLine', 'field', 'halfSet-1', 'row hideCustomField', 'slideDown-box  group expand hideCustomField', false,
            false, false, false, 1010);


DELETE FROM "anv".modelfield WHERE form_id='TASK_MAX_FORM' and field_id='TYPE';
INSERT INTO "anv".modelfield(
            field_id, form_id,
            hide, iscustomfield, mandatory, section, fsection, sorder,
            systemmandatory, widget, isentityfield,
            usablebyworkflow, type, fullwidth, place, split, fieldsetstyle,
            fieldstyle, halfsetstyle, rowstyle, sectionstyle, disableupdate,
            hideincustomizeform, systemdisable, isworkflowattribute,
            forder)
    VALUES ( 'TYPE', 'TASK_MAX_FORM',
            true, false, false, 'TASK_DETAILS', 'TASK_DETAILS', 8,
            false, 'DropDown', false, false, 'text', false, 0, false,
            'slideDown-content group labelLine', 'field', 'halfSet-1', 'row hideCustomField', 'slideDown-box  group expand hideCustomField', false,
            false, false, false, 1010);
