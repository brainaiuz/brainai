
insert into "0".reportingpermission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode,ismobile,isadvancedmode,companyid)
select code,context,name,sorder,ismainmenu,parent,iscore,modulecode,ismobile,isadvancedmode,companyid from permission
where isMainMenu is true or (context ='REPORTING' and (companyid is null or companyid=anv))  on conflict do nothing;


insert into "anv".reportingpermission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode,ismobile,isadvancedmode,companyid)
select code,context,name,sorder,ismainmenu,parent,iscore,modulecode,ismobile,isadvancedmode,companyid from permission
where isMainMenu is true or (context ='REPORTING' and (companyid is null or companyid=anv))  on conflict do nothing;