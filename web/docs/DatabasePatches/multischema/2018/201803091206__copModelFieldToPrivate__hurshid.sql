delete from "0".modelfield;

delete from modelfield where id in (select max(id) from modelfield group by field_id,form_id having count(id)>1);

delete from "anv".modelfield where id in (select max(id) from "anv".modelfield group by field_id,form_id having count(id)>1);

DROP FUNCTION IF EXISTS "anv".copymodelfieldtoprivateschema();
CREATE OR REPLACE FUNCTION "anv".copymodelfieldtoprivateschema()
  RETURNS INTEGER AS
$body$
DECLARE
  sq                  RECORD;

BEGIN

  FOR sq IN (SELECT customlabel, defaultvalue, disableupdate,fieldsetstyle, fieldstyle, field_id, form_id, fullwidth, halfsetstyle, helpmessage, hide, hideincustomizeform, iscustomfield, isentityfield, isworkflowattribute, label, mandatory, nolabelfor, nowrapperfor, place, rowstyle, section, sectionstyle, sorder, source, split, systemdisable, systemmandatory, type, usablebyworkflow, widget, forder, columntype FROM modelfield)
  LOOP
    IF NOT EXISTS(select id from "anv".modelfield where field_id=sq.field_ID and form_id=sq.form_id)
    THEN
      insert into "anv".modelfield(customlabel, defaultvalue, disableupdate,fieldsetstyle, fieldstyle, field_id, form_id, fullwidth, halfsetstyle, helpmessage, hide, hideincustomizeform, iscustomfield, isentityfield, isworkflowattribute, label, mandatory, nolabelfor, nowrapperfor, place, rowstyle, section, sectionstyle, sorder, source, split, systemdisable, systemmandatory, type, usablebyworkflow, widget, forder, columntype) values
      (sq.customlabel, sq.defaultvalue, sq.disableupdate,sq.fieldsetstyle, sq.fieldstyle, sq.field_id, sq.form_id, sq.fullwidth, sq.halfsetstyle, sq.helpmessage, sq.hide, sq.hideincustomizeform, sq.iscustomfield, sq.isentityfield, sq.isworkflowattribute, sq.label, sq.mandatory, sq.nolabelfor, sq.nowrapperfor, sq.place, sq.rowstyle, sq.section, sq.sectionstyle, sq.sorder, sq.source, sq.split, sq.systemdisable, sq.systemmandatory, sq.type, sq.usablebyworkflow, sq.widget, sq.forder, sq.columntype);
    END IF;
  END LOOP;
  RETURN NULL;
END;
$body$
LANGUAGE plpgsql;

ALTER FUNCTION "anv".copymodelfieldtoprivateschema() OWNER TO wfmtest;

UPDATE company SET selectFunctioncolumn =(SELECT "anv".copymodelfieldtoprivateschema()) where  id=(select id from company limit 1);

update "anv".modelfield set fsection=section;




--for scheme 0
DROP FUNCTION IF EXISTS "0".copymodelfieldtoprivateschema();
CREATE OR REPLACE FUNCTION "0".copymodelfieldtoprivateschema()
  RETURNS INTEGER AS
$body$
DECLARE
  sq                  RECORD;

BEGIN

  FOR sq IN (SELECT customlabel, defaultvalue, disableupdate,fieldsetstyle, fieldstyle, field_id, form_id, fullwidth, halfsetstyle, helpmessage, hide, hideincustomizeform, iscustomfield, isentityfield, isworkflowattribute, label, mandatory, nolabelfor, nowrapperfor, place, rowstyle, section, sectionstyle, sorder, source, split, systemdisable, systemmandatory, type, usablebyworkflow, widget, forder, columntype FROM modelfield)
  LOOP
    IF NOT EXISTS(select id from "0".modelfield where field_id=sq.field_ID and form_id=sq.form_id)
    THEN
      insert into "0".modelfield(customlabel, defaultvalue, disableupdate,fieldsetstyle, fieldstyle, field_id, form_id, fullwidth, halfsetstyle, helpmessage, hide, hideincustomizeform, iscustomfield, isentityfield, isworkflowattribute, label, mandatory, nolabelfor, nowrapperfor, place, rowstyle, section, sectionstyle, sorder, source, split, systemdisable, systemmandatory, type, usablebyworkflow, widget, forder, columntype) values
      (sq.customlabel, sq.defaultvalue, sq.disableupdate,sq.fieldsetstyle, sq.fieldstyle, sq.field_id, sq.form_id, sq.fullwidth, sq.halfsetstyle, sq.helpmessage, sq.hide, sq.hideincustomizeform, sq.iscustomfield, sq.isentityfield, sq.isworkflowattribute, sq.label, sq.mandatory, sq.nolabelfor, sq.nowrapperfor, sq.place, sq.rowstyle, sq.section, sq.sectionstyle, sq.sorder, sq.source, sq.split, sq.systemdisable, sq.systemmandatory, sq.type, sq.usablebyworkflow, sq.widget, sq.forder, sq.columntype);
    END IF;
  END LOOP;
  RETURN NULL;
END;
$body$
LANGUAGE plpgsql;

ALTER FUNCTION "0".copymodelfieldtoprivateschema() OWNER TO wfmtest;

UPDATE company SET selectFunctioncolumn =(SELECT "0".copymodelfieldtoprivateschema()) where  id=(select id from company limit 1);

update "0".modelfield set fsection=section;