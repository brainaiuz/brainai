delete
from "90826".genericsettings
where key ='ENABLE_PROJECT_MANAGEMENT_TASKS_FOR_HRMS';

insert into "90826".genericsettings (key, value)
values ('ENABLE_PROJECT_MANAGEMENT_TASKS_FOR_HRMS', 'YES');