update "anv".rfq set overallstatus = (select id from "anv".reference where code = 'APPROVED' and parentid = (select id from "anv".reference where code = 'RFQ_STATUS' limit 1))
where overallstatus = (select id from "anv".reference where code = 'OPEN' and parentid = (select id from "anv".reference where code = 'RFQ_STATUS' limit 1));
