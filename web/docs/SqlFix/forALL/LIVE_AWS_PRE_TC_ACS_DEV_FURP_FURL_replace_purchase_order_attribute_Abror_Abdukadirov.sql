update "0".emailTemplate set subject = replace(subject, '${customer}', '${supplier}'),
messagehtml = replace(messagehtml, '${customer}', '${supplier}')
where categoryid = (select id from "0".reference where code = 'PURCHASE_ORDER_MANAGER_CATEGORY');

update "anv".emailTemplate set subject = replace(subject, '${customer}', '${supplier}'),
messagehtml = replace(messagehtml, '${customer}', '${supplier}')
where categoryid = (select id from "anv".reference where code = 'PURCHASE_ORDER_MANAGER_CATEGORY');