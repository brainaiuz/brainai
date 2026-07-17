insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name)
values ('Sub Stage Reject Reasons', '_CANDIDATE_SUB_STAGE', false, true, true, 'Sub Stage Reject Reasons');


insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name, parentid, sorder,
                            requiredcomment)
values ('Низкая Оплата', '_CANDIDATE_SUB_STAGE_LOW_PAY', false, true, true, 'Низкая Оплата',
        (select id from "anv".reference where code = '_CANDIDATE_SUB_STAGE'), 1, true),
       ('Локация не подходит', '_CANDIDATE_SUB_STAGE_NOT_SUITABLE_LOCATION', false, true, true, 'Локация не подходит',
        (select id from "anv".reference where code = '_CANDIDATE_SUB_STAGE'), 2, true),
       ('Режим работы', '_CANDIDATE_SUB_STAGE_DUTY', false, true, true,
        'Режим работы', (select id from "anv".reference where code = '_CANDIDATE_SUB_STAGE'), 3, true),
       ('Особенность кандидата', '_CANDIDATE_SUB_STAGE_FEATURE_OF_CANDIDATE', false, true, true,
        'Особенность кандидата', (select id from "anv".reference where code = '_CANDIDATE_SUB_STAGE'), 4, true);
