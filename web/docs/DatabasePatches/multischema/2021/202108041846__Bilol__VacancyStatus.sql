UPDATE "anv".reference SET description = 70, shortname='Vacancy Declined status' where code = 'VS_DECLINED' and parentid = (SELECT id FROM "65159".reference where code = 'VACANCY_STATUSES');
UPDATE "anv".reference SET description = 0, shortname='Vacancy Cancelled status' where code = 'VS_CANCELLED' and parentid = (SELECT id FROM "65159".reference where code = 'VACANCY_STATUSES');
UPDATE "anv".reference SET description = 100, shortname='Vacancy Filled status' where code = 'VS_FILLED' and parentid = (SELECT id FROM "65159".reference where code = 'VACANCY_STATUSES');
UPDATE "anv".reference SET description = 60, shortname='Vacancy Partially Filled status' where code = 'VS_PARTIALLY_FILLED' and parentid = (SELECT id FROM "65159".reference where code = 'VACANCY_STATUSES');
UPDATE "anv".reference SET description = 40, shortname='Vacancy On hold status' where code = 'VS_ON_HOLD' and parentid = (SELECT id FROM "65159".reference where code = 'VACANCY_STATUSES');
UPDATE "anv".reference SET description = 30, shortname='Vacancy In progress status' where code = 'VS_IN_PROGRESS' and parentid = (SELECT id FROM "65159".reference where code = 'VACANCY_STATUSES');
UPDATE "anv".reference SET description = 50, shortname='Vacancy Open status' where code = 'VS_OPEN' and parentid = (SELECT id FROM "65159".reference where code = 'VACANCY_STATUSES');
