delete from "anv".reference_locale  where  arabic = 'في تقدم' and russian ='В ходе выполнения' and uzbek = 'Jarayonda';

insert into "anv".reference_locale(arabic, russian, uzbek) values('في تقدم', 'В ходе выполнения', 'Jarayonda');
update "anv".reference set localeid = (select id from "anv".reference_locale where arabic = 'في تقدم' and russian ='В ходе выполнения' and uzbek = 'Jarayonda')
where code = 'VS_IN_PROGRESS';

delete from "anv".reference_locale where arabic = 'افتح' and russian ='Открыто' and uzbek = 'Ochiq';

insert into "anv".reference_locale(arabic, russian, uzbek) values('افتح', 'Открыто', 'Ochiq');
update "anv".reference set localeid = (select id from "anv".reference_locale where arabic = 'افتح' and russian ='Открыто' and uzbek = 'Ochiq')
where code = 'VS_OPEN';

delete from "anv".reference_locale where arabic = 'مملوء' and russian ='Заполненный' and uzbek = 'Toldirilgan';

insert into "anv".reference_locale(arabic, russian, uzbek) values('مملوء', 'Заполненный', 'Toldirilgan');
update "anv".reference set localeid = (select id from "anv".reference_locale where arabic = 'مملوء' and russian ='Заполненный' and uzbek = 'Toldirilgan')
where code = 'VS_FILLED';

delete from "anv".reference_locale where arabic = 'رفض' and russian = 'Отклоненный' and uzbek = 'Rad Etilgan';

insert into "anv".reference_locale(arabic, russian, uzbek) values('رفض', 'Отклоненный', 'Rad Etilgan');
update "anv".reference set localeid = (select id from "anv".reference_locale where arabic = 'رفض' and russian = 'Отклоненный' and uzbek = 'Rad Etilgan')
where code = 'VS_DECLINED';

delete from "anv".reference_locale where arabic = 'ألغيت' and russian = 'Отменен' and uzbek = 'Bekor qilindi';

insert into "anv".reference_locale(arabic, russian, uzbek) values('ألغيت', 'Отменен', 'Bekor qilindi');
update "anv".reference set localeid = (select id from "anv".reference_locale where arabic = 'ألغيت' and russian = 'Отменен' and uzbek = 'Bekor qilindi')
where code = 'VS_CANCELLED';

delete from "anv".reference_locale where arabic = 'مملوء جزئيا' and russian = 'Частично заполнен' and uzbek = 'Qisman toldirilgan';

insert into "anv".reference_locale(arabic, russian, uzbek) values('مملوء جزئيا', 'Частично заполнен', 'Qisman toldirilgan');
update "anv".reference set localeid = (select id from "anv".reference_locale where arabic = 'مملوء جزئيا' and russian = 'Частично заполнен' and uzbek = 'Qisman toldirilgan')
where code = 'VS_PARTIALLY_FILLED';

delete from "anv".reference_locale where arabic = 'في الانتظار' and russian = 'На удерживании' and uzbek = 'Ushlab qolingan';

insert into "anv".reference_locale(arabic, russian, uzbek) values('في الانتظار', 'На удерживании', 'Ushlab qolingan');
update "anv".reference set localeid = (select id from "anv".reference_locale where arabic = 'في الانتظار' and russian = 'На удерживании' and uzbek = 'Ushlab qolingan')
where code = 'VS_ON_HOLD';

