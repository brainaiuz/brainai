delete
from "anv".genericsettings
where key = 'FINGERPRINT_DEVICE_ENABLED';


insert into "anv".genericsettings(key, value)
values ('FINGERPRINT_DEVICE_ENABLED', 'YES');