update Region
set name='Tashkent region',
    runame='Ташкентская область'
where name = 'Tashkent'
  and uzname = 'Toshkent viloyati';

update Region
set name='Tashkent city',
    runame='Город Ташкент'
where name = 'Tashkent'
  and uzname = 'Toshkent shahri';