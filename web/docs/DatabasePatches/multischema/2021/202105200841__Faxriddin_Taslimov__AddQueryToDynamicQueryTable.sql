

insert into dynamic_query (query_name,query_text) values ('timesheet_app_all_project_lookup','select p.id pid, p.number || '' -> ''||p.name pname
from "87739".project p
       left join "87739".projectcustomfields pcf on p.projectcustomfieldsid = pcf.id
where p.isdeleted is not true
  and lower(p.number || '' -> '' || p.name) ilike (''%'' ||?|| ''%'')');

insert into dynamic_query (query_name,query_text) values ('timesheet_app_regular_project_lookup','select p.id pid, p.number || '' -> ''||p.name pname
from "87739".project p
       join "87739".projectcustomfields pcf on p.projectcustomfieldsid = pcf.id
where p.isdeleted is not true
  and pcf.string_value1 = ''Regular''
  and lower(p.number || '' -> '' || p.name) ilike (''%'' ||?|| ''%'')');


insert into dynamic_query (query_name,query_text) values ('timesheet_app_extra_project_lookup','select p.id pid, p.number || '' -> ''||p.name pname
from "87739".project p
       join "87739".projectcustomfields pcf on p.projectcustomfieldsid = pcf.id
where p.isdeleted is not true
  and pcf.string_value1 = ''Extra''
  and lower(p.number || '' -> '' || p.name) ilike (''%'' ||?|| ''%'')');
