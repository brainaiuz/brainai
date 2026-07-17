delete
from "anv".reference
where parentid = (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON');
delete
from "anv".reference
where code = 'EMPLOYEE_REJECTION_REASON';


insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name)
values ('Employee Lost Reasons', 'EMPLOYEE_REJECTION_REASON', false, true, true, 'Employee Lost Reasons');


insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name, parentid, sorder,
                            requiredComment)
values ('не устраивает заработная плата', 'НЕ_УСТРАИВАЕТ_ЗАРАБОТНАЯ_ПЛАТА', false, true, true, 'не устраивает заработная плата',
        (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON'), 1, true),
       ('конфликт с руководителем', 'КОНФОИКТ_С_РУКОВОДИТЕЛЕМ', false, true, true, 'конфликт с руководителем',
        (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON'), 1, true),
       ('нет карьерного роста', 'НЕТ_КАРЬЕРНОГО_РОСТА', false, true, true, 'нет карьерного роста',
        (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON'), 1, true),
       ('не могу найти общий язык в команде', 'НЕ_МОГУ_НАЙТИ_ОБЩИЙ_ЯЗЫК_В_КОМАНДЕ', false, true, true, 'не могу найти общий язык в команде',
        (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON'), 1, true),
       ('предложили работу интереснее', 'ПРЕДЛОЖИЛИ_РАБОТУ_ИНТЕРЕСНЕЕ', false, true, true, 'предложили работу интереснее',
        (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON'), 1, true),
       ('по семейным обстоятельствам', 'ПО_СЕМЕЙНЫМ_ОБСТОЯТЕЛЬСТВАМ', false, true, true, 'по семейным обстоятельствам',
        (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON'), 1, true),
       ('не вижу себя на этой должности', 'НЕ_ВИЖУ_СЕБЯ_НА_ЭТОЙ_ДОЛЖНОСТИ', false, true, true, 'не вижу себя на этой должности',
        (select id from "anv".reference where code = 'EMPLOYEE_REJECTION_REASON'), 1, true);

