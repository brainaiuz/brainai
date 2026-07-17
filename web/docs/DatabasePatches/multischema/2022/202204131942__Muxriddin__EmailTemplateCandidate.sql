
delete from "anv".reference where parentid = (select id from "anv".reference where code = '_EMAIL_TEMPLATE_MODULE') and code = 'ET_CANDIDATE_MODULE';
insert into "anv".reference (sorder, code, name, isremovable, issystemreference, shared, iscustombutton,showInMessageCenter, parentid)
values (33, 'ET_CANDIDATE_MODULE', 'Candidate', false, true, false, false,true, (select id from "anv".reference where code = '_EMAIL_TEMPLATE_MODULE'));
