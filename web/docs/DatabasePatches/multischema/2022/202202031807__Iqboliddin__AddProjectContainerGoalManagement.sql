delete
from "anv".container_item
where modulecode = 'hrms'
  and propertyid in (select id from "anv".property where objectname = 'subProjectList');


INSERT INTO "anv".property(isactive, convertitems, defaultname, fid, form_id, form_type, iscustom, last_modified_date,
                           modulecode, objectname, plural, shortcut, singular, user_id)
select false,
       null,
       'Projects',
       null,
       null,
       null,
       false,
       null,
       'pm',
       'projectList',
       'Projects',
       'P',
       'Project',
       null
from "anv".property
WHERE NOT EXISTS(
        select * from "anv".property where objectname = 'projectList'
    ) limit 1;


insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
select true,
       'hrms',
       (select sorder
        from "anv".container_item
        where propertyid = (select id from "anv".property where objectname = 'projectgoal')) + 2,
       (select containerid
        from "anv".container_item
        where propertyid = (select id from "anv".property where objectname = 'projectgoal')),
       (select moduleid
        from "anv".container_item
        where propertyid = (select id from "anv".property where objectname = 'projectgoal')),
       (select id from "anv".property where objectname = 'projectList')
from "anv".container_item
where not exists(
        select *
        from "anv".container_item
        where propertyid = (select id from "anv".property where objectname = 'projectList')
    ) limit 1;