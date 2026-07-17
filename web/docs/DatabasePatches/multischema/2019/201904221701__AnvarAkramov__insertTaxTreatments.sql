insert into "0".reference (code, name, isremovable, issystemreference) values ('_TAX_TREATMENT', 'Tax Treatments', false, true);

insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('VAT_REGISTERED', 'Vat Registered',          (select id from "0".reference r where r.code = '_TAX_TREATMENT'), 1, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('NON_VAT_REGISTERED', 'Non VAT Registered', (select id from "0".reference r where r.code = '_TAX_TREATMENT'), 2, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('GCC_VAT_REGISTERED', 'GCC VAT Registered',    (select id from "0".reference r where r.code = '_TAX_TREATMENT'), 3, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('GCC_NON_VAT_REGISTERED', 'GCC Non VAT Registered',          (select id from "0".reference r where r.code = '_TAX_TREATMENT'), 4, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('NON_GCC', 'Non GCC', (select id from "0".reference r where r.code = '_TAX_TREATMENT'), 5, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('VAT_REGISTERED_DESIGNATED_ZONE', 'Vat Registered - Designated Zone',          (select id from "0".reference r where r.code = '_TAX_TREATMENT'), 6, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('NON_VAT_REGISTERED_DESIGNATED_ZONE', 'Non Vat Registered - Designated Zone',    (select id from "0".reference r where r.code = '_TAX_TREATMENT'), 7, false, true);



insert into "anv".reference (code, name, isremovable, issystemreference) values ('_TAX_TREATMENT', 'Tax Treatments', false, true);

insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('VAT_REGISTERED', 'Vat Registered',          (select id from "anv".reference r where r.code = '_TAX_TREATMENT'), 1, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('NON_VAT_REGISTERED', 'Non VAT Registered', (select id from "anv".reference r where r.code = '_TAX_TREATMENT'), 2, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('GCC_VAT_REGISTERED', 'GCC VAT Registered',    (select id from "anv".reference r where r.code = '_TAX_TREATMENT'), 3, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('GCC_NON_VAT_REGISTERED', 'GCC Non VAT Registered',          (select id from "anv".reference r where r.code = '_TAX_TREATMENT'), 4, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('NON_GCC', 'Non GCC', (select id from "anv".reference r where r.code = '_TAX_TREATMENT'), 5, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('VAT_REGISTERED_DESIGNATED_ZONE', 'Vat Registered - Designated Zone',          (select id from "anv".reference r where r.code = '_TAX_TREATMENT'), 6, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('NON_VAT_REGISTERED_DESIGNATED_ZONE', 'Non Vat Registered - Designated Zone',    (select id from "anv".reference r where r.code = '_TAX_TREATMENT'), 7, false, true);


insert into modelfield(form_ID,     field_ID, label, columntype,fsection,                                  sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_TAX_TREATMENT',  'Tax Treatment', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                       7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'DropDown',  false,                     '',                            '',                   false,   false);

insert into modelfield(form_ID,     field_ID, label, columntype,fsection,                                   sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_PLACEOFSUPPLY_COUNTRY',  'Place Of Supply', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                         7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'DropDown',  false,                     '',                            '',                   false,   false);

insert into modelfield(form_ID,     field_ID, label, columntype,fsection,                                   sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_PLACEOFSUPPLY_STATE',  'Place Of Supply', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                         7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'DropDown',  false,                     '',                            '',                   false,   false);

insert into modelfield(form_ID,     field_ID, label, columntype,fsection,                                   sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_TRN',  'Tax Registration Number (TRN)', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                         7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'TextBox',  false,                     '',                            '',                   false,   false);



insert into "0".modelfield(form_ID,     field_ID, label, columntype,fsection,                                  sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_TAX_TREATMENT',  'Tax Treatment', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                       7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'DropDown',  false,                     '',                            '',                   false,   false);

insert into "0".modelfield(form_ID,     field_ID, label, columntype,fsection,                                   sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_PLACEOFSUPPLY_COUNTRY',  'Place Of Supply', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                         7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'DropDown',  false,                     '',                            '',                   false,   false);

insert into "0".modelfield(form_ID,     field_ID, label, columntype,fsection,                                   sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_PLACEOFSUPPLY_STATE',  'Place Of Supply', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                         7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'DropDown',  false,                     '',                            '',                   false,   false);

insert into "0".modelfield(form_ID,     field_ID, label, columntype,fsection,                                   sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_TRN',  'Tax Registration Number (TRN)', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                         7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'TextBox',  false,                     '',                            '',                   false,   false);


insert into "anv".modelfield(form_ID,     field_ID, label, columntype,fsection,                                  sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_TAX_TREATMENT',  'Tax Treatment', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                       7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'DropDown',  false,                     '',                            '',                   false,   false);

insert into "anv".modelfield(form_ID,     field_ID, label, columntype,fsection,                                   sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_PLACEOFSUPPLY_COUNTRY',  'Place Of Supply', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                         7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'DropDown',  false,                     '',                            '',                   false,   false);

insert into "anv".modelfield(form_ID,     field_ID, label, columntype,fsection,                                   sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_PLACEOFSUPPLY_STATE',  'Place Of Supply', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                         7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'DropDown',  false,                     '',                            '',                   false,   false);

insert into "anv".modelfield(form_ID,     field_ID, label, columntype,fsection,                                   sorder, mandatory,  hide,   isCustomField,         section,                     defaultValue, widget, systemmandatory,             nolabelfor,                   nowrapperfor,            fullWidth, split) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_TRN',  'Tax Registration Number (TRN)', 'COL_2', 'CRM_ACCOUNT_INFORMATION',                         7,      false,   false,       false,   'CRM_ACCOUNT_INFORMATION',                  '',    'TextBox',  false,                     '',                            '',                   false,   false);



