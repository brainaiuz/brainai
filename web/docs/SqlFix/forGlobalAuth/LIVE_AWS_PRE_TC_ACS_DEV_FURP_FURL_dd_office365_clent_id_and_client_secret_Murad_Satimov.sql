alter table hostbasedsetting add office365clientid varchar(255) null;
alter table hostbasedsetting add office365clientsecret varchar(255) null;

---for localhost
update hostbasedsetting set office365clientid = '88c8f2a2-4f4b-489c-81ec-7f75f6050c4e' where hostname = 'localhost:8080';

update hostbasedsetting set office365clientsecret = 'fgRqdn7FeLh5TXBYCqja4MT' where hostname = 'localhost:8080';

---for servers

update hostbasedsetting set office365clientid = '92771783-ad44-4652-964f-704491c69e85' where hostname in ('aws.kpi.com', 'app.kpi.com', 'accounts.kpi.com');

update hostbasedsetting set office365clientsecret = 'OTdqRyHmU2aAeTeoUjuB0yV' where hostname in ('aws.kpi.com', 'app.kpi.com', 'accounts.kpi.com');