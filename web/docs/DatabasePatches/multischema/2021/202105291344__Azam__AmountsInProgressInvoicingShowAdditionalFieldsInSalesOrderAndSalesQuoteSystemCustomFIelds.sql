
--show additional fields
delete from "0".companycustomfieldssettings  where entityname='SaleOrderSystem' and columncode='inputshowmore';
INSERT INTO "0".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'showmore', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputshowmore', null, 'System', false, null, null, 'SaleOrderSystem', 'Show Additional Fields', false, false, null, '',
        '', null, null, null, false, false, 'Link', null, null, null, true);

--amounts
delete from "0".companycustomfieldssettings  where entityname='SaleOrderSystem' and columncode='inputtaxcalc';
INSERT INTO "0".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'taxcalc', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputtaxcalc', null, 'System', false, null, null, 'SaleOrderSystem', 'Amounts', false, false, null,
        '', '', null, null, null, false, false, 'ListBox', null, null, null, true);

--progress invoicing
delete from "0".companycustomfieldssettings  where entityname='SaleOrderSystem' and columncode='inputprogressinvoicing';
INSERT INTO "0".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'progressinvoicing', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputprogressinvoicing', null, 'System', false, null, null, 'SaleOrderSystem', 'Progress Invoicing', false, false, null, '',
        '', null, null, null, false, false, 'CheckBox', null, null, null, true);

--show addtional fields
delete from "anv".companycustomfieldssettings  where entityname='SaleOrderSystem' and columncode='inputshowmore';
INSERT INTO "anv".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'showmore', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputshowmore', null, 'System', false, null, null, 'SaleOrderSystem', 'Show Additional Fields', false, false, null, '',
        '', null, null, null, false, false, 'Link', null, null, null, true);

--amounts
delete from "anv".companycustomfieldssettings  where entityname='SaleOrderSystem' and columncode='inputtaxcalc';
INSERT INTO "anv".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'taxcalc', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputtaxcalc', null, 'System', false, null, null, 'SaleOrderSystem', 'Amounts', false, false, null,
        '', '', null, null, null, false, false, 'ListBox', null, null, null, true);

--progress invoicing
delete from "anv".companycustomfieldssettings  where entityname='SaleOrderSystem' and columncode='inputprogressinvoicing';
INSERT INTO "anv".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'progressinvoicing', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputprogressinvoicing', null, 'System', false, null, null, 'SaleOrderSystem', 'Progress Invoicing', false, false, null, '',
        '', null, null, null, false, false, 'CheckBox', null, null, null, true);

--Sales Quote
--show addtional fields
delete from "0".companycustomfieldssettings  where entityname='SaleQuoteSystem' and columncode='inputshowmore';
INSERT INTO "0".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'showmore', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputshowmore', null, 'System', false, null, null, 'SaleQuoteSystem', 'Show Additional Fields', false, false, null, '',
        '', null, null, null, false, false, 'Link', null, null, null, true);

--amounts
delete from "0".companycustomfieldssettings  where entityname='SaleQuoteSystem' and columncode='inputtaxcalc';
INSERT INTO "0".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'taxcalc', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputtaxcalc', null, 'System', false, null, null, 'SaleQuoteSystem', 'Amounts', false, false, null,
        '', '', null, null, null, false, false, 'ListBox', null, null, null, true);

--progress invoicing
delete from "0".companycustomfieldssettings  where entityname='SaleQuoteSystem' and columncode='inputprogressinvoicing';
INSERT INTO "0".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'progressinvoicing', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputprogressinvoicing', null, 'System', false, null, null, 'SaleQuoteSystem', 'Progress Invoicing', false, false, null, '',
        '', null, null, null, false, false, 'CheckBox', null, null, null, true);

--show addtional fields
delete from "anv".companycustomfieldssettings  where entityname='SaleQuoteSystem' and columncode='inputshowmore';
INSERT INTO "anv".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'showmore', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputshowmore', null, 'System', false, null, null, 'SaleQuoteSystem', 'Show Additional Fields', false, false, null, '',
        '', null, null, null, false, false, 'Link', null, null, null, true);

--amounts
delete from "anv".companycustomfieldssettings  where entityname='SaleQuoteSystem' and columncode='inputtaxcalc';
INSERT INTO "anv".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'taxcalc', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputtaxcalc', null, 'System', false, null, null, 'SaleQuoteSystem', 'Amounts', false, false, null,
        '', '', null, null, null, false, false, 'ListBox', null, null, null, true);

--progress invoicing
delete from "anv".companycustomfieldssettings  where entityname='SaleQuoteSystem' and columncode='inputprogressinvoicing';
INSERT INTO "anv".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser,
                                             modificationdate, clickable, columncode, columnwidth, datatype, disabled,
                                             entitycategoryalias, entitycategoryname, entityname, fieldname,
                                             isfacetable, isrequired, lookuptype, predefinedvalues,
                                             predefinedvalueswithsorting, prefix, query, relationship,
                                             showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid,
                                             referenceid, active)
VALUES (false, 'progressinvoicing', null, '2021-05-29 13:45:42.994122', false, '2021-05-29 13:45:42.994122', false,
        'inputprogressinvoicing', null, 'System', false, null, null, 'SaleQuoteSystem', 'Progress Invoicing', false, false, null, '',
        '', null, null, null, false, false, 'CheckBox', null, null, null, true);


