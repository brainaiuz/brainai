alter table hostbasedsetting add hmrcUrl varchar(255) null;
alter table hostbasedsetting add hmrcClientId varchar(255) null;
alter table hostbasedsetting add hmrcClientSecret varchar(255) null;

update hostbasedsetting set hmrcUrl = 'https://test-www.tax.service.gov.uk' where hostname in ('localhost:8080', 'dev.kpi.com');
update hostbasedsetting set hmrcClientId = 'uldVyI0xoblPxDktcHrl8SaVA7Nn' where hostname in ('localhost:8080', 'dev.kpi.com');
update hostbasedsetting set hmrcClientSecret = 'a267e23d-382f-4e7a-b550-cf601090a8d8' where hostname in ('localhost:8080', 'dev.kpi.com');
