
delete from "anv".module_localize where modulecode='pm';
insert into "anv".module_localize (moduleCode,isActive,name)
select modulecode,true,plural from "anv".property where objectname='project' and isactive is true;

delete from "anv".module_localize where modulecode='hrms';
insert into "anv".module_localize (moduleCode,isActive,name)
select modulecode,true,plural from "anv".property where objectname='Hrms' and isactive is true;