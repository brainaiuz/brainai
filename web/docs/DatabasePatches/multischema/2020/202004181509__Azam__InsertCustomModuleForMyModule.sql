delete from "anv".mymodule where code = 'CUSTOM';
insert into "anv".mymodule (code,name,section,active) values ('CUSTOM','Custom','core',true) on conflict do nothing;

update reportTemplateCategory set modulecode='CUSTOM' where code='CUSTOM';