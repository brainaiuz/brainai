
INSERT INTO "0".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en',
'<p>Thank you for your business and please feel free to contact if you will have any questions regarding the statement</p>

<p>Sincerely yours,</p>

<p>${company_name}</p>',

'Supplier Balance Default Template',
'Supplier Balance',
(SELECT id FROM "0".reference WHERE code = 'SUPPLIER_BALANCE_CATEGORY'),
(SELECT id FROM "0".reference WHERE code = 'ET_BALANCE_MODULE'), FALSE);


INSERT INTO "anv".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en',
'<p>Thank you for your business and please feel free to contact if you will have any questions regarding the statement</p>

<p>Sincerely yours,</p>

<p>${company_name}</p>',

'Supplier Balance Default Template',
'Supplier Balance',
(SELECT id FROM "anv".reference WHERE code = 'SUPPLIER_BALANCE_CATEGORY'),
(SELECT id FROM "anv".reference WHERE code = 'ET_BALANCE_MODULE'), FALSE);
