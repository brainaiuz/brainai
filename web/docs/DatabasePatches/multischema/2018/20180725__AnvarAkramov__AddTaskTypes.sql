INSERT INTO "0".reference(
            code, description, name,  
            deleted, isremovable, issystemreference, shared, 
            iscustombutton, isactive, leavedays, 
            attendancelr, autoapprove, hasprorata)
    VALUES ('_TASK_TYPES', '', 'Task Types', false, false, true, false, 
            false, true, 0, false, false, false);


INSERT INTO "0".reference(
            code, description, name,  
            deleted, isremovable, issystemreference, shared, 
            iscustombutton, isactive, leavedays, 
            attendancelr, autoapprove, hasprorata, parentid)
    VALUES ('TASK_TYPE_GENERAL', '', 'General', false, false, true, false, 
            false, true, 0, false, false, false, (SELECT id FROM "0".reference WHERE code='_TASK_TYPES'));

   INSERT INTO "0".reference(
            code, description, name,  
            deleted, isremovable, issystemreference, shared, 
            iscustombutton, isactive, leavedays, 
            attendancelr, autoapprove, hasprorata, parentid)
    VALUES ('TASK_TYPE_FEATURE_REQUEST', '', 'Feature Request', false, false, true, false, 
            false, true, 0, false, false, false, (SELECT id FROM "0".reference WHERE code='_TASK_TYPES'));

   INSERT INTO "0".reference(
            code, description, name,  
            deleted, isremovable, issystemreference, shared, 
            iscustombutton, isactive, leavedays, 
            attendancelr, autoapprove, hasprorata, parentid)
    VALUES ('TASK_TYPE_PROBLEM', '', 'Problem', false, false, true, false, 
            false, true, 0, false, false, false, (SELECT id FROM "0".reference WHERE code='_TASK_TYPES'));


-- SELECT * FROM "0".reference WHERE code='_TASK_TYPES' OR parentid=(SELECT id FROM "0".reference WHERE code='_TASK_TYPES');

INSERT INTO "anv".reference(
            code, description, name,  
            deleted, isremovable, issystemreference, shared, 
            iscustombutton, isactive, leavedays, 
            attendancelr, autoapprove, hasprorata)
    VALUES ('_TASK_TYPES', '', 'Task Types', false, false, true, false, 
            false, true, 0, false, false, false);


INSERT INTO "anv".reference(
            code, description, name,  
            deleted, isremovable, issystemreference, shared, 
            iscustombutton, isactive, leavedays, 
            attendancelr, autoapprove, hasprorata, parentid)
    VALUES ('TASK_TYPE_GENERAL', '', 'General', false, false, true, false, 
            false, true, 0, false, false, false, (SELECT id FROM "anv".reference WHERE code='_TASK_TYPES'));

   INSERT INTO "anv".reference(
            code, description, name,  
            deleted, isremovable, issystemreference, shared, 
            iscustombutton, isactive, leavedays, 
            attendancelr, autoapprove, hasprorata, parentid)
    VALUES ('TASK_TYPE_FEATURE_REQUEST', '', 'Feature Request', false, false, true, false, 
            false, true, 0, false, false, false, (SELECT id FROM "anv".reference WHERE code='_TASK_TYPES'));

   INSERT INTO "anv".reference(
            code, description, name,  
            deleted, isremovable, issystemreference, shared, 
            iscustombutton, isactive, leavedays, 
            attendancelr, autoapprove, hasprorata, parentid)
    VALUES ('TASK_TYPE_PROBLEM', '', 'Problem', false, false, true, false, 
            false, true, 0, false, false, false, (SELECT id FROM "anv".reference WHERE code='_TASK_TYPES'));


-- SELECT * FROM "anv".reference WHERE code='_TASK_TYPES' OR parentid=(SELECT id FROM "anv".reference WHERE code='_TASK_TYPES');


INSERT INTO "0".modelfield(
            field_id, form_id,  
            hide, iscustomfield, mandatory, section, sorder,
            systemmandatory, widget, isentityfield, -- nolabelfor, nowrapperfor, 
            usablebyworkflow, type, fullwidth, place, split, fieldsetstyle, 
            fieldstyle, halfsetstyle, rowstyle, sectionstyle, disableupdate, 
            hideincustomizeform, systemdisable, isworkflowattribute,  
            forder)
    VALUES ( 'TYPE', 'TASK_SUMMARY_FORM', 
            false, false, false, 'TASK_DETAILS', 8,
            false, 'DropDown', false, false, 'text', false, 0, false, 
            'slideDown-content group labelLine', 'field', 'halfSet-1', 'row hideCustomField', 'slideDown-box  group expand hideCustomField', false, 
            false, false, false, 0);

  INSERT INTO "anv".modelfield(
            field_id, form_id,  
            hide, iscustomfield, mandatory, section, sorder,
            systemmandatory, widget, isentityfield, -- nolabelfor, nowrapperfor, 
            usablebyworkflow, type, fullwidth, place, split, fieldsetstyle, 
            fieldstyle, halfsetstyle, rowstyle, sectionstyle, disableupdate, 
            hideincustomizeform, systemdisable, isworkflowattribute,  
            forder)
    VALUES ( 'TYPE', 'TASK_MAX_FORM', 
            false, false, false, 'TASK_DETAILS', 8,
            false, 'DropDown', false, false, 'text', false, 0, false, 
            'slideDown-content group labelLine', 'field', 'halfSet-1', 'row hideCustomField', 'slideDown-box  group expand hideCustomField', false, 
            false, false, false, 0);
            