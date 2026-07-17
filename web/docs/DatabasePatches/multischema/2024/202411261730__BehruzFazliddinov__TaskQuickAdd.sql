delete from "anv".quick_add_settings where form = 'TASK';
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



delete from "0".quick_add_settings where form = 'TASK';
insert into "0".quick_add_settings(form, settingsJSONData)
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