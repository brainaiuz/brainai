ALTER TABLE "anv".meetingminutes ALTER COLUMN nonCompanyAttendees TYPE text;

DROP FUNCTION IF EXISTS "anv".meeting_minutes_move_attendees();
CREATE OR REPLACE FUNCTION "anv".meeting_minutes_move_attendees()
  RETURNS INTEGER AS
  $BODY$
    DECLARE
      mee record;
      ma record;
      attendeesEmails text;
      contactEmail text;
      contactFirstName text;
      contactLastName text;
      contactCount integer;
      BEGIN
          FOR mee IN (SELECT * FROM "anv".meetingminutes)
          LOOP
            BEGIN
                attendeesEmails = '';

                FOR ma IN (SELECT * FROM "anv".meetingattendees WHERE isattendees = TRUE AND meetingminutesid = mee.id)
                LOOP
                    EXECUTE 'select count(c.id) from "anv".employee e
                                                  join "anv".employeeprofile ep on e.profileid = ep.id
                                                  join "anv".crmcontact c on ep.contact_id = c.id
                                                  where c.primaryemail is not null
                                                  and e.id = $1'
                    INTO contactCount USING ma.attendeesEmployeeID;

                    IF contactCount > 0
                    THEN
                        contactEmail = (select c.primaryemail from "anv".employee e
                                                  join "anv".employeeprofile ep on e.profileid = ep.id
                                                  join "anv".crmcontact c on ep.contact_id = c.id
                                                  where c.primaryemail is not null
                                                  and e.id = ma.attendeesEmployeeID);

                        contactFirstName = (select c.firstname from "anv".employee e
                                                  join "anv".employeeprofile ep on e.profileid = ep.id
                                                  join "anv".crmcontact c on ep.contact_id = c.id
                                                  where c.primaryemail is not null
                                                  and e.id = ma.attendeesEmployeeID);

                        contactLastName = (select c.lastname from "anv".employee e
                                                  join "anv".employeeprofile ep on e.profileid = ep.id
                                                  join "anv".crmcontact c on ep.contact_id = c.id
                                                  where c.primaryemail is not null
                                                  and e.id = ma.attendeesEmployeeID);

                        attendeesEmails =attendeesEmails || contactFirstName || ' ' || contactLastName || '<' || contactEmail || '>,';

                    END IF;
                END LOOP;

                UPDATE "anv".meetingminutes SET nonCompanyAttendees = COALESCE(nonCompanyAttendees, '') || attendeesEmails WHERE id = mee.id;

            END;
      END LOOP;
      RETURN NULL;
    END;
  $BODY$
LANGUAGE plpgsql;
ALTER FUNCTION "anv".meeting_minutes_move_attendees() OWNER TO wfmtest;
UPDATE company SET isdeleted = FALSE WHERE isdeleted IS NULL AND (select "anv".meeting_minutes_move_attendees()) IS NOT NULL;