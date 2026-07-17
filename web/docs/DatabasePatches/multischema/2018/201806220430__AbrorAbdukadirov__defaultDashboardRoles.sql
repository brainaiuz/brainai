insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "0".role where code = 'ADMIN' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "0".role where code = 'ACCOUNTANT' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "0".role where code = 'DR' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "0".role where code = 'CREATOR' limit 1));

insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".role where code = 'ADMIN' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".role where code = 'HR' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".role where code = 'DR' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".role where code = 'CREATOR' limit 1));

insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".role where code = 'ADMIN' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".role where code = 'HR' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".role where code = 'DR' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".role where code = 'CREATOR' limit 1));

insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'CRM' order by id limit 1), (select id from "0".role where code = 'SALESMAN' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'CRM' order by id limit 1), (select id from "0".role where code = 'CREATOR' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'CRM' order by id limit 1), (select id from "0".role where code = 'DR' limit 1));
insert into "0".dashboard_accesses(dashboard_id, role_id) values ((select id from "0".module_dashboards where module = 'CRM' order by id limit 1), (select id from "0".role where code = 'ADMIN' limit 1));

do $$
    begin
        if not exists (select dashboard_id from "anv".dashboard_accesses where dashboard_id = (select id from "anv".module_dashboards where module = 'ACCOUNTING' order by id limit 1) limit 1)
        then
            insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "anv".role where code = 'ADMIN' limit 1));
            insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "anv".role where code = 'ACCOUNTANT' limit 1));
            insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "anv".role where code = 'DR' limit 1));
            insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "anv".role where code = 'CREATOR' limit 1));
        end if;
        if not exists (select dashboard_id from "anv".dashboard_accesses where dashboard_id = (select id from "anv".module_dashboards where module = 'PM' order by id limit 1) limit 1)
        then
	        insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".role where code = 'ADMIN' limit 1));
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".role where code = 'HR' limit 1));
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".role where code = 'DR' limit 1));
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".role where code = 'CREATOR' limit 1));	
        end if;
        if not exists (select dashboard_id from "anv".dashboard_accesses where dashboard_id = (select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1) limit 1)
        then
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".role where code = 'ADMIN' limit 1));
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".role where code = 'HR' limit 1));
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".role where code = 'DR' limit 1));
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".role where code = 'CREATOR' limit 1));
        end if;
        if not exists (select dashboard_id from "anv".dashboard_accesses where dashboard_id = (select id from "anv".module_dashboards where module = 'CRM' order by id limit 1) limit 1)
        then
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'CRM' order by id limit 1), (select id from "anv".role where code = 'SALESMAN' limit 1));
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'CRM' order by id limit 1), (select id from "anv".role where code = 'CREATOR' limit 1));
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'CRM' order by id limit 1), (select id from "anv".role where code = 'DR' limit 1));
          insert into "anv".dashboard_accesses(dashboard_id, role_id) values ((select id from "anv".module_dashboards where module = 'CRM' order by id limit 1), (select id from "anv".role where code = 'ADMIN' limit 1));
        end if;
end$$;