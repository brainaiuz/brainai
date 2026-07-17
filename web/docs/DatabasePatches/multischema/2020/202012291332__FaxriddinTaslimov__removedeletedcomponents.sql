delete
from "anv".default_components
where id in (select dc.id
             from "anv".default_components dc
                    left join "anv".reporting r on dc.report_code = r.code
             where dc.report_code is not null
               and (r.id is null or r.deleted is true))