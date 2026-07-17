

delete from "0".reference where code='VENDOR';
update "0".reference set  parentid= null where code in ('CONSIGNOR', 'CONSIGNEE', 'INTEGRATOR', 'PRESS', 'ANALYST');

delete from "0_template".reference where code='VENDOR';
update "0_template".reference set  parentid= null where code in ('CONSIGNOR', 'CONSIGNEE', 'INTEGRATOR', 'PRESS', 'ANALYST');

delete from "anv".crmAccount_types where type_id=(select id from "anv".reference where code='VENDOR') and crmaccount_id in (select crmaccount_id from "anv".crmAccount_types where type_id=(select id from "anv".reference where code='SUPPLIER'));
update "anv".crmAccount_types set type_id=(select id from "anv".reference where code='SUPPLIER') where type_id=(select id from "anv".reference where code='VENDOR');
delete from "anv".reference where code='VENDOR';
update "anv".reference set  parentid= null where code in ('CONSIGNOR', 'CONSIGNEE', 'INTEGRATOR', 'PRESS', 'ANALYST');
