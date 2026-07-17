insert into "anv".quick_add_settings(form, settingsJSONData)
values ('TASK', '[
  {
    "code": "PROJECT",
    "required": true,
    "selected": true,
    "order": 0
  },
  {
    "code": "NUMBER",
    "required": false,
    "selected": true,
    "order": 1
  },
  {
    "code": "STATUS",
    "required": false,
    "selected": true,
    "order": 2
  },
  {
    "code": "NAME",
    "required": true,
    "selected": true,
    "order": 3
  },
  {
    "code": "DESCRIPTION",
    "required": false,
    "selected": true,
    "order": 4
  },
  {
    "code": "PERIOD",
    "required": false,
    "selected": true,
    "order": 5
  },
  {
    "code": "ASSIGNEES",
    "required": false,
    "selected": true,
    "order": 6
  }
]');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('QUICK_ADD_SETTINGS', 'SETTINGS', false, 'Quick Add Settings', (select max(sorder) from permission),
        (select id from permission where code = 'SETTINGS_CUSTOMIZATION'), false, 'CORE');

insert into "anv".permission_context(permissioncode, contextcode)
values ('QUICK_ADD_SETTINGS', 'SETTINGS');