
delete from "0".permission_context prc
where prc.permissioncode in (select rrp.code from "0".reportingpermission rrp
                             where rrp.id not in (select id from "0".reportingpermission where iscore is true or ismainmenu is true
                                                  union all
                                                  select id from "0".reportingpermission where code ilike ('%REPORTING_TEMPLATE_CATEGORY_%') or code='REPORTING_TEMPLATE'
                                                  union all
                                                  select id from "0".reportingpermission where code ilike ('%REPORTING_SAVED_REPORT_CATEGORY_%') or code='REPORTING_SAVED_REPORT'
                                                  union all
                                                  select rp.id  from reporttemplate rt join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code
                                                  union all
                                                  select rp.id  from reporttemplate rt join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code||'_0'=rp.code
                                                  union all
                                                  select rp.id from "0".reporting r join "0".reportingpermission rp on r.permissioncode=rp.code
                                                  where r.deleted is not true) ) and  prc.contextcode='REPORTING';

delete from "0".rolepermission rolep
where rolep.permissioncode in (select rrp.code from "0".reportingpermission rrp
                               where rrp.id not in (select id from "0".reportingpermission where iscore is true or ismainmenu is true
                                                    union all
                                                    select id from "0".reportingpermission where code ilike ('%REPORTING_TEMPLATE_CATEGORY_%') or code='REPORTING_TEMPLATE'
                                                    union all
                                                    select id from "0".reportingpermission where code ilike ('%REPORTING_SAVED_REPORT_CATEGORY_%') or code='REPORTING_SAVED_REPORT'
                                                    union all
                                                    select rp.id  from reporttemplate rt join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code
                                                    union all
                                                    select rp.id  from reporttemplate rt join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code||'_0'=rp.code
                                                    union all
                                                    select rp.id from "0".reporting r join "0".reportingpermission rp on r.permissioncode=rp.code
                                                    where r.deleted is not true) );


delete from "0".reportingpermission
where id not in (select id from "0".reportingpermission where iscore is true or ismainmenu is true
                 union all
                 select id from "0".reportingpermission where code ilike ('%REPORTING_TEMPLATE_CATEGORY_%') or code='REPORTING_TEMPLATE'
                 union all
                 select id from "0".reportingpermission where code ilike ('%REPORTING_SAVED_REPORT_CATEGORY_%') or code='REPORTING_SAVED_REPORT'
                 union all
                 select rp.id  from reporttemplate rt join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code
                 union all
                 select rp.id  from reporttemplate rt join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code||'_0'=rp.code
                 union all
                 select rp.id from "0".reporting r join "0".reportingpermission rp on r.permissioncode=rp.code
                 where r.deleted is not true
                );




delete from "anv".permission_context prc
where prc.permissioncode in (select rrp.code from "anv".reportingpermission rrp
                             where rrp.id not in (select id from "anv".reportingpermission where iscore is true or ismainmenu is true
                                                  union all
                                                  select id from "anv".reportingpermission where code ilike ('%REPORTING_TEMPLATE_CATEGORY_%') or code='REPORTING_TEMPLATE'
                                                  union all
                                                  select id from "anv".reportingpermission where code ilike ('%REPORTING_SAVED_REPORT_CATEGORY_%') or code='REPORTING_SAVED_REPORT'
                                                  union all
                                                  select rp.id  from reporttemplate rt join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code
                                                  union all
                                                  select rp.id  from reporttemplate rt join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','')=rp.code
                                                  union all
                                                  select rp.id from "anv".reporting r join "anv".reportingpermission rp on r.permissioncode=rp.code
                                                  where r.deleted is not true) ) and  prc.contextcode='REPORTING';

delete from "anv".rolepermission rolep where rolep.permissioncode in (select rrp.code from "anv".reportingpermission rrp
                                                                      where rrp.id not in (select id from "anv".reportingpermission where iscore is true or ismainmenu is true
                                                                                           union all
                                                                                           select id from "anv".reportingpermission where code ilike ('%REPORTING_TEMPLATE_CATEGORY_%') or code='REPORTING_TEMPLATE'
                                                                                           union all
                                                                                           select id from "anv".reportingpermission where code ilike ('%REPORTING_SAVED_REPORT_CATEGORY_%') or code='REPORTING_SAVED_REPORT'
                                                                                           union all
                                                                                           select rp.id  from reporttemplate rt join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code
                                                                                           union all
                                                                                           select rp.id  from reporttemplate rt join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','')=rp.code
                                                                                           union all
                                                                                           select rp.id from "anv".reporting r join "anv".reportingpermission rp on r.permissioncode=rp.code
                                                                                           where r.deleted is not true) );


delete from "anv".reportingpermission
where id not in ( select id from "anv".reportingpermission where iscore is true or ismainmenu is true
                  union all
                  select id from "anv".reportingpermission where code ilike ('%REPORTING_TEMPLATE_CATEGORY_%') or code='REPORTING_TEMPLATE'
                  union all
                  select id from "anv".reportingpermission where code ilike ('%REPORTING_SAVED_REPORT_CATEGORY_%') or code='REPORTING_SAVED_REPORT'
                  union all
                  select rp.id  from reporttemplate rt join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code
                  union all
                  select rp.id  from reporttemplate rt join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','')=rp.code
                  union all
                  select rp.id from "anv".reporting r join "anv".reportingpermission rp on r.permissioncode=rp.code
                  where r.deleted is not true
                );