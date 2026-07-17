

update "anv".itemtable_settings set settingsJSONData=replace(settingsJSONData, 'COST','UNITPRICE') where  section='RFQ_ITEM';
