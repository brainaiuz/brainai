UPDATE "0".customformsection
SET expanded= true
WHERE form_id = 'WORKFLOW_TELEGRAM_ALERT_FORM'
  AND (section = 'CONTENT' OR section = 'INFORMATION');

UPDATE "anv".customformsection
SET expanded= true
WHERE form_id = 'WORKFLOW_TELEGRAM_ALERT_FORM'
  AND (section = 'CONTENT' OR section = 'INFORMATION');