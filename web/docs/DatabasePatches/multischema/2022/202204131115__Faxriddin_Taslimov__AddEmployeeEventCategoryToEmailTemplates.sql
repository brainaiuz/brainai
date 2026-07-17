

INSERT INTO "0".reference (code, name,parentid,issystemreference,isremovable,isactive,shared,deleted)
    SELECT 'EMPLOYEE_EVENT_CATEGORY', 'Employee Event Reminder',(select id from "0".reference where code='ET_EVENT_MODULE'),true,false,true,true,false
WHERE NOT EXISTS (SELECT 1 FROM "0".reference where code='EMPLOYEE_EVENT_CATEGORY' and deleted is not true);


INSERT INTO "anv".reference (code, name,parentid,issystemreference,isremovable,isactive,shared,deleted)
    SELECT 'EMPLOYEE_EVENT_CATEGORY', 'Employee Event Reminder',(select id from "anv".reference where code='ET_EVENT_MODULE'),true,false,true,true,false
WHERE NOT EXISTS (SELECT 1 FROM "anv".reference where code='EMPLOYEE_EVENT_CATEGORY' and deleted is not true);
