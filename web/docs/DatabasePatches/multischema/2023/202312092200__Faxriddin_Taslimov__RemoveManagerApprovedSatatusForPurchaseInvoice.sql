update "anv".invoice i
set status_id=(select id from "anv".reference where code = 'APPROVE' and parentid = (select id from "anv".reference where code = 'INVOICE_STATUS' limit 1)),
    overallstatus= (select id from "anv".reference where code = 'APPROVE' and parentid = (select id from "anv".reference where code = 'INVOICE_STATUS' limit 1))
from (select i.id
      from "anv".invoice i
             left join "anv".reference st on i.status_id = st.id
             left join "anv".reference st2 on i.overallstatus = st2.id
      where st.code = 'MANAGER_APPROVE'
         or st2.code = 'MANAGER_APPROVE'
     ) subi
where i.id = subi.id;