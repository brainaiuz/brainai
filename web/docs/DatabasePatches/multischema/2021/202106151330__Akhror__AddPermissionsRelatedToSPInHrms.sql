delete
from permission
where code = 'HRMS_SINGLE_PAYRUN_LIST';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_SINGLE_PAYRUN_LIST', 'HRMS', false, 'Single Payruns List', 50,
        (select id from permission where code = 'HRMS_SECTION_TAB'), true, 'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_SINGLE_PAYRUN_LIST';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_SINGLE_PAYRUN_LIST', 'HRMS');


delete from "anv".rolepermission where permissioncode = 'HRMS_SINGLE_PAYRUN_LIST';
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_SINGLE_PAYRUN_LIST', 'ALLOW', 'HR');

-- delete from "anv".property where objectname = 'singlepayrun' and modulecode = 'hrms';
-- insert into "anv".property(defaultname, modulecode, objectname, plural, shortcut, singular) values ('Single Payruns', 'hrms', 'singlepayrun', 'Single Payruns', 'SP', 'Single Payrun');
update "anv".property set modulecode = 'hrms,payroll' where objectName = 'singlePayrunList';

delete from "anv".container_item where containerid = (select id from "anv".container where code='hrmsMain' limit 1) and propertyid = (select id from "anv".property where objectname = 'singlePayrunList' limit 1);
insert into "anv".container_item(moduleid, containerid, propertyid, sorder, modulecode) values ((select id from "anv".mymodule where code = 'HRMS_MODULE' limit 1), (select id from "anv".container where code = 'hrmsMain' limit 1), (select id from "anv".property where objectname = 'singlePayrunList' limit 1), 2, 'hrms');


update "anv".container_item set sorder = 1 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='employee'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 3 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='employeeDocuemnts'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 4 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='companyDocuemnts'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 5 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='benefit_requests'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 6 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='meeting'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 7 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='certificateslist'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 8 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='incidentList'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 9 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='organizationChart'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 10 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='departmentOrgChartView'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 11 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='notifications'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);
update "anv".container_item set sorder = 12 where moduleid = (select id from "anv".mymodule where code='HRMS_MODULE' limit 1) and propertyid = (select id from "anv".property where objectName='news_list'and modulecode = 'hrms' limit 1) and containerid = (select id from "anv".container where code='hrmsMain' limit 1);

