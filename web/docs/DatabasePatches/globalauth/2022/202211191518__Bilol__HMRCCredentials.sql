alter table hostbasedsetting add hmrcendpointdomain varchar(255) null;

update hostbasedsetting set hmrcendpointdomain = 'https://test-api.service.hmrc.gov.uk' where hostname in ('localhost:8080', 'dev.kpi.com');
