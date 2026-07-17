update "anv".quote
set overallstatus = (select id
                     from "anv".reference
                     where code = 'APPROVE'
                       and parentid = (select id from "anv".reference where code = 'INVOICE_STATUS'))
where overallstatus = (select id
                       from "anv".reference
                       where code = 'OPEN'
                         and parentid = (select id from "anv".reference where code = 'INVOICE_STATUS'));