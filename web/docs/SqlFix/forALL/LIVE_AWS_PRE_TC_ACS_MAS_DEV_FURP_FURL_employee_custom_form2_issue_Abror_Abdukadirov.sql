--=========== for PUBLIC
update modelfield set sorder = (select max(sorder) + 1 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_SICK_LEAVE';
update modelfield set sorder = (select max(sorder) + 2 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_STUDY_LEAVE';
update modelfield set sorder = (select max(sorder) + 3 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_ANNUAL_LEAVE';
update modelfield set sorder = (select max(sorder) + 4 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_LATE';
update modelfield set sorder = (select max(sorder) + 5 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_SPECIAL';
update modelfield set sorder = (select max(sorder) + 6 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_UNAUTHORIZED_LEAVE';
update modelfield set sorder = (select max(sorder) + 7 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_OTHER_LEAVE';
update modelfield set sorder = (select max(sorder) + 8 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'YEAR_LEAVE_DURATION';
update modelfield set sorder = (select max(sorder) + 9 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'YEAR_LEAVE_HOUR_DURATION';
update modelfield set sorder = (select max(sorder) + 10 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'APPLY_ALLOUNCE_ALL_EMPLOYEE';

update modelfield set sorder = (select max(sorder) + 11 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'INTERNAL_EMPLOYMENT';
update modelfield set sorder = (select max(sorder) + 12 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'PAST_EMPLOYMENT';
update modelfield set sorder = (select max(sorder) + 13 from modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'PUNISHMENT_PROMOTION';

update modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id = 'HRMS_EMPLOYEE_FORM';
update modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id = 'HRMS_EMPLOYEE_FORM';
update modelfield set fieldSetStyle = 'slideDown-content group nobrd' where noLabelFor is not null and noLabelFor != '' and form_id = 'HRMS_EMPLOYEE_FORM' and section != 'ADDRESS_INFORMATION';
update modelfield set halfSetStyle = 'halfSet-1 left' where form_id = 'HRMS_EMPLOYEE_FORM';
update modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id = 'HRMS_EMPLOYEE_FORM' and section != 'ADDRESS_INFORMATION';
update modelfield set rowStyle = 'row hideCustomField' where form_id = 'HRMS_EMPLOYEE_FORM';
update modelfield set fieldStyle = 'field' where form_id = 'HRMS_EMPLOYEE_FORM';

--=========== for ZERO
update "0".modelfield set sorder = (select max(sorder) + 1 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_SICK_LEAVE';
update "0".modelfield set sorder = (select max(sorder) + 2 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_STUDY_LEAVE';
update "0".modelfield set sorder = (select max(sorder) + 3 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_ANNUAL_LEAVE';
update "0".modelfield set sorder = (select max(sorder) + 4 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_LATE';
update "0".modelfield set sorder = (select max(sorder) + 5 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_SPECIAL';
update "0".modelfield set sorder = (select max(sorder) + 6 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_UNAUTHORIZED_LEAVE';
update "0".modelfield set sorder = (select max(sorder) + 7 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_OTHER_LEAVE';
update "0".modelfield set sorder = (select max(sorder) + 8 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'YEAR_LEAVE_DURATION';
update "0".modelfield set sorder = (select max(sorder) + 9 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'YEAR_LEAVE_HOUR_DURATION';
update "0".modelfield set sorder = (select max(sorder) + 10 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'APPLY_ALLOUNCE_ALL_EMPLOYEE';

update "0".modelfield set sorder = (select max(sorder) + 11 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'INTERNAL_EMPLOYMENT';
update "0".modelfield set sorder = (select max(sorder) + 12 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'PAST_EMPLOYMENT';
update "0".modelfield set sorder = (select max(sorder) + 13 from "0".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'PUNISHMENT_PROMOTION';

update "0".modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id = 'HRMS_EMPLOYEE_FORM';
update "0".modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id = 'HRMS_EMPLOYEE_FORM';
update "0".modelfield set fieldSetStyle = 'slideDown-content group nobrd' where noLabelFor is not null and noLabelFor != '' and form_id = 'HRMS_EMPLOYEE_FORM' and section != 'ADDRESS_INFORMATION';
update "0".modelfield set halfSetStyle = 'halfSet-1 left' where form_id = 'HRMS_EMPLOYEE_FORM';
update "0".modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id = 'HRMS_EMPLOYEE_FORM' and section != 'ADDRESS_INFORMATION';
update "0".modelfield set rowStyle = 'row hideCustomField' where form_id = 'HRMS_EMPLOYEE_FORM';
update "0".modelfield set fieldStyle = 'field' where form_id = 'HRMS_EMPLOYEE_FORM';

--=========== for PRIVATE
update "anv".modelfield set sorder = (select max(sorder) + 1 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_SICK_LEAVE';
update "anv".modelfield set sorder = (select max(sorder) + 2 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_STUDY_LEAVE';
update "anv".modelfield set sorder = (select max(sorder) + 3 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_ANNUAL_LEAVE';
update "anv".modelfield set sorder = (select max(sorder) + 4 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_LATE';
update "anv".modelfield set sorder = (select max(sorder) + 5 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_SPECIAL';
update "anv".modelfield set sorder = (select max(sorder) + 6 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_UNAUTHORIZED_LEAVE';
update "anv".modelfield set sorder = (select max(sorder) + 7 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'LR_TYPE_OTHER_LEAVE';
update "anv".modelfield set sorder = (select max(sorder) + 8 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'YEAR_LEAVE_DURATION';
update "anv".modelfield set sorder = (select max(sorder) + 9 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'YEAR_LEAVE_HOUR_DURATION';
update "anv".modelfield set sorder = (select max(sorder) + 10 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), section = 'WORKFLOW_FIELDS', hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'APPLY_ALLOUNCE_ALL_EMPLOYEE';

update "anv".modelfield set sorder = (select max(sorder) + 11 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'INTERNAL_EMPLOYMENT';
update "anv".modelfield set sorder = (select max(sorder) + 12 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'PAST_EMPLOYMENT';
update "anv".modelfield set sorder = (select max(sorder) + 13 from "anv".modelfield where form_id = 'HRMS_EMPLOYEE_FORM'), hideInCustomizeForm = false, isEntityField = true where form_id = 'HRMS_EMPLOYEE_FORM' and field_id = 'PUNISHMENT_PROMOTION';

update "anv".modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id = 'HRMS_EMPLOYEE_FORM';
update "anv".modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id = 'HRMS_EMPLOYEE_FORM';
update "anv".modelfield set fieldSetStyle = 'slideDown-content group nobrd' where noLabelFor is not null and noLabelFor != '' and form_id = 'HRMS_EMPLOYEE_FORM' and section != 'ADDRESS_INFORMATION';
update "anv".modelfield set halfSetStyle = 'halfSet-1 left' where form_id = 'HRMS_EMPLOYEE_FORM';
update "anv".modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id = 'HRMS_EMPLOYEE_FORM' and section != 'ADDRESS_INFORMATION';
update "anv".modelfield set rowStyle = 'row hideCustomField' where form_id = 'HRMS_EMPLOYEE_FORM';
update "anv".modelfield set fieldStyle = 'field' where form_id = 'HRMS_EMPLOYEE_FORM';