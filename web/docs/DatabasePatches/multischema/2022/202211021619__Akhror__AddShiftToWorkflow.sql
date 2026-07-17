insert into "anv".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_SHIFT', 'Shift', (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
        (select id from "anv".reference where code = '_WORKFLOW_MODULE'));