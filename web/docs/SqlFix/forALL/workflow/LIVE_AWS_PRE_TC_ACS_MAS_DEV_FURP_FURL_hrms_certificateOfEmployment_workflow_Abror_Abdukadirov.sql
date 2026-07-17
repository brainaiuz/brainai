delete from "0".reference  where code = 'CERTIFICATE_OF_EMPLOYMENT_STATUS';
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, isactive)
							values('CERTIFICATE_OF_EMPLOYMENT_STATUS', false, true, 'Certificate Status', true, 1, true);

delete from "0".reference  where code = 'CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED';
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED', false, true, 'Approved', true, 2, (select id from "0".reference where code='CERTIFICATE_OF_EMPLOYMENT_STATUS'), true);


delete from "0".reference  where code = 'CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED';
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED', false, true, 'Rejected', true, 3, (select id from "0".reference where code='CERTIFICATE_OF_EMPLOYMENT_STATUS'), true);


delete from "0".reference  where code = 'CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED';
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED', false, true, 'Submitted', true, 4, (select id from "0".reference where code='CERTIFICATE_OF_EMPLOYMENT_STATUS'), true);



update "anv".certificateofemploymenttype SET formid = upper(replace(name, ' ', '_')) where deleted = false;
update "anv".certificateofemploymenttype SET formid = upper(formid || '_FORM') where deleted = false and lower(formid) not like '%_form';

delete from "anv".reference  where code = 'CERTIFICATE_OF_EMPLOYMENT_STATUS';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, isactive)
							values('CERTIFICATE_OF_EMPLOYMENT_STATUS', false, true, 'Certificate Status', true, 1, true);

delete from "anv".reference  where code = 'CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED', false, true, 'Approved', true, 2, (select id from "anv".reference where code='CERTIFICATE_OF_EMPLOYMENT_STATUS'), true);


delete from "anv".reference  where code = 'CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED', false, true, 'Rejected', true, 3, (select id from "anv".reference where code='CERTIFICATE_OF_EMPLOYMENT_STATUS'), true);


delete from "anv".reference  where code = 'CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED', false, true, 'Submitted', true, 4, (select id from "anv".reference where code='CERTIFICATE_OF_EMPLOYMENT_STATUS'), true);




=========

Shartmas


DROP FUNCTION IF EXISTS "500005".create_certificate_form();
CREATE OR REPLACE FUNCTION "500005".create_certificate_form()
  RETURNS INTEGER AS
  $BODY$
    DECLARE
      cert record;
      BEGIN
          FOR cert IN (SELECT * FROM "500005".certificateofemploymenttype WHERE deleted = false)
          LOOP
            BEGIN
              IF NOT EXISTS (SELECT id FROM model WHERE formid = cert.formid
              ) THEN
                  INSERT INTO model(active, formid, title, viewname) VALUES(true, cert.formid, cert.name, cert.name);
              END IF;
            END;
      END LOOP;
      RETURN NULL;
    END;
  $BODY$
LANGUAGE plpgsql;
ALTER FUNCTION "500005".create_certificate_form() OWNER TO wfmtest;
UPDATE company SET isdeleted = FALSE WHERE isdeleted IS NULL AND (select "500005".create_certificate_form()) IS NOT NULL;