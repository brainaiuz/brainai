INSERT INTO "anv".modelfield (form_id, field_id, columntype, fsection, forder, mandatory, hide)
SELECT 'HRMS_EMPLOYEE_FORM', 'FINGERPRINT_DEVICE', 'COL_1', 'ADDITIONAL_INFORMATION', 5, false, true
WHERE NOT EXISTS (
    SELECT 1 FROM "anv".modelfield
    WHERE form_id = 'HRMS_EMPLOYEE_FORM'
      AND field_id = 'FINGERPRINT_DEVICE'
);