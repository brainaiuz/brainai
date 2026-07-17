--// Faqat so'ralgan companylarga

DROP function if EXISTS "500005".certificate_create_workflow_module();
CREATE OR replace function "500005".certificate_create_workflow_module()
  returns INTEGER AS
  $body$
DECLARE
    cert record;

    BEGIN
        IF EXISTS (select "value" from "500005".genericSettings where "key" ='ENABLE_MULTI_APPROVAL_CERTIFICATE')
              AND (select "value" from "500005".genericSettings where "key" ='ENABLE_MULTI_APPROVAL_CERTIFICATE') = 'YES'
        THEN
          FOR cert IN (SELECT * FROM "500005".certificateofemploymenttype WHERE deleted IS FALSE)
          LOOP
              IF NOT EXISTS (SELECT id FROM "500005".reference WHERE code = ('_WORKFLOW_MODULE_' || (replace(cert.formid, '_FORM', '')))
                                                               AND parentid = (SELECT id FROM "500005".reference WHERE code = '_WORKFLOW_MODULE')
              ) AND (COALESCE(cert.formid, '') = '') IS NOT TRUE
              THEN

                  INSERT INTO "500005".reference (code, name, sorder, parentid)
                  VALUES ('_WORKFLOW_MODULE_' || (replace(cert.formid, '_FORM', '')), cert.name, (SELECT max(sorder) FROM "500005".reference
                                                                      WHERE parentid = (SELECT id FROM "500005".reference
                                                                                        WHERE code = '_WORKFLOW_MODULE')),
                                                                  (SELECT id FROM "500005".reference WHERE code = '_WORKFLOW_MODULE'));
              END IF;
          END LOOP;
        END IF;
    return NULL;
    END;
$body$
LANGUAGE plpgsql;
ALTER function "500005".certificate_create_workflow_module() owner TO wfmtest;
UPDATE company SET isdeleted = FALSE WHERE isdeleted IS NULL AND (select "500005".certificate_create_workflow_module()) IS NOT NULL;