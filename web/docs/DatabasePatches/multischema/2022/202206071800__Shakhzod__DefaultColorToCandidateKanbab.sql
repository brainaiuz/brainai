update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '728fff')
where code = 'CANDIDATE_STATUS_NEW';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#ffce44')
where code = 'CANDIDATE_STATUS_MATCHED';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#af78c8')
where code = 'CANDIDATE_STATUS_INTERVIEW';
update "anv".Reference
set referencecolorid =(select id from "anv".referencecolor where hex = '51cc98')
where code = 'CANDIDATE_STATUS_ON_HOLD';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#337BE2')
where code = 'CANDIDATE_STATUS_REJECTED';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#47A7E6')
where code = 'CANDIDATE_STATUS_OFFER_MADE';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#49943D')
where code = 'CANDIDATE_STATUS_PLACED';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#2E649E')
where code = 'CANDIDATE_STATUS_OFFER_DECLINED';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#59B378')
where code = 'CANDIDATE_STATUS_OFFER_WITHDRAWN';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#000000')
where code = 'CANDIDATE_STATUS_HIRED';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#855099')
where code = 'CANDIDATE_STATUS_UNQUALIFIED';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#E73532')
where code = 'CANDIDATE_STATUS_SHORTLIST';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#6A4AA9')
where code = 'CANDIDATE_STATUS_AVAILABLE';
update "anv".Reference
set referencecolorid = (select id from "anv".referencecolor where hex = '#99CA3A')
where code = 'CANDIDATE_STATUS_NOT_AVAILABLE';