delete from "anv".modelfield where field_id = 'EMPLOYEE_DEGREE' and form_id = 'HRMS_EMPLOYEE_FORM';
insert into "anv".modelfield
(form_id,                   fsection,                   section,                 hide,  nolabelfor,    fieldstyle,    columntype,   fieldsetstyle,    rowstyle,    mandatory,    sectionstyle,    widget,         forder,    field_id) values
('HRMS_EMPLOYEE_FORM',	    'EMPLOYEE_INFORMATION',     'EMPLOYEE_INFORMATION',  true, '',            'field',         'COL_1',	     '',               '',          false,       '',              'DropDown',     1,	     'EMPLOYEE_DEGREE');

DELETE  FROM "anv".spokenlanguages WHERE entitytype='EMPLOYEE';

DROP function if EXISTS "anv".createEmployeeLanguageMigration();
CREATE OR replace function "anv".createEmployeeLanguageMigration()
  returns INTEGER AS
$body$
DECLARE  fold record;
BEGIN

  FOR fold IN (select * from "anv".employee_spokenlanguages)
    loop
     insert into "anv".spokenlanguages(entityid,entitytype,languageid,levelid) values(fold.employeeprofile_id,'EMPLOYEE',
                                            fold.language_id,(select id from "anv".reference where code='BEGINNER'));
    END loop;
  return NULL;
END;
$body$
  LANGUAGE plpgsql;
ALTER function "anv".createEmployeeLanguageMigration() owner TO postgres;

UPDATE company SET selectFunctioncolumn =(SELECT "anv".createEmployeeLanguageMigration()) WHERE  id=(SELECT id FROM company LIMIT 1);
