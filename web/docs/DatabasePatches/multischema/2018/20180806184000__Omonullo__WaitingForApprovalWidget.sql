delete from "0".default_components where componentcode = 'WAITING_FOR_APPROVAL';

insert into "0".default_components(width, height, minHeight, minWidth, componentName, componentCode, modules, report_code) values
 (6, 4, 3, 2, 'Waiting for Approval', 'WAITING_FOR_APPROVAL', '["HRMS"]', null);

delete from "0".dashboard_components where dashboard_id = (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1)
and component_id = (select id from "0".default_components where componentCode = 'LEAVE_REASON_STATUS' order by id limit 1);

insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6, 4, 6, 4, 4, 4, (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "0".default_components where componentCode = 'LEAVE_REASON_STATUS' order by id limit 1));

delete from "anv".default_components where componentcode = 'WAITING_FOR_APPROVAL';

insert into "anv".default_components(width, height, minHeight, minWidth, componentName, componentCode, modules, report_code) values
 (6, 4, 3, 2, 'Waiting ForApproval', 'WAITING_FOR_APPROVAL', '["HRMS"]', null);

delete from "anv".dashboard_components where dashboard_id = (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1)
and component_id = (select id from "anv".default_components where componentCode = 'LEAVE_REASON_STATUS' order by id limit 1);

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6, 4, 6, 4, 4, 4, (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "anv".default_components where componentCode = 'WAITING_FOR_APPROVAL' order by id limit 1));