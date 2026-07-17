

update "0".modelfield set forder=forder*25 where form_id='ACTIVITY_FORM' and columntype='COL_2' and forder>=1;

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
select 'ACTIVITY_FORM', 'CUSTOM_HTML_TEMPLATE', false, 'COL_2', 'EVENT_INFORMATION', 2
WHERE NOT EXISTS (SELECT id FROM "0".modelfield  where form_id='ACTIVITY_FORM' and field_id='CUSTOM_HTML_TEMPLATE');


update "anv".modelfield set forder=forder*25 where form_id='ACTIVITY_FORM' and columntype='COL_2' and forder>=1;

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
select 'ACTIVITY_FORM', 'CUSTOM_HTML_TEMPLATE', false, 'COL_2', 'EVENT_INFORMATION', 2
WHERE NOT EXISTS (SELECT id FROM "anv".modelfield  where form_id='ACTIVITY_FORM' and field_id='CUSTOM_HTML_TEMPLATE');


