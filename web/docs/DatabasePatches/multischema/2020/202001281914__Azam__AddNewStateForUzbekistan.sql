insert into region (name, countryid) values
('Andijan', (select id from country where code='UZ' and name='Uzbekistan')),
('Bukhara', (select id from country where code='UZ' and name='Uzbekistan')),
('Djizzak', (select id from country where code='UZ' and name='Uzbekistan')),
('Fergana', (select id from country where code='UZ' and name='Uzbekistan')),
('Kashkadarya', (select id from country where code='UZ' and name='Uzbekistan')),
('Khorezm', (select id from country where code='UZ' and name='Uzbekistan')),
('Namangan', (select id from country where code='UZ' and name='Uzbekistan')),
('Navoi', (select id from country where code='UZ' and name='Uzbekistan')),
('Samarkand', (select id from country where code='UZ' and name='Uzbekistan')),
('Surkhandarya', (select id from country where code='UZ' and name='Uzbekistan')),
('Syrdarya', (select id from country where code='UZ' and name='Uzbekistan')),
('Tashkent', (select id from country where code='UZ' and name='Uzbekistan')),
('Republic of Karakalpakistan', (select id from country where code='UZ' and name='Uzbekistan'));