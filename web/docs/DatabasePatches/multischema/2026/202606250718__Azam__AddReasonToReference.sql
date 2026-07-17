insert into "anv".reference_locale (english, russian, uzbek, arabic)
select 'Other', 'Другое', 'Boshqa', 'أخرى'
where not exists (
    select 1 from "anv".reference_locale
    where english = 'Other' and uzbek = 'Boshqa'
);

insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name,
                              parentid, sorder, requiredComment, localeid)
select 'другое', 'ДРУГОЕ', false, true, true, 'другое',
       (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON'),
       1, true,
       (select id from "anv".reference_locale where english = 'Other' and uzbek = 'Boshqa')
where not exists (
    select 1 from "anv".reference
    where code = 'ДРУГОЕ'
      and parentid = (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON')
);