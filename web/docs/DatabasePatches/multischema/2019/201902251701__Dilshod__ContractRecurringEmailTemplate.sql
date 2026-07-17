

-------- ZERO SCHEMA --------------

delete from "0".emailtemplate where code = 'DEFAULT_CONTRACT_REMINDER_TEMPLATE';
delete from "0".reference where code = 'ET_CONTRACT_REMINDER_MODULE';
delete from "0".reference where code = 'PM_CONTRACT_REMINDER';

--- ET_CONTRACT_REMINDER_MODULE
insert into "0".reference (sorder, code, name, isremovable, issystemreference, shared, iscustombutton, parentid)
values ((select max(sorder)+1 from "0".reference
where parentid = (select id from "0".reference where code = '_EMAIL_TEMPLATE_MODULE')), 'ET_CONTRACT_REMINDER_MODULE', 'Contract Reminder Template', false, true, false, false, (select id from "0".reference where code = '_EMAIL_TEMPLATE_MODULE'));


-- PM_CONTRACT_REMINDER
insert into "0".reference (sorder,                             code,  name,                  isremovable, issystemreference, shared, iscustombutton, parentid)
values                       (1,   'PM_CONTRACT_REMINDER',               'Contract Reminder', false,       true,              false,  false,         (select id from "0".reference where code = 'ET_CONTRACT_REMINDER_MODULE'));


-- DEFAULT_CONTRACT_REMINDER_TEMPLATE
insert into "0".emailtemplate(code, deleted, fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id)
     VALUES (
   'DEFAULT_CONTRACT_REMINDER_TEMPLATE', false, -1, 'DEFAULT_EMAIL_TEMPLATE', true, 'en', '<p>Dear ${receiving_employeename},</p>
<p>This is to remind you that ${left_time_to_due_date} left to due date of ${contract_number} contract .</p>
<p>Click <a href="${link}">here</a> to view the contract details.</p>
<p>* If you are not able to click on the link, you can instead copy and paste the following address into your web browser:</p>
<p>${link}</p>',
    'Contract Reminder', 'Contract Reminder', (select id from "0".reference where code='PM_CONTRACT_REMINDER'), (select id from "0".reference where code='ET_CONTRACT_REMINDER_MODULE'));


------- ALL SCHEMAS -------------

delete from "anv".emailtemplate where code = 'DEFAULT_CONTRACT_REMINDER_TEMPLATE';
delete from "anv".reference where code = 'ET_CONTRACT_REMINDER_MODULE';
delete from "anv".reference where code = 'PM_CONTRACT_REMINDER';

--- ET_CONTRACT_REMINDER_MODULE
insert into "anv".reference (sorder, code, name, isremovable, issystemreference, shared, iscustombutton, parentid)
values ((select max(sorder)+1 from "anv".reference
where parentid = (select id from "anv".reference where code = '_EMAIL_TEMPLATE_MODULE')), 'ET_CONTRACT_REMINDER_MODULE', 'Contract Reminder Template', false, true, false, false, (select id from "anv".reference where code = '_EMAIL_TEMPLATE_MODULE'));


-- PM_CONTRACT_REMINDER
insert into "anv".reference (sorder,                             code,  name,                  isremovable, issystemreference, shared, iscustombutton, parentid)
values                       (1,   'PM_CONTRACT_REMINDER',               'Contract Reminder', false,       true,              false,  false,         (select id from "anv".reference where code = 'ET_CONTRACT_REMINDER_MODULE'));


-- DEFAULT_CONTRACT_REMINDER_TEMPLATE
insert into "anv".emailtemplate(code, deleted, fromuserid, iscompanyemailtemplate, isdefault, locale, messagehtml, name, subject, categoryid, module_id)
     VALUES (
   'DEFAULT_CONTRACT_REMINDER_TEMPLATE', false, -1, 'DEFAULT_EMAIL_TEMPLATE', true, 'en', '<p>Dear ${receiving_employeename},</p>
<p>This is to remind you that ${left_time_to_due_date} left to due date of ${contract_number} contract .</p>
<p>Click <a href="${link}">here</a> to view the contract details.</p>
<p>* If you are not able to click on the link, you can instead copy and paste the following address into your web browser:</p>
<p>${link}</p>',
    'Contract Reminder', 'Contract Reminder', (select id from "anv".reference where code='PM_CONTRACT_REMINDER'), (select id from "anv".reference where code='ET_CONTRACT_REMINDER_MODULE'));

