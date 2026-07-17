alter table "anv".leave_reason
    rename saturday_is_working_day to exceptional_timeslot;

alter table "anv".timeslotitem
    add exStartTime INTEGER,
    add exEndTime INTEGER,
    add exLunchStart INTEGER,
    add exLunchEnd INTEGER,
    add exCoffeeStart INTEGER,
    add exCoffeeEnd INTEGER;