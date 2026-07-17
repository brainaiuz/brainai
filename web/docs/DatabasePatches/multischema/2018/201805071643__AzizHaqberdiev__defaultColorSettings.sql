DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT id FROM "0".referenceColor WHERE hex = '#f5a623' LIMIT 1
        ) THEN
            insert into "0".referenceColor (name, hex) values ('vivid orange', '#f5a623');
        END IF;
END$$;
DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT id FROM "0".referenceColor WHERE hex = '#85ca40' LIMIT 1
        ) THEN
            insert into "0".referenceColor (name, hex) values ('moderate green', '#85ca40');
        END IF;
END$$;
DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT id FROM "0".referenceColor WHERE hex = '#00a8ea' LIMIT 1
        ) THEN
            insert into "0".referenceColor (name, hex) values ('pure (or mostly pure) blue', '#00a8ea');
        END IF;
END$$;
DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT id FROM "0".referenceColor WHERE hex = '#8d9ba8' LIMIT 1
        ) THEN
            insert into "0".referenceColor (name, hex) values ('dark grayish blue', '#8d9ba8');
        END IF;
END$$;
DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT id FROM "0".referenceColor WHERE hex = '#536677' LIMIT 1
        ) THEN
            insert into "0".referenceColor (name, hex) values ('mostly desaturated dark blue', '#536677');
        END IF;
END$$;

delete from "0".reference where code not in ('NOT_CONTACTED','ATTEMPTED_TO_CONTACT','CONTACT_IN_FUTURE','PRE_QUALIFIED','CONTACTED') and parentid = (select id from "0".reference where code = '_LEAD_STATUS');
update "0".reference set name = 'Unqualified', sorder = 1, color = '#f5a623', referenceColorId = (select id from "0".referenceColor where hex = '#f5a623' limit 1), description = 'These are leads that may have told you they`re no longer interested in moving forward nor the authority to purchase from you, mark the lead unqualified' where code = 'NOT_CONTACTED' and parentid = (select id from "0".reference where code = '_LEAD_STATUS');
update "0".reference set name = 'New', sorder = 2, color = '#85ca40', referenceColorId = (select id from "0".referenceColor where hex = '#85ca40' limit 1), description = 'These are leads you have not yet reached out to.Respond to your lead within five minutes to increase your chances of converting the lead record to an opportunity' where code = 'ATTEMPTED_TO_CONTACT' and parentid = (select id from "0".reference where code = '_LEAD_STATUS');
update "0".reference set name = 'Working', sorder = 3, color = '#00a8ea', referenceColorId = (select id from "0".referenceColor where hex = '#00a8ea' limit 1), description = 'Gather as much information as you can. Get your lead’s email address, phone number, and title so that you can include your lead in future campaigns' where code = 'CONTACT_IN_FUTURE' and parentid = (select id from "0".reference where code = '_LEAD_STATUS');
update "0".reference set name = 'Nurturing', sorder = 4, color = '#8d9ba8', referenceColorId = (select id from "0".referenceColor where hex = '#8d9ba8' limit 1), description = 'Send emails based on time intervals and create rules. Nurturing leads helps to free up time to focus on other opportunities in your pipeline' where code = 'PRE_QUALIFIED' and parentid = (select id from "0".reference where code = '_LEAD_STATUS');
update "0".reference set name = 'Qualified', sorder = 5, color = '#536677', referenceColorId = (select id from "0".referenceColor where hex = '#536677' limit 1), description = 'Ready  to Convert lead to the opportunity' where code = 'CONTACTED' and parentid = (select id from "0".reference where code = '_LEAD_STATUS');

