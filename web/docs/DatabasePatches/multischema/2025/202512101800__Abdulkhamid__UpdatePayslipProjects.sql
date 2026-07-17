update "anv".payslipTableItem pti
set projectid=subd.ptprojectid
from (select pti.id ptiid, pt.projectId ptprojectid
      from "anv".payslipTableItem pti
               join "anv".payslipTable pt on pti.payslipTable_id = pt.id
      where pt.projectId is not null) subd
where pti.projectid is null
  and pti.id = subd.ptiid;
