insert into "0".modelfield
(form_id,       fsection,                columntype, mandatory,         forder,       field_id,hide) values
    ('PRODUCT',  'PRODUCT_INF
 ORMATION',  'COL_1',    false,               5,            'STATUS',true);


insert into "anv".modelfield
(form_id,       fsection,                columntype, mandatory,         forder,       field_id,hide) values
    ('PRODUCT',  'PRODUCT_INF
 ORMATION',  'COL_1',    false,               5,            'STATUS',true);


insert into "0".modelfield
(form_id,       fsection,                columntype, mandatory,         forder,       field_id,hide) values
    ('PRODUCT',  'PRODUCT_INFORMATION',  'COL_1',  false,    6, 'RENT_ITEM',true);

insert into "anv".modelfield
(form_id,       fsection,                columntype, mandatory,         forder,       field_id,hide) values
    ('PRODUCT',  'PRODUCT_INFORMATION',  'COL_1',  false,    6, 'RENT_ITEM',true);




insert into "0".reference_locale(arabic, english, russian, uzbek) values('مشغول', 'Occupied', 'Занято', 'Band');
insert into "0".reference_locale(arabic, english, russian, uzbek) values('متوفر', 'Available', 'Свободно', 'Mavjud');



INSERT INTO "0".reference (code, name) VALUES ('RENT_ITEM_STATUS', 'Rent Item Status');

INSERT INTO "0".reference (code, name, parentid, sorder,description,localeid) VALUES ('AVAILABLE', 'Available', (SELECT id  from "0".reference  WHERE code = 'RENT_ITEM_STATUS'), 0,'100',
                                                                                          (select id from "0".reference_locale where  arabic = 'متوفر' and english = 'Available' and russian ='Свободно' and uzbek = 'Mavjud') );

INSERT INTO "0".reference (code, name, parentid, sorder,description,localeid) VALUES ('OCCUPIED', 'Occupied', (SELECT id from "0".reference  WHERE code = 'RENT_ITEM_STATUS'), 1,'0',
                                                                                          (select id from "0".reference_locale where  arabic = 'مشغول' and english = 'Occupied' and russian ='Занято' and uzbek = 'Band') );





insert into "anv".reference_locale(arabic, english, russian, uzbek) values('مشغول', 'Occupied', 'Занято', 'Band');
insert into "anv".reference_locale(arabic, english, russian, uzbek) values('متوفر', 'Available', 'Свободно', 'Mavjud');



INSERT INTO "anv".reference (code, name) VALUES ('RENT_ITEM_STATUS', 'Rent Item Status');

INSERT INTO "anv".reference (code, name, parentid, sorder,description,localeid) VALUES ('AVAILABLE', 'Available', (SELECT id  from "anv".reference  WHERE code = 'RENT_ITEM_STATUS'), 0,'100',
                                                                                      (select id from "anv".reference_locale where  arabic = 'متوفر' and english = 'Available' and russian ='Свободно' and uzbek = 'Mavjud') );

INSERT INTO "anv".reference (code, name, parentid, sorder,description,localeid) VALUES ('OCCUPIED', 'Occupied', (SELECT id from "anv".reference  WHERE code = 'RENT_ITEM_STATUS'), 1,'0',
                                                                                      (select id from "anv".reference_locale where  arabic = 'مشغول' and english = 'Occupied' and russian ='Занято' and uzbek = 'Band') );



