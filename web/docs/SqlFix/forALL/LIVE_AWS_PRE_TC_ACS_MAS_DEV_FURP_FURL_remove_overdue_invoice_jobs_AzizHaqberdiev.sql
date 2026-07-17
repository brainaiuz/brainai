--bu update urilishidan oldin shu job li companyid lar supportga berilsin active clientlarni inform qilish uchun
--select distinct companyid from recurrence where jobid = 1;

update recurrence set changed=true,deleted = true where jobid = 1;