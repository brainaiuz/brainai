
insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name)
values ('Sub Stage Lost Reasons', '_OPPORTUNITY_SUB_STAGE', false, true, true, 'Sub Stage Lost Reasons');


insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name, parentid, sorder)
values ('Undefined reason', '_OPPORTUNITY_SUB_STAGE_UNDEFINED_REASON', false, true, true, 'Undefined reason',
        (select id from "anv".reference where code = '_OPPORTUNITY_SUB_STAGE'), 1),
       ('Insufficient budget', '_OPPORTUNITY_SUB_STAGE_INSUFFICIENT_BUDGET', false, true, true, 'Insufficient budget',
        (select id from "anv".reference where code = '_OPPORTUNITY_SUB_STAGE'), 2),
       ('Product does not fit need', '_OPPORTUNITY_SUB_STAGE_PRODUCT_DOES_NOT_FIT_NEED', false, true, true,
        'Product does not fit need', (select id from "anv".reference where code = '_OPPORTUNITY_SUB_STAGE'), 3),
       ('Not satisfied with conditions', '_OPPORTUNITY_SUB_STAGE_NOT_SATISFIED_CONDITION', false, true, true,
        'Not satisfied with conditions', (select id from "anv".reference where code = '_OPPORTUNITY_SUB_STAGE'), 4),
       ('Bought from competitor', '_OPPORTUNITY_SUB_STAGE_BOUGHT_FROM_COMPETITOR', false, true, true,
        'Bought from competitor', (select id from "anv".reference where code = '_OPPORTUNITY_SUB_STAGE'), 5);