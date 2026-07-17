
insert into "0".reference (sorder, code, name, isremovable, issystemreference, shared, iscustombutton, parentid)
values (32, 'ET_RFQ_MODULE', 'Request For Quote', false, true, false, false, (select id from "0".reference where code = '_EMAIL_TEMPLATE_MODULE'));

insert into "anv".reference (sorder, code, name, isremovable, issystemreference, shared, iscustombutton, parentid)
values (32, 'ET_RFQ_MODULE', 'Request For Quote', false, true, false, false, (select id from "anv".reference where code = '_EMAIL_TEMPLATE_MODULE'));
