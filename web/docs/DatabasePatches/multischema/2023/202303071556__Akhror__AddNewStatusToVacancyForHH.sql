insert into "anv".reference(code, isactive, issystemreference, name, sorder, parentid)
select 'PUBLISHED',
       true,
       true,
       'Published',
       80,
       (select id from "anv".reference where code = 'VACANCY_STATUSES' limit 1)
where not exists (select id from "anv".reference where code = 'PUBLISHED'
  and parentid = (select id from "anv".reference where code = 'VACANCY_STATUSES' limit 1));