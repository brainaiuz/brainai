update "0".default_components set modules = '["HRMS","MYWORKSPACE]' where componentcode = 'UNAVAILABLE_EMPLOYEES_SUPERVISION';

insert into "0".default_components (width, height, minHeight, minWidth, componentName, componentCode, modules, report_code)
values
  (4, 4, 2, 2, 'Unavailable Employees', 'UNAVAILABLE_EMPLOYEES_SUPERVISION', '["HRMS","MYWORKSPACE]', null);

delete from "0".dashboard_components
where component_id = (select id from "0".default_components where componentCode = 'UNAVAILABLE_EMPLOYEES_SUPERVISION' order by id limit 1)
and dashboard_id = (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1);


insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,8, 4, 6, (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "0".default_components where componentCode = 'UNAVAILABLE_EMPLOYEES_SUPERVISION' order by id limit 1));


update "anv".default_components set modules = '["HRMS","MYWORKSPACE]' where componentcode = 'UNAVAILABLE_EMPLOYEES_SUPERVISION';

insert into "anv".default_components (width, height, minHeight, minWidth, componentName, componentCode, modules, report_code)
values
  (4, 4, 2, 2, 'Unavailable Employees', 'UNAVAILABLE_EMPLOYEES_SUPERVISION', '["HRMS","MYWORKSPACE]', null);

delete from "anv".dashboard_components

where component_id = (select id from "anv".default_components where componentCode = 'UNAVAILABLE_EMPLOYEES_SUPERVISION' order by id limit 1)
      and dashboard_id = (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1);

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,8, 4, 6, (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "anv".default_components where componentCode = 'UNAVAILABLE_EMPLOYEES_SUPERVISION' order by id limit 1));