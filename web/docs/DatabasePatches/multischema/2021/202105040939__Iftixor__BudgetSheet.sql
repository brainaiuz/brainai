
delete from "anv".container_item where propertyid= (select id from "anv".property where objectName='budgetsheetView' limit 1) and containerId=(select id from "anv".container where code='report' limit 1);
insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='budgetsheetView' limit 1), (select id from "anv".container where code='report' limit 1), 15, 'accounting');


delete from "anv".container_item where propertyid= (select id from "anv".property where objectName='budgetsheetView' limit 1) and containerId=(select id from "anv".container where code='crmWelcome' limit 1);
insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CRM_MODULE' limit 1), (select id from "anv".property where objectName='budgetsheetView' limit 1), (select id from "anv".container where code='crmWelcome' limit 1), 0, 'crm');



update "anv".property set modulecode='accounting,crm,pm,profile,settings' where objectname='saleinvoice';
update "anv".property set modulecode='accounting,crm,pm,profile,settings' where objectname='salequote';
update "anv".property set modulecode='accounting,crm,pm,profile,settings' where objectname='saleorder';
update "anv".property set modulecode='accounting,logistics,crm,profile,pm,settings' where objectname='purchaseorder';
update "anv".property set modulecode='accounting,logistics,crm,settings' where objectname='purchaseinvoice';
update "anv".property set modulecode='accounting,pm,hrms,settings' where objectname='EXPENSES_CLAIM';
update "anv".property set modulecode='accounting,crm' where objectname='budgetsheetView';