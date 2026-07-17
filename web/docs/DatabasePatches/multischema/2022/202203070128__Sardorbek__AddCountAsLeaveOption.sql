alter table "anv".timeslotitem
        drop column exstarttime,
        drop column exendtime,
        drop column exlunchstart,
        drop column exlunchend,
        drop column excoffeestart,
        drop column excoffeeend;

alter table "anv".timeslotitem
        add additional_leave_days varchar;