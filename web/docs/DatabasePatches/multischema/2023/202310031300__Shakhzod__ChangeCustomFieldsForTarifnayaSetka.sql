update "311555".modelfield
set usablebyworkflow = false
where field_id = 'string_value1'
  and form_id = 'TARIFNAJA_SETKA_FORM';
delete
from "311555".companyCustomFieldsSettings
where id = 175;