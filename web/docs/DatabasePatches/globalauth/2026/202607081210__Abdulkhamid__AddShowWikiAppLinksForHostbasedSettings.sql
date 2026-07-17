ALTER TABLE hostbasedsetting
    ADD COLUMN IF NOT EXISTS schedule_demo_url  varchar(300),
    ADD COLUMN IF NOT EXISTS phone_number       varchar(100),
    ADD COLUMN IF NOT EXISTS show_schedule_demo boolean DEFAULT true,
    ADD COLUMN IF NOT EXISTS show_phone_number  boolean DEFAULT true,
    ADD COLUMN IF NOT EXISTS show_wiki          boolean DEFAULT true,
    ADD COLUMN IF NOT EXISTS show_app_links     boolean DEFAULT true;