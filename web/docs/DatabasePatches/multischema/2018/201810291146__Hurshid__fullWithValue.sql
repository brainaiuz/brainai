

update "anv".modelfield set fullWidth=false where fullWidth is null and form_id in ('CLIENT_FORM', 'ACCOUNT_FORM');