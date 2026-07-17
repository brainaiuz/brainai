
insert into "0".default_components(componentName, componentCode, modules) values
  ('My Files', 'HRMS_MY_FILES', '["HRMS"]');

insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,0, 2, 3, (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "0".default_components where componentCode = 'HRMS_MY_FILES' order by id limit 1));

insert into "anv".default_components(componentName, componentCode, modules) values
  ('My Files', 'HRMS_MY_FILES', '["HRMS"]');

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
 (6,4,0,0, 2, 3, (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "anv".default_components where componentCode = 'HRMS_MY_FILES' order by id limit 1));
