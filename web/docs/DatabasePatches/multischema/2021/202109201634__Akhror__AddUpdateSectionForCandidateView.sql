insert into "anv".customformsection(form_id, section, active, sorder, expanded)
values ('CANDIDATE_FORM', 'UPDATES', true, 11, true);

delete from "anv".modelfield where form_id = 'CANDIDATE_FORM' and field_id = 'CREATED_DATE';
insert into "anv".modelfield(field_id, form_id, sorder, type, columntype, forder, fsection)
values ('CREATED_BY', 'CANDIDATE_FORM', 12, 'text', 'COL_1', 0, 'UPDATES'),
       ('CREATED_DATE', 'CANDIDATE_FORM', 12, 'text', 'COL_1', 1, 'UPDATES'),
       ('UPDATED_BY', 'CANDIDATE_FORM', 13, 'text', 'COL_2', 0, 'UPDATES'),
       ('UPDATED_DATE', 'CANDIDATE_FORM', 13, 'text', 'COL_2', 2, 'UPDATES');