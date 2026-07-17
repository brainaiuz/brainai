--For schema 0
DELETE FROM "0".emailtemplate WHERE categoryid in (SELECT id FROM "0".reference WHERE code in ('BILL_OF_MATERIALS_SUBMITTED','BILL_OF_MATERIALS_APPROVED','BILL_OF_MATERIALS_REJECTED')) and iscompanyemailtemplate='DEFAULT_EMAIL_TEMPLATE';
delete from "0".reference where code in ('BILL_OF_MATERIALS_SUBMITTED', 'BILL_OF_MATERIALS_APPROVED', 'BILL_OF_MATERIALS_REJECTED');
INSERT INTO "0".reference (code, name, parentid) VALUES('BILL_OF_MATERIALS_SUBMITTED','Bill Of materials(Submitted for approval)', (SELECT id FROM "0".reference WHERE code = 'ET_PROJECT_MODULE'));
INSERT INTO "0".reference (code, name, parentid) VALUES('BILL_OF_MATERIALS_APPROVED','Bill Of materials(Approved)', (SELECT id FROM "0".reference WHERE code = 'ET_PROJECT_MODULE'));
INSERT INTO "0".reference (code, name, parentid) VALUES('BILL_OF_MATERIALS_REJECTED','Bill Of materials(Rejected)', (SELECT id FROM "0".reference WHERE code = 'ET_PROJECT_MODULE'));

INSERT INTO "0".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en',
'Dear ${recipientname},

<p>Please be advised that ${currentUser} has submitted a Bill of Materials for your attention on ${date}. The BOM details are as follows:</p>

<p>Project Name: ${projectName}</p>
<p>Project Start Date: ${startdate}</p>

<p>Click <a href="${link}">here</a> to view this BOM where you can approve or decline it.</p>

<p>* If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}</p>',

'Bill of Materials waiting for approval default template',
'Bill of Materials waiting for your approval',
(SELECT id FROM "0".reference WHERE code = 'BILL_OF_MATERIALS_SUBMITTED'),
(SELECT id FROM "0".reference WHERE code = 'ET_PROJECT_MODULE'), FALSE);


INSERT INTO "0".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en',
'Dear ${recipientname},

<p>Please be advised that ${currentUser} has rejected your BOM on ${date}, due to below reason:</p>
<p>${rejectionReason}</p>

<p>Click <a href="${link}">here</a> to view this BOM</p>

<p>* If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}</p>',

'Bill of Materials rejected default template',
'Bill of Materials rejected',
(SELECT id FROM "0".reference WHERE code = 'BILL_OF_MATERIALS_REJECTED'),
(SELECT id FROM "0".reference WHERE code = 'ET_PROJECT_MODULE'), FALSE);



INSERT INTO "0".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en',
'Dear ${recipientname},

<p>Please be advised that ${currentUser} has approved your BOM on ${date}</p>

<p>Click <a href="${link}">here</a> to view this BOM</p>

<p>* If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}</p>',

'Bill of Materials approved default template',
'Bill of Materials approved',
(SELECT id FROM "0".reference WHERE code = 'BILL_OF_MATERIALS_APPROVED'),
(SELECT id FROM "0".reference WHERE code = 'ET_PROJECT_MODULE'), FALSE);









--For schema anv
DELETE FROM "anv".emailtemplate WHERE categoryid in (SELECT id FROM "anv".reference WHERE code in ('BILL_OF_MATERIALS_SUBMITTED','BILL_OF_MATERIALS_APPROVED','BILL_OF_MATERIALS_REJECTED')) and iscompanyemailtemplate='DEFAULT_EMAIL_TEMPLATE';
delete from "anv".reference where code in ('BILL_OF_MATERIALS_SUBMITTED', 'BILL_OF_MATERIALS_APPROVED', 'BILL_OF_MATERIALS_REJECTED');
INSERT INTO "anv".reference (code, name, parentid) VALUES('BILL_OF_MATERIALS_SUBMITTED','Bill Of materials(Submitted for approval)', (SELECT id FROM "anv".reference WHERE code = 'ET_PROJECT_MODULE'));
INSERT INTO "anv".reference (code, name, parentid) VALUES('BILL_OF_MATERIALS_APPROVED','Bill Of materials(Approved)', (SELECT id FROM "anv".reference WHERE code = 'ET_PROJECT_MODULE'));
INSERT INTO "anv".reference (code, name, parentid) VALUES('BILL_OF_MATERIALS_REJECTED','Bill Of materials(Rejected)', (SELECT id FROM "anv".reference WHERE code = 'ET_PROJECT_MODULE'));

INSERT INTO "anv".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en',
'Dear ${recipientname},

<p>Please be advised that ${currentUser} has submitted a Bill of Materials for your attention on ${date}. The BOM details are as follows:</p>

<p>Project Name: ${projectName}</p>
<p>Project Start Date: ${startdate}</p>

<p>Click <a href="${link}">here</a> to view this BOM where you can approve or decline it.</p>

<p>* If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}</p>',

'Bill of Materials waiting for approval default template',
'Bill of Materials waiting for your approval',
(SELECT id FROM "anv".reference WHERE code = 'BILL_OF_MATERIALS_SUBMITTED'),
(SELECT id FROM "anv".reference WHERE code = 'ET_PROJECT_MODULE'), FALSE);


INSERT INTO "anv".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en',
'Dear ${recipientname},

<p>Please be advised that ${currentUser} has rejected your BOM on ${date}, due to below reason:</p>
<p>${rejectionReason}</p>

<p>Click <a href="${link}">here</a> to view this BOM</p>

<p>* If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}</p>',

'Bill of Materials rejected default template',
'Bill of Materials rejected',
(SELECT id FROM "anv".reference WHERE code = 'BILL_OF_MATERIALS_REJECTED'),
(SELECT id FROM "anv".reference WHERE code = 'ET_PROJECT_MODULE'), FALSE);



INSERT INTO "anv".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en',
'Dear ${recipientname},

<p>Please be advised that ${currentUser} has approved your BOM on ${date}</p>

<p>Click <a href="${link}">here</a> to view this BOM</p>

<p>* If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}</p>',

'Bill of Materials approved default template',
'Bill of Materials approved',
(SELECT id FROM "anv".reference WHERE code = 'BILL_OF_MATERIALS_APPROVED'),
(SELECT id FROM "anv".reference WHERE code = 'ET_PROJECT_MODULE'), FALSE);
