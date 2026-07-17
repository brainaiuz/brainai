
DELETE from qrtz_triggers where trigger_name='userFingerTrigger';
DELETE from qrtz_cron_triggers where trigger_name='userFingerTrigger';
DELETE from qrtz_fired_triggers where trigger_name='userFingerTrigger';
DELETE from qrtz_simple_triggers where trigger_name='userFingerTrigger';
DELETE from qrtz_simprop_triggers where trigger_name='userFingerTrigger';
DELETE from qrtz_job_details where job_class_name like ('%UserFingerRecurrenceJob%');