delete from "0".reference where code not in ('QUALIFICATION','NEEDS_ANALYSIS','PROPOSAL_PRICE_QUOTE','NEGOTIATION_REVIEW','CLOSED_WON','CLOSED_LOST') and parentid = (select id from "0".reference where code = '_OPPORTUNITY_STAGE');
update "0".reference set name = 'Qualification', sorder = 1, color = '#f5a623', referenceColorId = (select id from "0".referenceColor where hex = '#f5a623' limit 1), shortname = 'Qualify the opportunity and confirm budget' where code = 'QUALIFICATION' and parentid = (select id from "0".reference where code = '_OPPORTUNITY_STAGE');
update "0".reference set name = 'Need Analysis', sorder = 2, color = '#85ca40', referenceColorId = (select id from "0".referenceColor where hex = '#85ca40' limit 1), shortname = 'Understand the business need and decision criteria' where code = 'NEEDS_ANALYSIS' and parentid = (select id from "0".reference where code = '_OPPORTUNITY_STAGE');
update "0".reference set name = 'Proposal', sorder = 3, color = '#00a8ea', referenceColorId = (select id from "0".referenceColor where hex = '#00a8ea' limit 1), shortname = 'Present the solution and understand the buying process' where code = 'PROPOSAL_PRICE_QUOTE' and parentid = (select id from "0".reference where code = '_OPPORTUNITY_STAGE');
update "0".reference set name = 'Negotiation', sorder = 4, color = '#8d9ba8', referenceColorId = (select id from "0".referenceColor where hex = '#8d9ba8' limit 1), shortname = 'Negotiate value and resolve objections' where code = 'NEGOTIATION_REVIEW' and parentid = (select id from "0".reference where code = '_OPPORTUNITY_STAGE');
update "0".reference set name = 'Closed Won', sorder = 5, color = '#536677', referenceColorId = (select id from "0".referenceColor where hex = '#536677' limit 1), shortname = 'Congrats! Keep Up the Good Work!' where code = 'CLOSED_WON' and parentid = (select id from "0".reference where code = '_OPPORTUNITY_STAGE');
update "0".reference set name = 'Lost', sorder = 6, color = '#536677', referenceColorId = (select id from "0".referenceColor where hex = '#536677' limit 1), shortname = 'Revise techniques to increase future wins' where code = 'CLOSED_LOST' and parentid = (select id from "0".reference where code = '_OPPORTUNITY_STAGE');

delete from "0".reference where code not in ('NEW','WAITING_FOR_REPLY','REPLIED','RESOLVED','CS_CLOSED') and parentid = (select id from "0".reference where code = '_CASE_STATUS');
update "0".reference set name = 'New', sorder = 1, color = '#f5a623', referenceColorId = (select id from "0".referenceColor where hex = '#f5a623' limit 1), description = 'This means the case has not been worked on' where code = 'NEW' and parentid = (select id from "0".reference where code = '_CASE_STATUS');
update "0".reference set name = 'Open', sorder = 2, color = '#85ca40', referenceColorId = (select id from "0".referenceColor where hex = '#85ca40' limit 1), description = 'This means an agent is working on the case' where code = 'WAITING_FOR_REPLY' and parentid = (select id from "0".reference where code = '_CASE_STATUS');
update "0".reference set name = 'Pending', sorder = 3, color = '#00a8ea', referenceColorId = (select id from "0".referenceColor where hex = '#00a8ea' limit 1), description = 'This means that the case is waiting for a response back from the customer' where code = 'REPLIED' and parentid = (select id from "0".reference where code = '_CASE_STATUS');
update "0".reference set name = 'Resolved', sorder = 4, color = '#8d9ba8', referenceColorId = (select id from "0".referenceColor where hex = '#8d9ba8' limit 1), description = 'This means the case is finished and a resolution was provided' where code = 'RESOLVED' and parentid = (select id from "0".reference where code = '_CASE_STATUS');
update "0".reference set name = 'Closed', sorder = 5, color = '#536677', referenceColorId = (select id from "0".referenceColor where hex = '#536677' limit 1), description = 'This means that the case is permanently archived and set to read only' where code = 'CS_CLOSED' and parentid = (select id from "0".reference where code = '_CASE_STATUS');
