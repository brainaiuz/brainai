with target as (select id from "anv".property where shortcut = 'Loc' and lnameid is null),
     inserted as (insert into "anv".customFormLocalization (arabicname, englishname, russianname, uzbekname)
         select 'موقع', 'Location', 'Местоположение', 'Joylashuv'
         from target
         returning id)
update "anv".property p
set lnameid = i.id
from inserted i
where p.id in (select id from target);


with target as (select id from "anv".property where shortcut = 'Loc' and lpluralid is null),
     inserted as (insert into "anv".customFormLocalization (arabicname, englishname, russianname, uzbekname)
         select 'المواقع', 'Locations', 'Местоположения', 'Joylashuvlar'
         from target
         returning id)
update "anv".property p
set lpluralid = i.id
from inserted i
where p.id in (select id from target);