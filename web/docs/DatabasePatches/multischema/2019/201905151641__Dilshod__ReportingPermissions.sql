-------------------------------------------------  REPORT TEMPLATES----------------------------------------------------------------

update "0".reportingpermission set parent=0,sorder=1 where code='REPORTING_MAIN_MENU';

delete from "0".reportingpermission where code ilike '%REPORTING_TEMPLATE_CATEGORY_%' or code='REPORTING_TEMPLATE';

insert into "0".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_ACCOUNTING','REPORTING','REPORTING_SYSTEM','Accounts',0,0);
insert into "0".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_CRM','REPORTING','REPORTING_SYSTEM','Sales',0,1);
insert into "0".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_PM','REPORTING','REPORTING_SYSTEM','Projects',0,2);
insert into "0".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_HRMS','REPORTING','REPORTING_SYSTEM','Humans',0,3);
insert into "0".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_PAYROLL','REPORTING','REPORTING_SYSTEM','Payroll',0,4);
insert into "0".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_CUSTOM','REPORTING','REPORTING_SYSTEM','Custom',0,5);
insert into "0".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_SYSTEM','REPORTING','REPORTING_SYSTEM','System',0,6);


--- ACCOUNTING
update "0".reportingpermission set parent=(select id from "0".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_ACCOUNTING')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||0 = rp.code where rtc.code='ACCOUNTING');


--- CRM
update "0".reportingpermission set parent=(select id from "0".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_CRM')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||0 = rp.code where rtc.code='CRM');

--- PM
update "0".reportingpermission set parent=(select id from "0".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_PM')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||0 = rp.code where rtc.code='PM');


--- HRMS
update "0".reportingpermission set parent=(select id from "0".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_HRMS')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||0 = rp.code where rtc.code='HRMS');


--- PAYROLL
update "0".reportingpermission set parent=(select id from "0".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_PAYROLL')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||0 = rp.code where rtc.code='PAYROLL');



--- CUSTOM
update "0".reportingpermission set parent=(select id from "0".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_CUSTOM')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||0 = rp.code where rtc.code='CUSTOM');



--- SYSTEM
update "0".reportingpermission set parent=(select id from "0".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_SYSTEM')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||0 = rp.code where rtc.code='SYSTEM');


update "0".reportingpermission p set parent =
                                             (select max(rp.id) from "0".reportingpermission rp
                                                                    join reportTemplate rt on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"0"','"','') = rp.code
                                                                    join "0".reporting r on rt.code = r.viewcode
                                              where  p.code=r.permissioncode)  WHERE code IN (select r.permissioncode from "0".reportingpermission rp
                                                                    join reportTemplate rt on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"0"','"','') = rp.code
                                                                    join "0".reporting r on rt.code = r.viewcode);


update "anv".reportingpermission set parent=0,sorder=1 where code='REPORTING_MAIN_MENU';

delete from "anv".reportingpermission where code ilike '%REPORTING_TEMPLATE_CATEGORY_%' or code='REPORTING_TEMPLATE';

insert into "anv".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_ACCOUNTING','REPORTING','REPORTING_SYSTEM','Accounts',0,0);
insert into "anv".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_CRM','REPORTING','REPORTING_SYSTEM','Sales',0,1);
insert into "anv".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_PM','REPORTING','REPORTING_SYSTEM','Projects',0,2);
insert into "anv".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_HRMS','REPORTING','REPORTING_SYSTEM','Humans',0,3);
insert into "anv".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_PAYROLL','REPORTING','REPORTING_SYSTEM','Payroll',0,4);
insert into "anv".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_CUSTOM','REPORTING','REPORTING_SYSTEM','Custom',0,5);
insert into "anv".reportingpermission(code,context,modulecode,name,parent,sorder) values('REPORTING_TEMPLATE_CATEGORY_SYSTEM','REPORTING','REPORTING_SYSTEM','System',0,6);


--- ACCOUNTING
update "anv".reportingpermission set parent=(select id from "anv".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_ACCOUNTING')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code where rtc.code='ACCOUNTING');


--- CRM
update "anv".reportingpermission set parent=(select id from "anv".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_CRM')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code where rtc.code='CRM');

--- PM
update "anv".reportingpermission set parent=(select id from "anv".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_PM')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code where rtc.code='PM');


--- HRMS
update "anv".reportingpermission set parent=(select id from "anv".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_HRMS')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code where rtc.code='HRMS');


--- PAYROLL
update "anv".reportingpermission set parent=(select id from "anv".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_PAYROLL')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code where rtc.code='PAYROLL');



--- CUSTOM
update "anv".reportingpermission set parent=(select id from "anv".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_CUSTOM')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code where rtc.code='CUSTOM');



--- SYSTEM
update "anv".reportingpermission set parent=(select id from "anv".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_SYSTEM')
where code in (select distinct rp.code from reportTemplateCategory rtc
                                                join reporttemplate rt on rtc.id = rt.categoryid
                                                join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code where rtc.code='SYSTEM');



update "anv".reportingpermission p set parent =
                                             (select max(rp.id) from "anv".reportingpermission rp
                                                                    join reportTemplate rt on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code
                                                                    join "anv".reporting r on rt.code = r.viewcode
                                              where  p.code=r.permissioncode)  WHERE code IN (select r.permissioncode from "anv".reportingpermission rp
                                                                    join reportTemplate rt on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code
                                                                    join "anv".reporting r on rt.code = r.viewcode);



------------------------- SAVED REPORTS ----------------------------------------------------------------
delete from "0".reportingpermission where code in (select code from (select id,replace(replace(code,'_0',''),'0','') code,name from "0".reportingpermission) foo group by code,name having count(id)>1);

delete from "0".reportingpermission where code ilike '%REPORTING_SAVED_REPORT_CATEGORY_%' OR code ='REPORTING_SAVED_REPORT';

delete from "anv".reportingpermission where code in (select code from (select id,replace(replace(code,'_'||replace('"anv"','"',''),''),replace('"anv"','"',''),'') code,name from "anv".reportingpermission) foo group by code,name having count(id)>1);

delete from "anv".reportingpermission where code ilike '%REPORTING_SAVED_REPORT_CATEGORY_%' OR code ='REPORTING_SAVED_REPORT';


