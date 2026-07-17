delete from "anv".emailtemplate where categoryid = (SELECT id FROM "anv".reference WHERE code = 'PAYSLIP_APPROVED_TO_EMPLOYEE');
delete from "anv".reference where code = 'PAYSLIP_APPROVED_TO_EMPLOYEE';
INSERT INTO "anv".reference (sorder, code, name, isactive, issystemreference, shared, parentid)
VALUES (33, 'PAYSLIP_APPROVED_TO_EMPLOYEE', 'Payslip Approved to Employee', TRUE, TRUE, TRUE, (SELECT id
                                                                                               FROM "anv".reference
                                                                                               WHERE code =
                                                                                                     'ET_PAYROLL_MODULE'));

INSERT INTO "anv".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en', '<p>Dear ${employeefirstname} ${employeelastname}</p>
<p>Please, be advised that ${approverfirstname} ${approverlastname} has approved your payslip for ${month}, ${year} on ${approveddate}</p>
<p>The PDF version of the Payslip is attached for your attention.</p>', 'Approved Payslip to Employee', 'Payslip for ${month}',
        (SELECT id FROM "anv".reference WHERE code = 'PAYSLIP_APPROVED_TO_EMPLOYEE'), (SELECT id FROM "anv".reference WHERE code = 'ET_PAYROLL_MODULE'), FALSE);


delete from "0".emailtemplate where categoryid = (SELECT id FROM "0".reference WHERE code = 'PAYSLIP_APPROVED_TO_EMPLOYEE');
delete from "0".reference where code = 'PAYSLIP_APPROVED_TO_EMPLOYEE';
INSERT INTO "0".reference (sorder, code, name, isactive, issystemreference, shared, parentid)
VALUES (33, 'PAYSLIP_APPROVED_TO_EMPLOYEE', 'Payslip Approved to Employee', TRUE, TRUE, TRUE, (SELECT id
                                                                                               FROM "0".reference
                                                                                               WHERE code =
                                                                                                     'ET_PAYROLL_MODULE'));

INSERT INTO "0".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_EMAIL_TEMPLATE', TRUE, 'en', '<p>Dear ${employeefirstname} ${employeelastname}</p>
<p>Please, be advised that ${approverfirstname} ${approverlastname} has approved your payslip for ${month}, ${year} on ${approveddate}</p>
<p>The PDF version of the Payslip is attached for your attention.</p>', 'Approved Payslip to Employee', 'Payslip for ${month}',
        (SELECT id FROM "0".reference WHERE code = 'PAYSLIP_APPROVED_TO_EMPLOYEE'), (SELECT id FROM "0".reference WHERE code = 'ET_PAYROLL_MODULE'), FALSE);
