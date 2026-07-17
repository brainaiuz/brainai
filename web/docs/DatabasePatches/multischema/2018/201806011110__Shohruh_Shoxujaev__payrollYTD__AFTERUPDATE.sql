insert into "0".default_components(componentName, componentCode, modules) values
  ('Payroll YTD', 'PAYROLL_YTD', '["HRMS"]');

insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,6,8, 2, 3, (select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".default_components where componentCode = 'PAYROLL_YTD' order by id limit 1));

insert into "anv".default_components(componentName, componentCode, modules) values
  ('Payroll YTD', 'PAYROLL_YTD', '["HRMS"]');

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,6,8, 2, 3, (select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".default_components where componentCode = 'PAYROLL_YTD' order by id limit 1));

