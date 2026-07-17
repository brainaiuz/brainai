INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid)
VALUES (false, '_COMPANY_INDUSTRY', false, '', true, false, false, true, 'Industry', true, null,null);


INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Retail', 'Chakana savdo','Розничная торговля');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Healthcare / Medicine', 'Sog''liqni saqlash / Tibbiyot', 'Здравоохранение / Медецина');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('IT/Computer Science', 'IT / Axborot texnologiyalari', 'Информационные технологии');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Online store', 'Onlayn do''kon', 'Интернет магазин');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Educational center / School', 'Ta''lim markazi / Maktab', 'Обучающий центр / Школа');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Wholesale', 'Ulgurji savdo', 'Оптовая торговля');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Construction of large facilities', 'Katta inshootlar qurilishi', 'Строительство крупных объектов');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Construction and repair', 'Qurilish va ta''mirlash', 'Стоительство и ремонтная');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Manufacturing', 'Ishlab chiqarish', 'Производство');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Real estate agency', 'Ko''chmas mulk agentligi', 'Агенство недвижимости');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Hospitality / Hotel', 'Mehmondo''stlik / Mehmonxona', 'Гостеприимство / Гостиничный бизнес');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Restaurant / Café', 'Restoran / Kafe', 'Ресторан / Кафе');

INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Others', 'Boshqa', 'Другие');


INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'RETAIL_INDUSTRY', false, '', true, false, false, true, 'Retail', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Retail' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'HEALTHCARE_MEDICINE_INDUSTRY', false, '', true, false, false, true, 'Healthcare / Medicine', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Healthcare / Medicine' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'IT_COMPUTER_SCIENCE_INDUSTRY', false, '', true, false, false, true, 'IT/Computer Science', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='IT/Computer Science' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'ONLINE_STORE_INDUSTRY', false, '', true, false, false, true, 'Online store', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Online store' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'EDUCATIONAL_CENTER_SCHOOL_INDUSTRY', false, '', true, false, false, true, 'Educational center / School', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Educational center / School' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'WHOLESALE_INDUSTRY', false, '', true, false, false, true, 'Wholesale', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Wholesale' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'CONSTRUCTION_LARGE_FACILITIES_INDUSTRY', false, '', true, false, false, true, 'Construction of large facilities', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Construction of large facilities' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'CONSTRUCTION_AND_REPAIR_INDUSTRY', false, '', true, false, false, true, 'Construction and repair', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Construction and repair' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'MANUFACTURING_INDUSTRY', false, '', true, false, false, true, 'Manufacturing', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Manufacturing' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'REAL_ESTATE_AGENCY_INDUSTRY', false, '', true, false, false, true, 'Real estate agency', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Real estate agency' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'HOSPITALITY_HOTEL_INDUSTRY', false, '', true, false, false, true, 'Hospitality / Hotel', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Hospitality / Hotel' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'RESTAURANT_CAFE_INDUSTRY', false, '', true, false, false, true, 'Restaurant / Café', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Restaurant / Café' limit 1));

INSERT INTO "anv".reference (autoapprove, code, deleted, description, isactive, iscustombutton, isremovable, issystemreference, name, shared, shortname, parentid, localeid)
VALUES (false, 'OTHERS_INDUSTRY', false, '', true, false, false, true, 'Others', true, null,
        (select id from "anv".reference where code ='_COMPANY_INDUSTRY' limit 1),
        (select id from "anv".reference_locale where english ='Others' limit 1));