update "anv".modelfield set columntype = 'COL_2' where field_id = 'TELEGRAM' and form_id = 'CANDIDATE_FORM';

delete from "anv".modelfield where field_id = 'MIDDLE_NAME' and form_id = 'CANDIDATE_FORM';

insert into "anv".modelfield(field_id, form_id, hide, iscustomfield, mandatory, section, sorder, widget, systemmandatory, isentityfield, usablebyworkflow, type, fullwidth, place, split, disableupdate, hideincustomizeform, systemdisable,
							   isworkflowattribute,fsection )
values('MIDDLE_NAME', 'CANDIDATE_FORM', true, false, false, 'CONTACT_INFORMATION', 25, 'TextBox', false, false, false, 'text', false, 0, false, false, false, false, false ,'CONTACT_INFORMATION');

