	   delete from "anv".container_item where propertyid = (select id from "anv".property where objectname = 'overtime' limit 1);
	   delete from "anv".property where objectname = 'overtime';

	   insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
       values ('overtime', 'Overtime', 'Overtime', 'Overtimes', 'OVT', 'payroll', false) on conflict do nothing;

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code = 'PAYROLL' limit 1),
       (select id from "anv".property where objectName='overtime' limit 1),
       (select id from "anv".container where code='payroll' limit 1), 12, 'payroll');