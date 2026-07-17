insert into context (code) select 'WORKSPACE' from permission where not exists (select id from context where code = 'WORKSPACE') limit 1;
insert into context (code) select 'REPORTING' from permission where not exists (select id from context where code = 'REPORTING') limit 1;
insert into context (code) select 'PM' from permission where not exists (select id from context where code = 'PM') limit 1;
insert into context (code) select 'CRM' from permission where not exists (select id from context where code = 'CRM') limit 1;
insert into context (code) select 'DASHBOARD' from permission where not exists (select id from context where code = 'DASHBOARD') limit 1;
insert into context (code) select 'PAYROLL' from permission where not exists (select id from context where code = 'PAYROLL') limit 1;
insert into context (code) select 'DOCUMENTS' from permission where not exists (select id from context where code = 'DOCUMENTS') limit 1;
insert into context (code) select 'ACCOUNTING' from permission where not exists (select id from context where code = 'ACCOUNTING') limit 1;
insert into context (code) select 'SETTINGS' from permission where not exists (select id from context where code = 'SETTINGS') limit 1;
insert into context (code) select 'TRAININGCENTER' from permission where not exists (select id from context where code = 'TRAININGCENTER') limit 1;
insert into context (code) select 'HRMS' from permission where not exists (select id from context where code = 'HRMS') limit 1;
insert into context (code) select 'MYACCOUNT' from permission where not exists (select id from context where code = 'MYACCOUNT') limit 1;
insert into context (code) select 'LOGISTICS' from permission where not exists (select id from context where code = 'LOGISTICS') limit 1;

--/////////////////////////////////////////////
-- drop qilgandan kiyin schema update qilish kerak
-- permission_context yangidan yaratish kerak
drop table "anv".permission_context;
drop table if exists "anv".permission_contexts;

drop table "0".permission_context;
drop table if exists "0".permission_contexts;
--/////////////////////////////////////////////////////



insert into "anv".permission_context (permissioncode, contextcode) select p.code, (select code from public.context co2 where co2.code = p.context)
                                                                    from permission p
                                                                      where (select code from context co2 where co2.code = p.context) is not NULL
                                                                      and not exists (select permissioncode from "anv".permission_context pc
                                                                                        where pc.permissioncode = p.code
                                                                                        and pc.contextcode = (select code from public.context co2 where co2.code = p.context));



insert into "0".permission_context (permissioncode, contextcode) select p.code, (select code from public.context co2 where co2.code = p.context)
                                                                    from permission p
                                                                      where (select code from context co2 where co2.code = p.context) is not NULL
                                                                      and not exists (select permissioncode from "0".permission_context pc
                                                                                        where pc.permissioncode = p.code
                                                                                        and pc.contextcode = (select code from public.context co2 where co2.code = p.context));
