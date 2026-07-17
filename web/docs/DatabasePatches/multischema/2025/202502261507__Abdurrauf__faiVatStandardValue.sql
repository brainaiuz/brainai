update "anv".vat
set faiid           = (select id from "anv".reference where code = 'SA_SA_EXMXXXX' limit 1),
    faicategorieids = concat('[', (select string_agg(id::text, ', ')
                                    from "anv".reference
                                    where parentid in (select id from "anv".reference where code = '_FAI_CATEGORY')
                                      and sorder in (21, 22, 23)), ']')::jsonb
where key = 'EXEMPT';

update "anv".vat
set faiid           = (select id from "anv".reference where code = 'SA_SA_NAAXXXX' limit 1),
    faicategorieids = concat('[', (select string_agg(id::text, ', ')
                                    from "anv".reference
                                    where parentid in (select id from "anv".reference where code = '_FAI_CATEGORY')
                                      and sorder in (2, 3, 4, 5, 6, 16, 19, 20, 21, 22, 23, 54, 55, 1)), ']')::jsonb
where key = 'OUT_OF_SCOPE';
