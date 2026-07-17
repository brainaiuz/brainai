update message set status = 'PENDING' where statusid =
(select id from "0".reference where code = 'MESSAGE_STATUS_PENDING' and parentid = (select id from "0".reference where code = 'MESSAGE_STATUS'));
update message set status = 'SENT' where statusid =
(select id from "0".reference where code = 'SENT' and parentid = (select id from "0".reference where code = 'MESSAGE_STATUS'));
update message set status = 'FAILED' where statusid =
(select id from "0".reference where code = 'FAILED' and parentid = (select id from "0".reference where code = 'MESSAGE_STATUS'));