-- Seed city_district (public schema) with districts and regional cities of Uzbekistan.
-- Used by the Location form and the AddressModal region->district cascading dropdown.
-- Region rows are matched by uzname (set by 202007200928__Muxriddin__RegionDuplicateIssueAndUzRegion.sql
-- and 20220719521__Sardor__AddressTahkentChange.sql).

-- create table if not exists city_district (
--     id serial primary key,
--     alias varchar(1000),
--     regionid integer,
--     name varchar(255),
--     runame varchar(255),
--     uzname varchar(255),
--     enname varchar(255)
-- );

delete from city_district
where regionid in (select id from region
                   where countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan'));

-- Tashkent city (Toshkent shahri)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Bektemir district', 'Бектемирский район', 'Bektemir tumani', 'Bektemir district'),
        ('Chilanzar district', 'Чиланзарский район', 'Chilonzor tumani', 'Chilanzar district'),
        ('Mirabad district', 'Мирабадский район', 'Mirobod tumani', 'Mirabad district'),
        ('Mirzo Ulugbek district', 'Мирзо-Улугбекский район', 'Mirzo Ulug''bek tumani', 'Mirzo Ulugbek district'),
        ('Olmazor district', 'Алмазарский район', 'Olmazor tumani', 'Olmazor district'),
        ('Sergeli district', 'Сергелийский район', 'Sergeli tumani', 'Sergeli district'),
        ('Shaykhantakhur district', 'Шайхантахурский район', 'Shayxontohur tumani', 'Shaykhantakhur district'),
        ('Uchtepa district', 'Учтепинский район', 'Uchtepa tumani', 'Uchtepa district'),
        ('Yakkasaray district', 'Яккасарайский район', 'Yakkasaroy tumani', 'Yakkasaray district'),
        ('Yangihayot district', 'Янгихаётский район', 'Yangihayot tumani', 'Yangihayot district'),
        ('Yashnabad district', 'Яшнабадский район', 'Yashnobod tumani', 'Yashnabad district'),
        ('Yunusabad district', 'Юнусабадский район', 'Yunusobod tumani', 'Yunusabad district')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Toshkent shahri'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Tashkent region (Toshkent viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Bekabad district', 'Бекабадский район', 'Bekobod tumani', 'Bekabad district'),
        ('Buka district', 'Букинский район', 'Bo''ka tumani', 'Buka district'),
        ('Bostanlik district', 'Бостанлыкский район', 'Bo''stonliq tumani', 'Bostanlik district'),
        ('Chinaz district', 'Чиназский район', 'Chinoz tumani', 'Chinaz district'),
        ('Akhangaran district', 'Ахангаранский район', 'Ohangaron tumani', 'Akhangaran district'),
        ('Akkurgan district', 'Аккурганский район', 'Oqqo''rg''on tumani', 'Akkurgan district'),
        ('Urtachirchik district', 'Уртачирчикский район', 'O''rtachirchiq tumani', 'Urtachirchik district'),
        ('Parkent district', 'Паркентский район', 'Parkent tumani', 'Parkent district'),
        ('Piskent district', 'Пскентский район', 'Piskent tumani', 'Piskent district'),
        ('Kibray district', 'Кибрайский район', 'Qibray tumani', 'Kibray district'),
        ('Kuyichirchik district', 'Куйичирчикский район', 'Quyichirchiq tumani', 'Kuyichirchik district'),
        ('Tashkent district', 'Ташкентский район', 'Toshkent tumani', 'Tashkent district'),
        ('Yangiyul district', 'Янгиюльский район', 'Yangiyo''l tumani', 'Yangiyul district'),
        ('Yukorichirchik district', 'Юкоричирчикский район', 'Yuqorichirchiq tumani', 'Yukorichirchik district'),
        ('Zangiata district', 'Зангиатинский район', 'Zangiota tumani', 'Zangiata district'),
        ('Angren city', 'город Ангрен', 'Angren shahri', 'Angren city'),
        ('Bekabad city', 'город Бекабад', 'Bekobod shahri', 'Bekabad city'),
        ('Chirchik city', 'город Чирчик', 'Chirchiq shahri', 'Chirchik city'),
        ('Nurafshon city', 'город Нурафшан', 'Nurafshon shahri', 'Nurafshon city'),
        ('Akhangaran city', 'город Ахангаран', 'Ohangaron shahri', 'Akhangaran city'),
        ('Almalyk city', 'город Алмалык', 'Olmaliq shahri', 'Almalyk city'),
        ('Yangiyul city', 'город Янгиюль', 'Yangiyo''l shahri', 'Yangiyul city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Toshkent viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Andijan region (Andijon viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Andijan district', 'Андижанский район', 'Andijon tumani', 'Andijan district'),
        ('Asaka district', 'Асакинский район', 'Asaka tumani', 'Asaka district'),
        ('Balikchi district', 'Балыкчинский район', 'Baliqchi tumani', 'Balikchi district'),
        ('Buston district', 'Бустанский район', 'Bo''ston tumani', 'Buston district'),
        ('Bulakbashi district', 'Булакбашинский район', 'Buloqboshi tumani', 'Bulakbashi district'),
        ('Izboskan district', 'Избасканский район', 'Izboskan tumani', 'Izboskan district'),
        ('Jalakuduk district', 'Джалакудукский район', 'Jalaquduq tumani', 'Jalakuduk district'),
        ('Markhamat district', 'Мархаматский район', 'Marhamat tumani', 'Markhamat district'),
        ('Altinkul district', 'Алтынкульский район', 'Oltinko''l tumani', 'Altinkul district'),
        ('Pakhtaabad district', 'Пахтаабадский район', 'Paxtaobod tumani', 'Pakhtaabad district'),
        ('Kurgantepa district', 'Кургантепинский район', 'Qo''rg''ontepa tumani', 'Kurgantepa district'),
        ('Shakhrikhan district', 'Шахриханский район', 'Shahrixon tumani', 'Shakhrikhan district'),
        ('Ulugnor district', 'Улугнорский район', 'Ulug''nor tumani', 'Ulugnor district'),
        ('Khojaabad district', 'Ходжаабадский район', 'Xo''jaobod tumani', 'Khojaabad district'),
        ('Andijan city', 'город Андижан', 'Andijon shahri', 'Andijan city'),
        ('Khanabad city', 'город Ханабад', 'Xonobod shahri', 'Khanabad city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Andijon viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Bukhara region (Buxoro viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Bukhara district', 'Бухарский район', 'Buxoro tumani', 'Bukhara district'),
        ('Gijduvan district', 'Гиждуванский район', 'G''ijduvon tumani', 'Gijduvan district'),
        ('Jondor district', 'Жондорский район', 'Jondor tumani', 'Jondor district'),
        ('Kagan district', 'Каганский район', 'Kogon tumani', 'Kagan district'),
        ('Alat district', 'Алатский район', 'Olot tumani', 'Alat district'),
        ('Peshku district', 'Пешкунский район', 'Peshku tumani', 'Peshku district'),
        ('Karakul district', 'Каракульский район', 'Qorako''l tumani', 'Karakul district'),
        ('Karaulbazar district', 'Караулбазарский район', 'Qorovulbozor tumani', 'Karaulbazar district'),
        ('Romitan district', 'Ромитанский район', 'Romitan tumani', 'Romitan district'),
        ('Shafirkan district', 'Шафирканский район', 'Shofirkon tumani', 'Shafirkan district'),
        ('Vabkent district', 'Вабкентский район', 'Vobkent tumani', 'Vabkent district'),
        ('Bukhara city', 'город Бухара', 'Buxoro shahri', 'Bukhara city'),
        ('Kagan city', 'город Каган', 'Kogon shahri', 'Kagan city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Buxoro viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Jizzakh region (Jizzax viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Arnasay district', 'Арнасайский район', 'Arnasoy tumani', 'Arnasay district'),
        ('Bakhmal district', 'Бахмальский район', 'Baxmal tumani', 'Bakhmal district'),
        ('Dustlik district', 'Дустликский район', 'Do''stlik tumani', 'Dustlik district'),
        ('Farish district', 'Фаришский район', 'Forish tumani', 'Farish district'),
        ('Gallaaral district', 'Галляаральский район', 'G''allaorol tumani', 'Gallaaral district'),
        ('Mirzachul district', 'Мирзачульский район', 'Mirzacho''l tumani', 'Mirzachul district'),
        ('Pakhtakor district', 'Пахтакорский район', 'Paxtakor tumani', 'Pakhtakor district'),
        ('Sharof Rashidov district', 'Шараф-Рашидовский район', 'Sharof Rashidov tumani', 'Sharof Rashidov district'),
        ('Yangiabad district', 'Янгиабадский район', 'Yangiobod tumani', 'Yangiabad district'),
        ('Zafarabad district', 'Зафарабадский район', 'Zafarobod tumani', 'Zafarabad district'),
        ('Zarbdor district', 'Зарбдорский район', 'Zarbdor tumani', 'Zarbdor district'),
        ('Zaamin district', 'Зааминский район', 'Zomin tumani', 'Zaamin district'),
        ('Jizzakh city', 'город Джизак', 'Jizzax shahri', 'Jizzakh city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Jizzax viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Fergana region (Farg'ona viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Besharik district', 'Бешарыкский район', 'Beshariq tumani', 'Besharik district'),
        ('Bagdad district', 'Багдадский район', 'Bog''dod tumani', 'Bagdad district'),
        ('Buvayda district', 'Бувайдинский район', 'Buvayda tumani', 'Buvayda district'),
        ('Dangara district', 'Дангаринский район', 'Dang''ara tumani', 'Dangara district'),
        ('Fergana district', 'Ферганский район', 'Farg''ona tumani', 'Fergana district'),
        ('Furkat district', 'Фуркатский район', 'Furqat tumani', 'Furkat district'),
        ('Altiarik district', 'Алтыарыкский район', 'Oltiariq tumani', 'Altiarik district'),
        ('Uzbekistan district', 'Узбекистанский район', 'O''zbekiston tumani', 'Uzbekistan district'),
        ('Kushtepa district', 'Куштепинский район', 'Qo''shtepa tumani', 'Kushtepa district'),
        ('Kuva district', 'Кувинский район', 'Quva tumani', 'Kuva district'),
        ('Rishtan district', 'Риштанский район', 'Rishton tumani', 'Rishtan district'),
        ('Sokh district', 'Сохский район', 'So''x tumani', 'Sokh district'),
        ('Tashlak district', 'Ташлакский район', 'Toshloq tumani', 'Tashlak district'),
        ('Uchkuprik district', 'Учкуприкский район', 'Uchko''prik tumani', 'Uchkuprik district'),
        ('Yazyavan district', 'Язъяванский район', 'Yozyovon tumani', 'Yazyavan district'),
        ('Fergana city', 'город Фергана', 'Farg''ona shahri', 'Fergana city'),
        ('Margilan city', 'город Маргилан', 'Marg''ilon shahri', 'Margilan city'),
        ('Kokand city', 'город Коканд', 'Qo''qon shahri', 'Kokand city'),
        ('Kuvasay city', 'город Кувасай', 'Quvasoy shahri', 'Kuvasay city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Farg''ona viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Kashkadarya region (Qashqadaryo viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Chirakchi district', 'Чиракчинский район', 'Chiroqchi tumani', 'Chirakchi district'),
        ('Dekhkanabad district', 'Дехканабадский район', 'Dehqonobod tumani', 'Dekhkanabad district'),
        ('Guzar district', 'Гузарский район', 'G''uzor tumani', 'Guzar district'),
        ('Kasbi district', 'Касбийский район', 'Kasbi tumani', 'Kasbi district'),
        ('Kitab district', 'Китабский район', 'Kitob tumani', 'Kitab district'),
        ('Kasan district', 'Касанский район', 'Koson tumani', 'Kasan district'),
        ('Kukdala district', 'Кокдалинский район', 'Ko''kdala tumani', 'Kukdala district'),
        ('Mirishkor district', 'Миришкорский район', 'Mirishkor tumani', 'Mirishkor district'),
        ('Mubarek district', 'Мубарекский район', 'Muborak tumani', 'Mubarek district'),
        ('Nishan district', 'Нишанский район', 'Nishon tumani', 'Nishan district'),
        ('Kamashi district', 'Камашинский район', 'Qamashi tumani', 'Kamashi district'),
        ('Karshi district', 'Каршинский район', 'Qarshi tumani', 'Karshi district'),
        ('Shakhrisabz district', 'Шахрисабзский район', 'Shahrisabz tumani', 'Shakhrisabz district'),
        ('Yakkabag district', 'Яккабагский район', 'Yakkabog'' tumani', 'Yakkabag district'),
        ('Karshi city', 'город Карши', 'Qarshi shahri', 'Karshi city'),
        ('Shakhrisabz city', 'город Шахрисабз', 'Shahrisabz shahri', 'Shakhrisabz city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Qashqadaryo viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Khorezm region (Xorazm viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Bagat district', 'Багатский район', 'Bog''ot tumani', 'Bagat district'),
        ('Gurlan district', 'Гурленский район', 'Gurlan tumani', 'Gurlan district'),
        ('Khazarasp district', 'Хазараспский район', 'Hazorasp tumani', 'Khazarasp district'),
        ('Koshkupir district', 'Кошкупырский район', 'Qo''shko''pir tumani', 'Koshkupir district'),
        ('Shavat district', 'Шаватский район', 'Shovot tumani', 'Shavat district'),
        ('Tuprakkala district', 'Тупраккалинский район', 'Tuproqqal''a tumani', 'Tuprakkala district'),
        ('Urgench district', 'Ургенчский район', 'Urganch tumani', 'Urgench district'),
        ('Khiva district', 'Хивинский район', 'Xiva tumani', 'Khiva district'),
        ('Khanka district', 'Ханкинский район', 'Xonqa tumani', 'Khanka district'),
        ('Yangiarik district', 'Янгиарыкский район', 'Yangiariq tumani', 'Yangiarik district'),
        ('Yangibazar district', 'Янгибазарский район', 'Yangibozor tumani', 'Yangibazar district'),
        ('Urgench city', 'город Ургенч', 'Urganch shahri', 'Urgench city'),
        ('Khiva city', 'город Хива', 'Xiva shahri', 'Khiva city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Xorazm viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Namangan region (Namangan viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Chartak district', 'Чартакский район', 'Chortoq tumani', 'Chartak district'),
        ('Chust district', 'Чустский район', 'Chust tumani', 'Chust district'),
        ('Kasansay district', 'Касансайский район', 'Kosonsoy tumani', 'Kasansay district'),
        ('Mingbulak district', 'Мингбулакский район', 'Mingbuloq tumani', 'Mingbulak district'),
        ('Namangan district', 'Наманганский район', 'Namangan tumani', 'Namangan district'),
        ('Narin district', 'Нарынский район', 'Norin tumani', 'Narin district'),
        ('Pap district', 'Папский район', 'Pop tumani', 'Pap district'),
        ('Turakurgan district', 'Туракурганский район', 'To''raqo''rg''on tumani', 'Turakurgan district'),
        ('Uchkurgan district', 'Учкурганский район', 'Uchqo''rg''on tumani', 'Uchkurgan district'),
        ('Uychi district', 'Уйчинский район', 'Uychi tumani', 'Uychi district'),
        ('Yangikurgan district', 'Янгикурганский район', 'Yangiqo''rg''on tumani', 'Yangikurgan district'),
        ('Namangan city', 'город Наманган', 'Namangan shahri', 'Namangan city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Namangan viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Navoi region (Navoiy viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Karmana district', 'Карманинский район', 'Karmana tumani', 'Karmana district'),
        ('Kanimekh district', 'Канимехский район', 'Konimex tumani', 'Kanimekh district'),
        ('Navbahor district', 'Навбахорский район', 'Navbahor tumani', 'Navbahor district'),
        ('Nurata district', 'Нуратинский район', 'Nurota tumani', 'Nurata district'),
        ('Kiziltepa district', 'Кызылтепинский район', 'Qiziltepa tumani', 'Kiziltepa district'),
        ('Tamdi district', 'Тамдынский район', 'Tomdi tumani', 'Tamdi district'),
        ('Uchkuduk district', 'Учкудукский район', 'Uchquduq tumani', 'Uchkuduk district'),
        ('Khatirchi district', 'Хатырчинский район', 'Xatirchi tumani', 'Khatirchi district'),
        ('Navoi city', 'город Навои', 'Navoiy shahri', 'Navoi city'),
        ('Zarafshan city', 'город Зарафшан', 'Zarafshon shahri', 'Zarafshan city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Navoiy viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Samarkand region (Samarqand viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Bulungur district', 'Булунгурский район', 'Bulung''ur tumani', 'Bulungur district'),
        ('Ishtikhan district', 'Иштыханский район', 'Ishtixon tumani', 'Ishtikhan district'),
        ('Jambay district', 'Джамбайский район', 'Jomboy tumani', 'Jambay district'),
        ('Kattakurgan district', 'Каттакурганский район', 'Kattaqo''rg''on tumani', 'Kattakurgan district'),
        ('Narpay district', 'Нарпайский район', 'Narpay tumani', 'Narpay district'),
        ('Nurabad district', 'Нурабадский район', 'Nurobod tumani', 'Nurabad district'),
        ('Akdarya district', 'Акдарьинский район', 'Oqdaryo tumani', 'Akdarya district'),
        ('Pastdargom district', 'Пастдаргомский район', 'Pastdarg''om tumani', 'Pastdargom district'),
        ('Pakhtachi district', 'Пахтачийский район', 'Paxtachi tumani', 'Pakhtachi district'),
        ('Payarik district', 'Пайарыкский район', 'Payariq tumani', 'Payarik district'),
        ('Koshrabad district', 'Кошрабадский район', 'Qo''shrabot tumani', 'Koshrabad district'),
        ('Samarkand district', 'Самаркандский район', 'Samarqand tumani', 'Samarkand district'),
        ('Taylak district', 'Тайлакский район', 'Toyloq tumani', 'Taylak district'),
        ('Urgut district', 'Ургутский район', 'Urgut tumani', 'Urgut district'),
        ('Kattakurgan city', 'город Каттакурган', 'Kattaqo''rg''on shahri', 'Kattakurgan city'),
        ('Samarkand city', 'город Самарканд', 'Samarqand shahri', 'Samarkand city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Samarqand viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Surkhandarya region (Surxondaryo viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Angor district', 'Ангорский район', 'Angor tumani', 'Angor district'),
        ('Bandikhan district', 'Бандиханский район', 'Bandixon tumani', 'Bandikhan district'),
        ('Baysun district', 'Байсунский район', 'Boysun tumani', 'Baysun district'),
        ('Denau district', 'Денауский район', 'Denov tumani', 'Denau district'),
        ('Jarkurgan district', 'Джаркурганский район', 'Jarqo''rg''on tumani', 'Jarkurgan district'),
        ('Muzrabad district', 'Музрабадский район', 'Muzrabot tumani', 'Muzrabad district'),
        ('Altinsay district', 'Алтынсайский район', 'Oltinsoy tumani', 'Altinsay district'),
        ('Kizirik district', 'Кизирикский район', 'Qiziriq tumani', 'Kizirik district'),
        ('Kumkurgan district', 'Кумкурганский район', 'Qumqo''rg''on tumani', 'Kumkurgan district'),
        ('Sariasiya district', 'Сариасийский район', 'Sariosiyo tumani', 'Sariasiya district'),
        ('Sherabad district', 'Шерабадский район', 'Sherobod tumani', 'Sherabad district'),
        ('Shurchi district', 'Шурчинский район', 'Sho''rchi tumani', 'Shurchi district'),
        ('Termez district', 'Термезский район', 'Termiz tumani', 'Termez district'),
        ('Uzun district', 'Узунский район', 'Uzun tumani', 'Uzun district'),
        ('Termez city', 'город Термез', 'Termiz shahri', 'Termez city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Surxondaryo viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Syrdarya region (Sirdaryo viloyati)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Bayaut district', 'Баяутский район', 'Boyovut tumani', 'Bayaut district'),
        ('Gulistan district', 'Гулистанский район', 'Guliston tumani', 'Gulistan district'),
        ('Mirzaabad district', 'Мирзаабадский район', 'Mirzaobod tumani', 'Mirzaabad district'),
        ('Akaltin district', 'Акалтынский район', 'Oqoltin tumani', 'Akaltin district'),
        ('Sardoba district', 'Сардобинский район', 'Sardoba tumani', 'Sardoba district'),
        ('Saykhunabad district', 'Сайхунабадский район', 'Sayxunobod tumani', 'Saykhunabad district'),
        ('Sirdarya district', 'Сырдарьинский район', 'Sirdaryo tumani', 'Sirdarya district'),
        ('Khavast district', 'Хавастский район', 'Xovos tumani', 'Khavast district'),
        ('Gulistan city', 'город Гулистан', 'Guliston shahri', 'Gulistan city'),
        ('Shirin city', 'город Ширин', 'Shirin shahri', 'Shirin city'),
        ('Yangiyer city', 'город Янгиер', 'Yangiyer shahri', 'Yangiyer city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Sirdaryo viloyati'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');

-- Republic of Karakalpakstan (Qoraqalpog'iston Respublikasi)
insert into city_district (regionid, name, runame, uzname, enname)
select r.id, v.name, v.runame, v.uzname, v.enname
from region r,
     (values
        ('Amudarya district', 'Амударьинский район', 'Amudaryo tumani', 'Amudarya district'),
        ('Beruniy district', 'Берунийский район', 'Beruniy tumani', 'Beruniy district'),
        ('Bozatau district', 'Бозатауский район', 'Bo''zatov tumani', 'Bozatau district'),
        ('Chimbay district', 'Чимбайский район', 'Chimboy tumani', 'Chimbay district'),
        ('Ellikkala district', 'Элликкалинский район', 'Ellikqal''a tumani', 'Ellikkala district'),
        ('Kegeyli district', 'Кегейлийский район', 'Kegeyli tumani', 'Kegeyli district'),
        ('Muynak district', 'Муйнакский район', 'Mo''ynoq tumani', 'Muynak district'),
        ('Nukus district', 'Нукусский район', 'Nukus tumani', 'Nukus district'),
        ('Kanlikul district', 'Канлыкульский район', 'Qanliko''l tumani', 'Kanlikul district'),
        ('Karauzyak district', 'Караузякский район', 'Qorao''zak tumani', 'Karauzyak district'),
        ('Kungrad district', 'Кунградский район', 'Qo''ng''irot tumani', 'Kungrad district'),
        ('Shumanay district', 'Шуманайский район', 'Shumanay tumani', 'Shumanay district'),
        ('Takhiatash district', 'Тахиаташский район', 'Taxiatosh tumani', 'Takhiatash district'),
        ('Takhtakupir district', 'Тахтакупырский район', 'Taxtako''pir tumani', 'Takhtakupir district'),
        ('Turtkul district', 'Турткульский район', 'To''rtko''l tumani', 'Turtkul district'),
        ('Khojayli district', 'Ходжейлийский район', 'Xo''jayli tumani', 'Khojayli district'),
        ('Nukus city', 'город Нукус', 'Nukus shahri', 'Nukus city')
     ) as v(name, runame, uzname, enname)
where r.uzname = 'Qoraqalpog''iston Respublikasi'
  and r.countryid = (select id from country where code = 'UZ' and name = 'Uzbekistan');
