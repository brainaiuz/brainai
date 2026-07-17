
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFormToContainer('crm', 'CRM_MODULE')) WHERE  id=(SELECT id FROM company LIMIT 1);

UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFormToContainer('hrms', 'HRMS_MODULE')) WHERE  id=(SELECT id FROM company LIMIT 1);
