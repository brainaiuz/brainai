update "anv".mailList set active = true where status is null or status =
(select id from "anv".reference where code = 'MLS_ACTIVE' and parentid =
(select id from "anv".reference where code = '_MAIL_LIST_STATUSES'));

update "anv".mailList set active = false where status =
(select id from "anv".reference where code = 'INACTIVE' and parentid =
(select id from "anv".reference where code = '_MAIL_LIST_STATUSES'));