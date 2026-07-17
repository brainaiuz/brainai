delete
from "anv".genericsettings
where key = 'ENABLE_REFERENCE_IN_SUM';

insert into "anv".genericsettings (key, value)
values ('ENABLE_REFERENCE_IN_SUM', 'NO');