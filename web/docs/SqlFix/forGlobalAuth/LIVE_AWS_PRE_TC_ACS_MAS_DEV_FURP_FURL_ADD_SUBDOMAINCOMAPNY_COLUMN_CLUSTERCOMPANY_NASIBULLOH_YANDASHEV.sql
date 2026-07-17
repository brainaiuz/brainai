alter table clustercompany
add column "subdomaincompany" text;
create index sbdcindex on clustercompany (subdomaincompany);