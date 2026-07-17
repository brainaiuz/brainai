alter table clustercompany add if not exists serviceid varchar(255);

update clustercompany set serviceid='kpi-dev-service';