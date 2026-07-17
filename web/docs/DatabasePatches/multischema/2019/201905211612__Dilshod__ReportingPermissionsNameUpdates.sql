
update "0".reportingpermission set name = REPLACE(name, ' Report','');
update "0".reportingpermission set name = trim(name);


update "anv".reportingpermission set name = REPLACE(name, ' Report','');
update "anv".reportingpermission set name = trim(name);
