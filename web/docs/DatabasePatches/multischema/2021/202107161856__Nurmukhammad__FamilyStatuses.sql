delete from "anv".reference_locale  where  arabic = 'متزوج' and english = 'Married' and russian ='Замужем/женат' and uzbek = 'Oilali';

insert into "anv".reference_locale(arabic, english, russian, uzbek) values('متزوج', 'Married', 'Замужем/женат', 'Oilali');
update "anv".reference set localeid = (select id from "anv".reference_locale where  arabic = 'متزوج' and english = 'Married' and russian ='Замужем/женат' and uzbek = 'Oilali')
where code = 'MARRIED';

delete from "anv".reference_locale  where  arabic = 'غير متزوج' and english = 'Single' and russian ='Не замужем/не женат' and uzbek = 'Turmush qurmagan';

insert into "anv".reference_locale(arabic, english, russian, uzbek) values('غير متزوج', 'Single', 'Не замужем/не женат', 'Turmush qurmagan');
update "anv".reference set localeid = (select id from "anv".reference_locale where  arabic = 'غير متزوج' and english = 'Single' and russian ='Не замужем/не женат' and uzbek = 'Turmush qurmagan')
where code = 'SINGLE';