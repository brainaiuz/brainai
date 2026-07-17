DROP FUNCTION IF EXISTS "anv".update_contact_primary_addresses();
CREATE OR REPLACE FUNCTION "anv".update_contact_primary_addresses()
  RETURNS INTEGER AS
  $BODY$
    DECLARE
      crmContactId INTEGER;
      BEGIN
        FOR crmContactId IN (SELECT id FROM "anv".crmContact WHERE deleted = false)
        LOOP
          BEGIN
            IF EXISTS (SELECT id FROM "anv".address WHERE entityType = 'contact'
                                AND deleted = FALSE AND contactID = crmContactId
                                AND isprimary = FALSE ORDER BY id ASC limit 1
            ) AND (SELECT id FROM "anv".address WHERE entityType = 'contact' AND deleted = FALSE
                                AND contactID = crmContactId AND isprimary = TRUE limit 1
            ) IS NULL AND (SELECT id FROM "anv".address WHERE entityType = 'contact' AND deleted = FALSE
                                AND contactID = crmContactId ORDER BY id ASC limit 1
            ) > 0 THEN
                UPDATE "anv".address SET isprimary = TRUE WHERE id = (SELECT id FROM "anv".address WHERE entityType = 'contact'
                  AND deleted = FALSE AND contactID = crmContactId ORDER BY id ASC limit 1);
            END IF;
          END;
        END LOOP;
        RETURN NULL;
      END;
  $BODY$
LANGUAGE plpgsql;
ALTER FUNCTION "anv".update_contact_primary_addresses() OWNER TO wfmtest;
UPDATE company SET isdeleted = FALSE WHERE isdeleted IS NULL AND (select "anv".update_contact_primary_addresses()) IS NOT NULL;