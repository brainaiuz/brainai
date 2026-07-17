ALTER TABLE hostbasedsetting
    ADD COLUMN schedule_demo_url   varchar(300),
    ADD COLUMN phone_number        varchar(100),
    ADD COLUMN show_schedule_demo  boolean DEFAULT true,
    ADD COLUMN show_phone_number   boolean DEFAULT true;
