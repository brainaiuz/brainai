
update "anv".crmContact set  status = (select id from "anv".reference where code='ATTEMPTED_TO_CONTACT' and deleted is not true and parentid=(select id from "anv".reference where code='_LEAD_STATUS'))   where status is null and contactType=5 and deleted is not true ;

update "anv".crmCase set  status = (select id from "anv".reference where code='NEW' and deleted is not true and parentid=(select id from "anv".reference where code='_CASE_STATUS'))   where status is null and deleted is not true ;