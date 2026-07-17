update "anv".modelfield set mandatory = false where field_id = 'TYPE' and form_id = 'SHIFT_FORM';
delete  from "anv".form_property where form_id ='SHIFT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('SHIFT_FORM',
        '[
          {
            "code": "monthPicker",
            "title": "Period",
            "aliasName": "monthPicker",
            "changed": false,
            "required": false,
            "widget": "DatePicker",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
          },
          {
            "code": "TYPE",
            "title": "Type",
            "aliasName": "TYPE",
            "changed": false,
            "required": false,
            "widget": "LOOKUP",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
          },
		  {
            "code": "DEPARTMENT",
            "title": "Department",
            "aliasName": "DEPARTMENT",
            "changed": false,
            "required": false,
            "widget": "LOOKUP",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
          },
          {
            "code": "APPROVERS",
            "title": "Approvers",
            "aliasName": "APPROVERS",
            "changed": false,
            "required": false,
            "widget": "UNKNOWN",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
          },
          {
            "code": "MANAGER",
            "title": "Manager",
            "aliasName": "MANAGER",
            "changed": false,
            "required": false,
            "widget": "UNKNOWN",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
          },
          {
            "code": "BACKUP_MANAGER",
            "title": "Backup Manager(s)",
            "aliasName": "BACKUP_MANAGER",
            "changed": false,
            "required": false,
            "widget": "UNKNOWN",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
          }
        ]')