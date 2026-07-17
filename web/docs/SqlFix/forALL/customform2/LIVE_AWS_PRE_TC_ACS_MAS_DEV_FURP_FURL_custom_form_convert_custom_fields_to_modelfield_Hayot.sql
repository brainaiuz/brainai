--functionni schema createda qushib quydim har yangi schemaga urish shartmas

DROP function if EXISTS "anv".convertCustomFieldsToModelFields(modelFormID text, viewName text);
CREATE OR replace function "anv".convertCustomFieldsToModelFields(modelFormID text, viewName text)
  returns INTEGER AS
  $body$
DECLARE  customField record;counter INTEGER;
    BEGIN
            DELETE FROM "anv".model m WHERE m.formid = modelFormID;
            DELETE FROM "anv".modelfield ml WHERE ml.form_id = modelFormID;
            INSERT INTO "anv".model(viewName,title,active, formid) SELECT m.viewName, m.title, m.active, m.formid FROM model m WHERE m.formid = modelFormID;
            INSERT INTO "anv".modelfield(label, customlabel, form_id, field_id, sorder,widget,TYPE, mandatory, systemmandatory,hide,iscustomfield, SECTION, noLabelFor, noWrapperFor,fullWidth,sectionStyle,fieldSetStyle,halfSetStyle,rowStyle,fieldStyle,split, source, usableByWorkflow, disableUpdate)
            SELECT ml.label, ml.customlabel, ml.form_id, ml.field_id, ml.sorder,ml.widget,ml.type, ml.mandatory, ml.systemmandatory,ml.hide,ml.iscustomfield, ml.section, ml.noLabelFor, ml.noWrapperFor, ml.fullWidth, ml.sectionStyle, ml.fieldSetStyle, ml.halfSetStyle, ml.rowStyle, ml.fieldStyle, ml.split, ml.source, ml.usableByWorkflow, ml.disableUpdate FROM modelfield ml WHERE ml.form_id = modelFormID;
            counter = (SELECT MAX(ml.sorder) FROM modelfield ml WHERE ml.form_id = modelFormID) + 1;
            FOR customField IN (SELECT * FROM "anv".companyCustomFieldsSettings WHERE entityname =viewName)
            loop
                INSERT INTO "anv".modelfield(sorder,  label,                  mandatory,                                                              hide,    isCustomField,  defaultValue, systemmandatory,                field_ID,               SECTION,                  widget,TYPE,             form_ID, sectionStyle,fieldSetStyle,halfSetStyle,rowStyle,fieldStyle, usableByWorkflow, source) VALUES
            (counter, customField.fieldname,  customField.isRequired IS NOT NULL AND customField.isRequired IS TRUE,  FALSE,   TRUE,           '',           customField.isRequired,        customField.columncode, 'ADDITIONAL_INFORMATION', customField.uitype, customField.dataType, modelFormID,'slideDown-box  group expand hideCustomField','slideDown-content group labelLine','halfSet-1 left','row hideCustomField','field', customField.uiType in ('TextBox','TextArea','TextArea2','DropDown','DatePicker','RadioButton','CheckBox'), (CASE WHEN customField.uiType in ('DropDown','CheckBox','RadioButton') THEN customField.predefinedValues ELSE null END));
                counter = counter + 1;
            END loop;
    return NULL;
    END;
$body$
LANGUAGE plpgsql;
ALTER function "anv".convertCustomFieldsToModelFields(modelFormID text, viewName text) owner TO wfmtest;