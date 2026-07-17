--// Faqat so'ralgan companylarga

DROP function if EXISTS "500005".certificate_generic_permissions();
CREATE OR replace function "500005".certificate_generic_permissions()
  returns INTEGER AS
  $body$
DECLARE
    addPermissionId INTEGER;
    editPermissionId INTEGER;
    deletePermissionId INTEGER;
    pdfPermissionId INTEGER;
    sorderPermission INTEGER;
    cert record;

    BEGIN
        IF EXISTS (select "value" from "500005".genericSettings where "key" ='ENABLE_MULTI_APPROVAL_CERTIFICATE')
              AND (select "value" from "500005".genericSettings where "key" ='ENABLE_MULTI_APPROVAL_CERTIFICATE') = 'YES'
        THEN
          FOR cert IN (SELECT * FROM "500005".certificateofemploymenttype WHERE deleted IS FALSE)
          LOOP
              IF NOT EXISTS (SELECT id FROM permission WHERE code = ('CERTIFICATE_OF_EMPLOYMENT_' || (replace(cert.formid, '_FORM', '')) || '_ADD_' || 500005)
              ) AND (COALESCE(cert.formid, '') = '') IS NOT TRUE
              THEN
                  sorderPermission = (SELECT MAX(sorder) FROM permission WHERE parent = (SELECT id FROM permission WHERE code = 'CETIFICATE_OF_EMPLOYMENT_LIST'));

                  INSERT INTO permission (code, context, ismainmenu, name, sorder, parent, modulecode, companyId)
                  VALUES ('CERTIFICATE_OF_EMPLOYMENT_' || (replace(cert.formid, '_FORM', '')) || '_ADD_' || 500005, 'HRMS', FALSE, cert.name || ' Add', COALESCE(sorderPermission, 1) + 1, (SELECT id FROM permission WHERE code='CETIFICATE_OF_EMPLOYMENT_LIST'), 'HRMS_MODULE', 500005) RETURNING id INTO addPermissionId;

                  INSERT INTO permission (code, context, ismainmenu, name, sorder, parent, modulecode, companyId)
                  VALUES ('CERTIFICATE_OF_EMPLOYMENT_' || (replace(cert.formid, '_FORM', '')) || '_EDIT_' || 500005, 'HRMS', FALSE, cert.name || ' Edit', 1, addPermissionId, 'HRMS_MODULE', 500005) RETURNING id INTO editPermissionId;

                  INSERT INTO permission (code, context, ismainmenu, name, sorder, parent, modulecode, companyId)
                  VALUES ('CERTIFICATE_OF_EMPLOYMENT_' || (replace(cert.formid, '_FORM', '')) || '_DELETE_' || 500005, 'HRMS', FALSE, cert.name || ' Delete', 2, addPermissionId, 'HRMS_MODULE', 500005) RETURNING id INTO deletePermissionId;

                  INSERT INTO permission (code, context, ismainmenu, name, sorder, parent, modulecode, companyId)
                  VALUES ('CERTIFICATE_OF_EMPLOYMENT_' || (replace(cert.formid, '_FORM', '')) || '_PDF_' || 500005, 'HRMS', FALSE, cert.name || ' Pdf', 3, addPermissionId, 'HRMS_MODULE', 500005) RETURNING id INTO pdfPermissionId;

                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = addPermissionId), 'ADMIN', 'ALLOW');
                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = addPermissionId), 'DR', 'ALLOW');
                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = addPermissionId), 'HR', 'ALLOW');

                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = editPermissionId), 'ADMIN', 'ALLOW');
                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = editPermissionId), 'DR', 'ALLOW');
                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = editPermissionId), 'HR', 'ALLOW');

                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = deletePermissionId), 'ADMIN', 'ALLOW');
                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = deletePermissionId), 'DR', 'ALLOW');
                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = deletePermissionId), 'HR', 'ALLOW');

                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = pdfPermissionId), 'ADMIN', 'ALLOW');
                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = pdfPermissionId), 'DR', 'ALLOW');
                  INSERT INTO "500005".rolepermission (permissioncode, rolecode, access) VALUES ((SELECT code FROM permission WHERE id = pdfPermissionId), 'HR', 'ALLOW');
              END IF;
          END LOOP;
        END IF;
    return NULL;
    END;
$body$
LANGUAGE plpgsql;
ALTER function "500005".certificate_generic_permissions() owner TO wfmtest;
UPDATE company SET isdeleted = FALSE WHERE isdeleted IS NULL AND (select "500005".certificate_generic_permissions()) IS NOT NULL;