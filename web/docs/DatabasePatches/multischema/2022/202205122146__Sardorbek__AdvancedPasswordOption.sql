alter table companydomains
    add column if not exists enabled_advanced_password boolean default false;