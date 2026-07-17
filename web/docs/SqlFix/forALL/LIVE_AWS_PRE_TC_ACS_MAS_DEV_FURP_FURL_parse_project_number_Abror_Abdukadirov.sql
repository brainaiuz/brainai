DROP FUNCTION IF EXISTS "50802".parse_project_number();
CREATE OR REPLACE FUNCTION "50802".parse_project_number()
  RETURNS INTEGER AS
  $BODY$
    DECLARE
      prj record;
      BEGIN
          FOR prj IN (SELECT * FROM "50802".project WHERE isdeleted = false)
          LOOP
            BEGIN
              IF EXISTS (SELECT id FROM "50802".project WHERE intnumber IS null AND id = prj.id
              ) THEN
                  UPDATE "50802".project SET intnumber = CAST(NULLIF(prj.number, '') AS integer) WHERE id = prj.id;
              END IF;

              EXCEPTION WHEN OTHERS THEN
            END;
      END LOOP;
      RETURN NULL;
    END;
  $BODY$
LANGUAGE plpgsql;
ALTER FUNCTION "50802".parse_project_number() OWNER TO wfmtest;
UPDATE company SET isdeleted = FALSE WHERE isdeleted IS NULL AND (select "50802".parse_project_number()) IS NOT NULL;