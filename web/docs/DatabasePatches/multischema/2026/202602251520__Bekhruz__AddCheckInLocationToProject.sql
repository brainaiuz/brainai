INSERT INTO "anv".customformsection (active, custom, form_id, section, sorder) VALUES
    (true, false, 'PROJECT_FORM', 'CHECK_IN_LOCATIONS', 11);

insert into "anv".modelfield (form_id, fsection, field_id, forder, columntype, hide, hideincustomizeform)
values('PROJECT_FORM', 'CHECK_IN_LOCATIONS', 'CHECK_IN_LOCATION', 0, 'COL_1', true, false);