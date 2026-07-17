
---Delete all ESS Settings Role permissions from zero
delete from "0".rolepermission where id in (
                    select rp.id from permission p
                     join "0".permission_context pc on p.code=pc.permissioncode
                     join "0".rolepermission rp on p.code=rp.permissioncode
where p.context='SETTINGS' and rolecode='ESS_USER');

insert into "0".rolepermission(permissioncode,rolecode,access) values ('SETTINGS_MAIN_MENU','ESS_USER','ALLOW');
insert into "0".rolepermission(permissioncode,rolecode,access) values ('SETTINGS_PROFILE_SETTINGS','ESS_USER','ALLOW');
insert into "0".rolepermission(permissioncode,rolecode,access) values ('SETTINGS_USER_CREDENTIALS','ESS_USER','ALLOW');


---Delete all ESS Settings Role permissions from all schemas

delete from "anv".rolepermission where id in (
                    select rp.id from permission p
                     join "anv".permission_context pc on p.code=pc.permissioncode
                     join "anv".rolepermission rp on p.code=rp.permissioncode
where p.context='SETTINGS' and rolecode='ESS_USER');

insert into "anv".rolepermission(permissioncode,rolecode,access) values ('SETTINGS_MAIN_MENU','ESS_USER','ALLOW');
insert into "anv".rolepermission(permissioncode,rolecode,access) values ('SETTINGS_PROFILE_SETTINGS','ESS_USER','ALLOW');
insert into "anv".rolepermission(permissioncode,rolecode,access) values ('SETTINGS_USER_CREDENTIALS','ESS_USER','ALLOW');