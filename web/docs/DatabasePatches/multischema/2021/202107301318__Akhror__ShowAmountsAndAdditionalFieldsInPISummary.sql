--show addtional fields
delete from "anv".companycustomfieldssettings  where entityname='PurchaseInvoiceSystem' and columncode='inputshowmore';
INSERT INTO "anv".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                               modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                               entitycategoryalias, entitycategoryname, entityname, fieldname,
                                               isfacetable, isrequired, lookuptype, predefinedvalues,
                                               predefinedvalueswithsorting, prefix, query, relationship,
                                               showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                               referenceid, active)
VALUES (false, 'showmore', null, '2021-07-30 13:00:42.994122', false, '2021-07-30 13:00:42.994122', false,
        'inputshowmore', null, 'System', false, null, null, 'PurchaseInvoiceSystem', 'Show Additional Fields', false, false, null, '',
        '', null, null, null, false, false, 'Link', null, null, null, true);

--amounts
delete from "anv".companycustomfieldssettings  where entityname='PurchaseInvoiceSystem' and columncode='inputtaxcalc';
INSERT INTO "anv".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                               modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                               entitycategoryalias, entitycategoryname, entityname, fieldname,
                                               isfacetable, isrequired, lookuptype, predefinedvalues,
                                               predefinedvalueswithsorting, prefix, query, relationship,
                                               showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                               referenceid, active)
VALUES (false, 'taxcalc', null, '2021-07-30 13:00:42.994122', false, '2021-07-30 13:00:42.994122', false,
        'inputtaxcalc', null, 'System', false, null, null, 'PurchaseInvoiceSystem', 'Amounts', false, false, null,
        '', '', null, null, null, false, false, 'ListBox', null, null, null, true);