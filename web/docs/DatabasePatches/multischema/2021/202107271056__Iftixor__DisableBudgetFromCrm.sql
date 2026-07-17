
update "anv".container_item set isactive =false where modulecode='crm' and propertyid = (select id from "anv".property where objectname='budgetsheetView' limit 1);