insert into "0".crmaccount_owners(crmaccount_id,owner_id)
select a.id crmaccount_id,a.owner owner_id from "0".crmaccount a where a.deleted is null or a.deleted is not true and a.owner is not null;


insert into "anv".crmaccount_owners(crmaccount_id,owner_id)
select a.id crmaccount_id,a.owner owner_id from "anv".crmaccount a where a.deleted is null or a.deleted is not true and a.owner is not null;