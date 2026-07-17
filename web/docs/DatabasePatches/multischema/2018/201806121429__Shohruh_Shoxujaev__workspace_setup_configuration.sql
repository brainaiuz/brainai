delete from "0".dashboard_setup_configuration;
delete from "anv".dashboard_setup_configuration;

insert into "0".dashboard_setup_configuration(title, description, state, type, dashboard_id) values
  ('Company Setup', 'The first step is to set up your company''s information, this is mostly used in printed business documents like orders, quotations and invoices.', 'ENABLED', 'COMPANY_SETUP', (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1)),
  ('Invite User', 'Invite user description', 'ENABLED', 'INVITE_USER', (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1)),
  ('Update Your Profile', 'Update Your Profile description', 'ENABLED', 'USER_PROFILE', (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1)),
  ('Data Migration', 'Data Migration description', 'ENABLED', 'DATA_MIGRATION', (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1)),
  ('Configure E-mail', 'Configure E-mail description', 'ENABLED', 'CONFIGURE_EMAIL', (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1));

insert into "anv".dashboard_setup_configuration(title, description, state, type, dashboard_id) values
  ('Company Setup', 'The first step is to set up your company''s information, this is mostly used in printed business documents like orders, quotations and invoices.', 'ENABLED', 'COMPANY_SETUP', (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1)),
  ('Invite User', 'Invite user description', 'ENABLED', 'INVITE_USER', (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1)),
  ('Update Your Profile', 'Update Your Profile description', 'ENABLED', 'USER_PROFILE', (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1)),
  ('Data Migration', 'Data Migration description', 'ENABLED', 'DATA_MIGRATION', (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1)),
  ('Configure E-mail', 'Configure E-mail description', 'ENABLED', 'CONFIGURE_EMAIL', (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1));

update "0".dashboard_setup_configuration set description = 'The first step is to set up your company''s information, this is mostly used in printed business documents like orders, quotations and invoices.' where type = 'COMPANY_SETUP';
update "0".dashboard_setup_configuration set description = 'When inviting users, you will need to define which access rights they are allowed to have. This is done by assigning a role to each user.' where type = 'INVITE_USER';
update "0".dashboard_setup_configuration set description = 'Changing your profile informatioon lets you control how others see you and your profile. These settings include things like your name, contact  and address details.' where type = 'USER_PROFILE';
update "0".dashboard_setup_configuration set description = 'Getting ready to import? Download our sample CSV files.' where type = 'DATA_MIGRATION';
update "0".dashboard_setup_configuration set description = 'Connect your email account to let kpi.com automatically organise all of your emails against the correct lead/contact/deals..etc' where type = 'CONFIGURE_EMAIL';

update "anv".dashboard_setup_configuration set description = 'The first step is to set up your company''s information, this is mostly used in printed business documents like orders, quotations and invoices.' where type = 'COMPANY_SETUP';
update "anv".dashboard_setup_configuration set description = 'When inviting users, you will need to define which access rights they are allowed to have. This is done by assigning a role to each user.' where type = 'INVITE_USER';
update "anv".dashboard_setup_configuration set description = 'Changing your profile informatioon lets you control how others see you and your profile. These settings include things like your name, contact  and address details.' where type = 'USER_PROFILE';
update "anv".dashboard_setup_configuration set description = 'Getting ready to import? Download our sample CSV files.' where type = 'DATA_MIGRATION';
update "anv".dashboard_setup_configuration set description = 'Connect your email account to let KPI.COM automatically organise all of your emails against the correct lead/contact/deals..etc' where type = 'CONFIGURE_EMAIL';