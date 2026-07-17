update "anv".reference set deleted = false where code = 'CANDIDATE_STATUS_HIRED';
insert into "anv".reference (code,color,isactive,name,shared,parentid,description) values ('CANDIDATE_STATUS_LOST','#CC0000',true,'Lost',true,(select id from "anv".reference where code = '_CANDIDATE_STATUS' limit 1),'0');
