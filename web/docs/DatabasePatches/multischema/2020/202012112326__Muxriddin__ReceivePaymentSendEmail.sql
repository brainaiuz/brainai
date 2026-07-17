
insert into "0".reference (sorder, code, name, isremovable, issystemreference, shared, iscustombutton, parentid)
values ((select count(*)+1 from "0".reference where parentid = (select id from "0".reference where code = '_EMAIL_TEMPLATE_MODULE' limit 1)), 'ET_RECEIVE_PAYMENT_MODULE', 'Receive Payment', false, true, false, false, (select id from "0".reference where code = '_EMAIL_TEMPLATE_MODULE' limit 1));

insert into "266582".reference (sorder, code, name, isremovable, issystemreference, shared, iscustombutton, parentid)
values ((select count(*)+1 from "266582".reference where parentid = (select id from "266582".reference where code = '_EMAIL_TEMPLATE_MODULE' limit 1)), 'ET_RECEIVE_PAYMENT_MODULE', 'Receive Payment', false, true, false, false, (select id from "266582".reference where code = '_EMAIL_TEMPLATE_MODULE' limit 1));

INSERT INTO "0".reference(attendancelr, autoapprove, code, deleted, isactive, iscustombutton, isremovable,
                              issystemreference, leavedays, name, shared, sorder, parentid, hasprorata)
VALUES (false, false, 'RECEIVE_PAYMENT_CATEGORY', false, true, false, false, true, 0, 'Reveive Payment', true, 1,(SELECT id FROM "0".Reference WHERE code = 'ET_RECEIVE_PAYMENT_MODULE' limit 1), false);

INSERT INTO "266582".reference(attendancelr, autoapprove, code, deleted, isactive, iscustombutton, isremovable,
                              issystemreference, leavedays, name, shared, sorder, parentid, hasprorata)
VALUES (false, false, 'RECEIVE_PAYMENT_CATEGORY', false, true, false, false, true, 0, 'Receive Payment', true, 1,(SELECT id FROM "266582".Reference WHERE code = 'ET_RECEIVE_PAYMENT_MODULE' limit 1), false);


INSERT INTO "0".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_RECEIVE_PAYMENT_EMAIL_TEMPLATE', TRUE, 'en',
'<p>Dear ${paymentcustomer}, </p> <p>This is a receive payment statement for the date of ${paymentdate}.
Please see the attached receive payment statement for more details.</p> <p>Thank you for your business and please contact us at <b><u> ${email} </u></b>
should you have any questions regarding this invoice statement.</p><p >Sincerely yours,</br>Accounting Department,</br><b> ${paymentcompanyname} </b></p>',

'Receive Payment Default Template',
'Receive Payment',
(SELECT id FROM "0".reference WHERE code = 'RECEIVE_PAYMENT_CATEGORY' limit 1),
(SELECT id FROM "0".reference WHERE code = 'ET_RECEIVE_PAYMENT_MODULE' limit 1), FALSE );


INSERT INTO "266582".emailtemplate (fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id, showinmessagecenter)
VALUES (-1, 'DEFAULT_RECEIVE_PAYMENT_EMAIL_TEMPLATE', TRUE, 'en',
'<p>Dear ${paymentcustomer}, </p> <p>This is a receive payment statement for the date of ${paymentdate}.
Please see the attached receive payment statement for more details.</p> <p>Thank you for your business and please contact us at <b><u> ${email} </u></b>
should you have any questions regarding this invoice statement.</p><p >Sincerely yours,</br>Accounting Department,</br><b> ${paymentcompanyname} </b></p>',

'Receive Payment Default Template',
'Receive Payment',
(SELECT id FROM "266582".reference WHERE code = 'RECEIVE_PAYMENT_CATEGORY' limit 1),
(SELECT id FROM "266582".reference WHERE code = 'ET_RECEIVE_PAYMENT_MODULE' limit 1), FALSE);
