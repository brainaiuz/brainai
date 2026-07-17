delete
from "anv".rolepermission
where permissioncode in ('CETIFICATE_OF_EMPLOYMENT_DELETE', 'CETIFICATE_OF_EMPLOYMENT_EDIT', 'HRMS_CERTIFICATES_PDF');
delete
from "anv".permission_context
where permissioncode in ('CETIFICATE_OF_EMPLOYMENT_DELETE', 'CETIFICATE_OF_EMPLOYMENT_EDIT', 'HRMS_CERTIFICATES_PDF');
delete
from permission
where code in ('CETIFICATE_OF_EMPLOYMENT_DELETE', 'CETIFICATE_OF_EMPLOYMENT_EDIT', 'HRMS_CERTIFICATES_PDF');