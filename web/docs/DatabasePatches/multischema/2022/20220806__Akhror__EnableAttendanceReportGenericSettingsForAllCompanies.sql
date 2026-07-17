delete from "anv".genericsettings where key in ('FOR_CUSTOM_FINGER_PRINT', 'ATTENDANCE_LUNCH_AND_COFFEE_TIME');


insert into "anv".genericsettings(key,value ) values('ATTENDANCE_LUNCH_AND_COFFEE_TIME', 'YES'), ('FOR_CUSTOM_FINGER_PRINT', 'YES');