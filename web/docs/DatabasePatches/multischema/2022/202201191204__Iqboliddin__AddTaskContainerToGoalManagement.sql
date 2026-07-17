delete
from "anv".container_item
where modulecode = 'hrms'
  and sorder = ((select sorder
                 from "anv".container_item
                 where propertyid = (select id from "anv".property where objectname = 'projectgoal')) + 1)
  and containerid = (select containerid
                     from "anv".container_item
                     where propertyid = (select id from "anv".property where objectname = 'projectgoal'))
  and moduleid = (select moduleid
                  from "anv".container_item
                  where propertyid = (select id from "anv".property where objectname = 'projectgoal'))
  and propertyid = (select id from "anv".property where objectname = 'task');


insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values (true, 'hrms', ((select sorder
                        from "anv".container_item
                        where propertyid = (select id from "anv".property where objectname = 'projectgoal')) + 1),
        (select containerid
         from "anv".container_item
         where propertyid = (select id from "anv".property where objectname = 'projectgoal')),
        (select moduleid
         from "anv".container_item
         where propertyid = (select id from "anv".property where objectname = 'projectgoal')),
        (select id from "anv".property where objectname = 'task'));