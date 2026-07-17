delete
from "anv".customformsection
where form_id = 'CANDIDATE_FORM' and section = 'CANDIDATE_STATUS_HISTORY';
insert into "anv".customformsection(form_id, section, active, sorder)
values ('CANDIDATE_FORM', 'CANDIDATE_STATUS_HISTORY', false,
        (select sorder + 1 from "anv".customformsection where form_id = 'CANDIDATE_FORM' and section = 'UPDATES') );

delete
from "anv".modelfield
where field_id = 'CANDIDATE_STATUS_HISTORY'
  and form_id = 'CANDIDATE_FORM';
insert into "anv".modelfield(field_id, form_id, section, sorder, widget, nolabelfor, type, fullwidth, fieldsetstyle,
                             fieldstyle, rowstyle, sectionstyle, columntype, fsection)
values ('CANDIDATE_STATUS_HISTORY', 'CANDIDATE_FORM', 'CANDIDATE_STATUS_HISTORY',
        (select max(sorder) + 1 from "anv".modelfield where form_id = 'CANDIDATE_FORM'), 'UNKNOWN', '', 'text', true,
        'slideDown-content group nobrd', 'field', 'row hideCustomField', 'slideDown-box  group expand hideCustomField',
        'COL_1', 'CANDIDATE_STATUS_HISTORY');
