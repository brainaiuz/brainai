delete from "anv".reference_locale  where  arabic = 'تم الموافقة' and english = 'Approved' and russian ='Одобрено' and uzbek = 'Maqullandi';

insert into "anv".reference_locale(arabic, english, russian, uzbek) values('تم الموافقة', 'Approved', 'Одобрено', 'Maqullandi');
update "anv".reference set localeid = (select id from "anv".reference_locale where  arabic = 'تم الموافقة' and english = 'Approved' and russian ='Одобрено' and uzbek = 'Maqullandi')
where code = 'PLACEMENT_STATUS_APPROVED';

delete from "anv".reference_locale  where  arabic = 'مشروع' and english = 'Draft' and russian ='Черновик' and uzbek = 'Qoralama';

insert into "anv".reference_locale(arabic, english, russian, uzbek) values('مشروع', 'Draft', 'Черновик', 'Qoralama');
update "anv".reference set localeid = (select id from "anv".reference_locale where  arabic = 'مشروع' and english = 'Draft' and russian ='Черновик' and uzbek = 'Qoralama')
where code = 'PLACEMENT_STATUS_SAVE_AS_DRAFT';

delete from "anv".reference_locale  where  arabic = 'تم توظيفه' and english = 'Hired' and russian ='Наемный' and uzbek = 'Yollangan';

insert into "anv".reference_locale(arabic, english, russian, uzbek) values('تم توظيفه', 'Hired', 'Наемный', 'Yollangan');
update "anv".reference set localeid = (select id from "anv".reference_locale where  arabic = 'تم توظيفه' and english = 'Hired' and russian ='Наемный' and uzbek = 'Yollangan')
where code = 'PLACEMENT_STATUS_HIRED';
