delete from "anv".customformsection where form_id = 'TASK_MAX_FORM' and section = 'TASK_STATUS_HISTORY';
insert into "anv".customformsection(form_id,section,active,sorder) values
('TASK_MAX_FORM','TASK_STATUS_HISTORY',false,(select sorder from "anv".customformsection where form_id = 'TASK_MAX_FORM' and section = 'ADDITIONAL_INFORMATION'));

delete from "anv".customformsection where form_id = 'TASK_MAX_FORM' and section = 'TASK_HISTORY_LOG';

update "anv".customformsection set sorder = (select sorder+1 from "anv".customformsection where form_id = 'TASK_MAX_FORM' and section = 'ADDITIONAL_INFORMATION') where form_id = 'TASK_MAX_FORM' and section = 'ADDITIONAL_INFORMATION';

delete from "anv".modelfield where field_id = 'TASK_STATUS_HISTORY' and form_id = 'TASK_MAX_FORM';
insert into "anv".modelfield(field_id,form_id,section,sorder,widget,nolabelfor,type,fullwidth,fieldsetstyle,fieldstyle,rowstyle,sectionstyle,columntype,fsection)
values('TASK_STATUS_HISTORY','TASK_MAX_FORM','TASK_STATUS_HISTORY',(select max(sorder)+1 from "anv".modelfield where form_id='TASK_MAX_FORM'),'UNKNOWN','','text',true,'slideDown-content group nobrd','field','row hideCustomField','slideDown-box  group expand hideCustomField','COL_1','TASK_STATUS_HISTORY');

delete from "anv".modelfield where field_id = 'TASK_HISTORY_LOG' and form_id = 'TASK_MAX_FORM';