update "anv".crmContact set primaryContact = true where id in
(select min(id) from "anv".crmContact where deleted is not true and crmAccount is not null group by crmAccount) and crmAccount not in
(select crmAccount from "anv".crmContact where deleted is not true and primaryContact is true and crmAccount is not null);

--Schema updatedan keyin urilsin
update "anv".workflow_alerts set emailSetting = (select id from "anv".autoresponse where active is true and deleted is not true and companyEmail is true);