update permission
set parent=(select id from permission where code = 'HRMS_MAIN_MENU')
where code = 'MROT_FORM_300205'
  and companyid = 300205;

update permission
set parent=(select id from permission where code = 'HRMS_MAIN_MENU')
where code = 'EDINAYA_TARIFNAYA_SETKA_FORM_300205'
  and companyid = 300205;

update permission
set parent=(select id from permission where code = 'HRMS_MAIN_MENU')
where code = 'ROTACII_(PO_DOLZHNOSTI)_FORM_300205'
  and companyid = 300205;