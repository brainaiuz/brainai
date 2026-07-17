

DROP function if EXISTS "anv".convertCustomFieldsToModelFields(modelFormID text, viewName text);
CREATE OR replace function "anv".convertCustomFieldsToModelFields(modelFormID text, viewName text)
  returns INTEGER AS
$body$
DECLARE  customField record;counter INTEGER;
BEGIN
  PERFORM ml.label, ml.form_id, ml.field_id, ml.widget, ml.type, ml.mandatory, ml.systemmandatory, ml.hide, ml.iscustomfield, ml.noLabelFor, ml.noWrapperFor, ml.usableByWorkflow, ml.columntype, ml.forder, ml.fsection FROM modelfield ml WHERE ml.form_id = modelFormID;
  delete from "anv".modelfield where iscustomfield is true and form_id=modelFormID;
  counter=0;
  FOR customField IN (SELECT * FROM "anv".companyCustomFieldsSettings WHERE entityname =viewName order by  creationdate)
    loop
      INSERT INTO "anv".modelfield(label,mandatory,hide,isCustomField, defaultValue, systemmandatory, field_ID, widget, TYPE, form_ID, usableByWorkflow, columntype, forder, fsection) VALUES
      (customField.fieldname, customField.isRequired IS NOT NULL AND customField.isRequired IS TRUE,  FALSE,   TRUE,'', customField.isRequired,        customField.columncode,  customField.uitype, customField.dataType, modelFormID, customField.uiType in ('TextBox','TextArea','TextArea2','DropDown','DatePicker','RadioButton','CheckBox'), 'COL_1', counter, 'ADDITIONAL_INFORMATION');
      counter = counter + 1;
    END loop;
  return NULL;
END;
$body$
  LANGUAGE plpgsql;
ALTER function "anv".convertCustomFieldsToModelFields(modelFormID text, viewName text) owner TO wfmtest;