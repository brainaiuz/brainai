UPDATE "anv".modelfield
SET disableupdate       = false,
    fullwidth           = false,
    helpmessage         = '',
    hideincustomizeform = false,
    iscustomfield       = false,
    isentityfield       = false,
    isworkflowattribute = false,
    place=0,
    widget='DropDown',
    type='text',
    usablebyworkflow= false,
    section             = 'CS_COMPANY_SETTINGS',
    sorder              = (SELECT MAX(sorder) + 1 FROM "anv".modelfield where form_id = 'COMPANY_SETTINGS_FORM'),
    split               = false,
    systemdisable       = false,
    systemmandatory     = false,
    customizabletable   = false,
    gridheight          = 1,
    gridwidth           = 4,
    gridx               = 0,
    gridy               = 0
WHERE field_id = 'NAME_FORMAT'
  and form_id = 'COMPANY_SETTINGS_FORM';
