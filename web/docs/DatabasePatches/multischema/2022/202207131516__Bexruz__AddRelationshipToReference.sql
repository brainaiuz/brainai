insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder, parentid, iscustombutton, isactive)
values('RELATIONSHIP', false, 'Relationship', 'Relationship', false, false, true, 0, null, false, true);

insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder, parentid, iscustombutton, isactive) values
('AUNT', false, 'Aunt', 'Aunt', false, false, true, 1,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('BROTHER', false, 'Brother', 'Brother', false, false, true, 2,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('COUSIN', false, 'Cousin', 'Cousin', false, false, true, 3,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('DAUGHTER', false, 'Daughter', 'Daughter', false, false, true, 4,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('EX_HUSBAND', false, 'Ex-husband', 'Ex-husband', false, false, true, 5,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('EX_WIFE', false, 'Ex-wife', 'Ex-wife', false, false, true, 6,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('FATHER', false, 'Father', 'Father', false, false, true, 7,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('HUSBAND', false, 'Husband', 'Husband', false, false, true, 8,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('MOTHER', false, 'Mother', 'Mother', false, false, true, 9,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('SISTER', false, 'Sister', 'Sister', false, false, true, 10,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('SON', false, 'Son', 'Son', false, false, true, 11,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('STEPSON', false, 'Stepson', 'Stepson', false, false, true, 12,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('STEPDAUGHTER', false, 'Stepdaughter', 'Stepdaughter', false, false, true, 13,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('STEPFATHER', false, 'Stepfather', 'Stepfather', false, false, true, 14,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('STEPMOTHER', false, 'Stepmother', 'Stepmother', false, false, true, 15,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('UNCLE', false, 'Uncle', 'Uncle', false, false, true, 16,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('WIFE', false, 'Wife', 'Wife', false, false, true, 17,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('MOTHER_IN_LAW', false, 'Mother In Law', 'Mother In Law', false, false, true, 18,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('FATHER_IN_LAW', false, 'Father In Law', 'Father In Law', false, false, true, 19,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('DAUGHTER_IN_LAW', false, 'Daughter In Law', 'Daughter In Law', false, false, true, 20,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('NEPHEW', false, 'Nephew', 'Nephew', false, false, true, 21,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('GRANDMOTHER', false, 'Grandmother', 'Grandmother', false, false, true, 22,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('GRANDFATHER', false, 'Grandfather', 'Grandfather', false, false, true, 23,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true),
('OTHER_RELATIVE', false, 'Other relative', 'Other relative', false, false, true, 24,
(select r.id from "anv".reference r where r.code = 'RELATIONSHIP' order by r.id desc limit 1), false, true);

INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Aunt', 'Xola','Тетя','العمة /الخالة');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Brother', 'Aka-uka','Брат','الأخ');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Cousin', 'Amakivachcha','Двоюродная сестра','قريب');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Daughter', 'Qiz','Дочь','الإبنة');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Ex-husband', 'Sobiq er','Бывший муж','زوج سابق');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Ex-wife', 'Sobiq xotin','Бывшая жена','الزوجة السابقة');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Father', 'Ota','Отец','الأب');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Husband', 'Er','Супруг','الزوج');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Mother', 'Ona','Мать','الأم');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Sister', 'Singil','Сестра','أخت');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Son', 'O''g''il','Сын','الإبن');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Stepson', 'O''gay o''g''il','Сводный сын','ربيب/ة');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Stepdaughter', 'O''gay qiz','Падчерица','الربيبة');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Stepfather', 'O''gay ota','Отчим','زوج الأم');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Stepmother', 'O''gay ona','Отчим','Мачеха');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Uncle', 'Amaki','Дядя','العم');
INSERT INTO "anv".reference_locale(english, uzbek, russian, arabic)
VALUES ('Wife', 'Rafiqa','Супруга','الزوجة');
INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Mother In Law', 'Qaynona','Мать супруги');
INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Father In Law', 'Qaynota','Отец супруги');
INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Daughter In Law', 'Kelin','Невестка');
INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Nephew', 'Jiyan','Племянник');
INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Grandmother', 'Buvi','Бабушка');
INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Grandfather', 'Bobo','Дедушка');
INSERT INTO "anv".reference_locale(english, uzbek, russian)
VALUES ('Other relative', 'Boshqa qarindosh','Другой родственник');


UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Aunt' and uzbek = 'Xola')
WHERE code = 'AUNT';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Brother' and uzbek = 'Aka-uka')
WHERE code = 'BROTHER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Cousin' and uzbek = 'Amakivachcha')
WHERE code = 'COUSIN';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Daughter' and uzbek = 'Qiz')
WHERE code = 'DAUGHTER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Ex-husband' and uzbek = 'Sobiq er')
WHERE code = 'EX_HUSBAND';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Ex-wife' and uzbek = 'Sobiq xotin')
WHERE code = 'EX_WIFE';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Father' and uzbek = 'Ota')
WHERE code = 'FATHER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Husband' and uzbek = 'Er')
WHERE code = 'HUSBAND';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Mother' and uzbek = 'Ona')
WHERE code = 'MOTHER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Sister' and uzbek = 'Singil')
WHERE code = 'SISTER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Son' and uzbek = 'O''g''il')
WHERE code = 'SON';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Stepson' and uzbek = 'O''gay o''g''il')
WHERE code = 'STEPSON';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Stepdaughter' and uzbek = 'O''gay qiz')
WHERE code = 'STEPDAUGHTER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Stepfather' and uzbek = 'O''gay ota')
WHERE code = 'STEPFATHER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Stepmother' and uzbek = 'O''gay ona')
WHERE code = 'STEPMOTHER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Uncle' and uzbek = 'Amaki')
WHERE code = 'UNCLE';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Wife' and uzbek = 'Rafiqa')
WHERE code = 'WIFE';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Mother In Law' and uzbek = 'Qaynona')
WHERE code = 'MOTHER_IN_LAW';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Father In Law' and uzbek = 'Qaynota')
WHERE code = 'FATHER_IN_LAW';


UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Daughter In Law' and uzbek = 'Kelin')
WHERE code = 'DAUGHTER_IN_LAW';


UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Nephew' and uzbek = 'Jiyan')
WHERE code = 'NEPHEW';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Grandmother' and uzbek = 'Buvi')
WHERE code = 'GRANDMOTHER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Grandfather' and uzbek = 'Bobo')
WHERE code = 'GRANDFATHER';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Other relative' and uzbek = 'Boshqa qarindosh')
WHERE code = 'OTHER_RELATIVE';














