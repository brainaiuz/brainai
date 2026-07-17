update "anv".modelfield set usableByWorkflow = true, disableUpdate = true where isCustomField = true and widget = 'CheckBox';

update "anv".modelfield m set source = (select cf.predefinedValues from "anv".companyCustomFieldsSettings cf where columncode = m.field_id
and entityname = (select viewName from model where formid = m.form_id))
where isCustomField = true and widget = 'CheckBox';