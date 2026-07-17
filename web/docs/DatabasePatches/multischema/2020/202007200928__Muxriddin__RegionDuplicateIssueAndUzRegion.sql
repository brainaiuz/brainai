delete from region where countryid =  (select id from country where code='UZ' and name='Uzbekistan');

insert into region (name, countryid, runame, uzname) values
('Andijan', (select id from country where code='UZ' and name='Uzbekistan'), 'Андижан', 'Andijon viloyati'),
('Bukhara', (select id from country where code='UZ' and name='Uzbekistan'), 'Бухара', 'Buxoro viloyati'),
('Djizzak', (select id from country where code='UZ' and name='Uzbekistan'), 'Джизак', 'Jizzax viloyati'),
('Fergana', (select id from country where code='UZ' and name='Uzbekistan'), 'Фергана', 'Farg''ona viloyati'),
('Kashkadarya', (select id from country where code='UZ' and name='Uzbekistan'), 'Кашкадарья', 'Qashqadaryo viloyati'),
('Khorezm', (select id from country where code='UZ' and name='Uzbekistan'), 'Хорезм', 'Xorazm viloyati'),
('Namangan', (select id from country where code='UZ' and name='Uzbekistan'), 'Наманган', 'Namangan viloyati'),
('Navoi', (select id from country where code='UZ' and name='Uzbekistan'), 'Навои', 'Navoiy viloyati'),
('Samarkand', (select id from country where code='UZ' and name='Uzbekistan'), 'Самарканд', 'Samarqand viloyati'),
('Surkhandarya', (select id from country where code='UZ' and name='Uzbekistan'), 'Сурхандарья', 'Surxondaryo viloyati'),
('Syrdarya', (select id from country where code='UZ' and name='Uzbekistan'), 'Сырдарья', 'Sirdaryo viloyati'),
('Tashkent', (select id from country where code='UZ' and name='Uzbekistan'), 'Ташкент', 'Toshkent viloyati'),
('Republic of Karakalpakistan', (select id from country where code='UZ' and name='Uzbekistan'), 'Республика Каракалпакистан', 'Qoraqalpog''iston Respublikasi');