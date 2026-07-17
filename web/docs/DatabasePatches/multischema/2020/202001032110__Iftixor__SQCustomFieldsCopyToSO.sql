

insert into "anv".companycustomfieldssettings (aliasname, createdby_id, creationdate,modificationdate, clickable, columncode,datatype, entitycategoryalias,entitycategoryname,entityname,fieldname, isfacetable, isrequired,predefinedvalues, predefinedvalueswithsorting, query, relationship,showinfiltergrouping, showinlisting,uitype,modifiedby_id, entitytypeid, disabled,prefix,columnWidth, lookuptype, issuperuser)
select cf.aliasname, cf.createdby_id, cf.creationdate,cf.modificationdate, cf.clickable, cf.columncode,cf.datatype, cf.entitycategoryalias,cf.entitycategoryname,'SaleOrder',cf.fieldname, cf.isfacetable, cf.isrequired,cf.predefinedvalues, cf.predefinedvalueswithsorting, cf.query, cf.relationship,cf.showinfiltergrouping, cf.showinlisting,cf.uitype,cf.modifiedby_id, cf.entitytypeid, cf.disabled,cf.prefix,cf.columnWidth, cf.lookuptype, cf.issuperuser from "anv".companycustomfieldssettings cf where cf.entityname='SaleQuote';

