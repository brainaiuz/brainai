

---_LEAD_STATUS
update "anv".reference set referencecolorid=1 ,sorder=10 where code='ATTEMPTED_TO_CONTACT';
update "anv".reference set referencecolorid=2 ,sorder=20 where code='CONTACT_IN_FUTURE';
update "anv".reference set referencecolorid=3 ,sorder=30 where code='CONTACTED';
update "anv".reference set referencecolorid=4 ,sorder=40 where code='JUNK_LEAD';
update "anv".reference set referencecolorid=5 ,sorder=50 where code='LOST_LEAD';
update "anv".reference set referencecolorid=6 ,sorder=60 where code='NOT_CONTACTED';
update "anv".reference set referencecolorid=7 ,sorder=70 where code='PRE_QUALIFIED';

--- _OPPORTUNITY_STAGE
update "anv".reference set referencecolorid=1 ,sorder=10 where code='QUALIFICATION';
update "anv".reference set referencecolorid=2 ,sorder=20 where code='NEEDS_ANALYSIS';
update "anv".reference set referencecolorid=3 ,sorder=30 where code='VALUE_PROPOSITION';
update "anv".reference set referencecolorid=4 ,sorder=40 where code='ID_DECISION_MAKERS';
update "anv".reference set referencecolorid=5 ,sorder=50 where code='PROPOSAL_PRICE_QUOTE';
update "anv".reference set referencecolorid=6 ,sorder=60 where code='NEGOTIATION_REVIEW';
update "anv".reference set referencecolorid=7 ,sorder=70 where code='CLOSED_WON';
update "anv".reference set referencecolorid=8 ,sorder=80 where code='CLOSED_LOST';
update "anv".reference set referencecolorid=1 ,sorder=90 where code='CLOSED_LOST_TO_COMPETITION';
update "anv".reference set referencecolorid=2 ,sorder=100 where code='NEEDS_ESTIMATE';
update "anv".reference set referencecolorid=3 ,sorder=110 where code='ESTIMATED';
update "anv".reference set referencecolorid=4 ,sorder=120 where code='ON_TENDER';

--- _TASK_STATUS

update "anv".reference set referencecolorid=1 ,sorder=10 where code='NOT_STARTED';
update "anv".reference set referencecolorid=2 ,sorder=20 where code='IN_PROGRESS';
update "anv".reference set referencecolorid=3 ,sorder=30 where code='ON_HOLD';
update "anv".reference set referencecolorid=4 ,sorder=40 where code='COMPLETED';
update "anv".reference set referencecolorid=5 ,sorder=50 where code='CANCELLED';
update "anv".reference set referencecolorid=6 ,sorder=60 where code='WAITING_FOR_SOMEONE_ELSE';
update "anv".reference set referencecolorid=7 ,sorder=70 where code='CLOSED';