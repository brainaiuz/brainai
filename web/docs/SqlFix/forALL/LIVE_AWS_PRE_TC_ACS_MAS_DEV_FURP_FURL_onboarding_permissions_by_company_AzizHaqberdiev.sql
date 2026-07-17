update "anv".rolepermission set permissioncode = concat(permissioncode||'_'||(select companyId from permission where code = permissioncode)) where permissioncode like 'EMPLOYEE_STEP%' AND permissioncode in (select code from permission where modulecode = 'ONBOARDING' and companyId is not null);


update permission set code = concat(code||'_'||companyId) where modulecode = 'ONBOARDING' and companyId is not null and code like 'EMPLOYEE_STEP%';