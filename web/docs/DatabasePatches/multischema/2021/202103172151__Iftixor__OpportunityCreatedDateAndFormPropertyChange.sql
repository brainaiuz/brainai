


delete from "anv".modelfield where form_id = 'OPPORTUNITY_FORM' and field_id = 'CREATED_DATE';
insert into "anv".modelfield(form_ID,field_ID, label, columntype,fsection,sorder, mandatory,hide,isCustomField,section,defaultValue, widget, systemmandatory,nolabelfor,nowrapperfor,fullWidth, split) values
('OPPORTUNITY_FORM', 'CREATED_DATE',  'Created Date', 'COL_1', 'OPPORTUNITY_INFORMATION',8,false,true ,false,'OPPORTUNITY_INFORMATION','','HTML',  false,'','',false,   false);


update "anv".form_property set settingsjsondata=replace(settingsjsondata, '[{"code":"CRM_OPPORTUNITY_PROBABILITY","title":"Probability (%)","aliasName":"PROBABILITY","changed":false,"required":false,"widget":"TextBox","defaultValue":"","disabled":false}','[{"code":"CRM_OPPORTUNITY_PROBABILITY","title":"Probability (%)","aliasName":"PROBABILITY","changed":false,"required":false,"widget":"TextBox","defaultValue":"","disabled":false},
  {"code": "PROJECT_FIELD",
    "title": "Project",
    "aliasName": "PROJECT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {"code": "CREATED_DATE",
    "title": "Created Date",
    "aliasName": "CREATED_DATE",
    "changed": false,
    "required": false,
    "widget": "HTML",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }') where form_id ='OPPORTUNITY_FORM';