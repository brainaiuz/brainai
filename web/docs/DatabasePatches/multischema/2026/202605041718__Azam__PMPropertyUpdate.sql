BEGIN;

SELECT setval(
               pg_get_serial_sequence('"anv".property', 'id'),
               (SELECT COALESCE(MAX(id), 1) FROM "anv".property)
       );

DELETE FROM "anv".container_item
WHERE containerId = (SELECT id FROM "anv".container WHERE code = 'project' LIMIT 1);

DELETE FROM "anv".container_item
WHERE propertyID = (SELECT id FROM "anv".property WHERE objectName = 'projectList' LIMIT 1);

DELETE FROM "anv".property WHERE objectName = 'projectList';

UPDATE "anv".property SET modulecode = 'accounting, pm' WHERE objectName = 'EXPENSES_CLAIM';
UPDATE "anv".property SET modulecode = 'accounting, pm' WHERE objectName = 'clientList';

UPDATE "anv".property
SET defaultName='Contracts', singular='Contracts', plural='Contracts',
    shortcut='C', moduleCode='pm', isactive=true
WHERE objectName='contractList';
INSERT INTO "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
SELECT 'contractList', 'Contracts', 'Contracts', 'Contracts', 'C', 'pm', true
WHERE NOT EXISTS (SELECT 1 FROM "anv".property WHERE objectName='contractList');

UPDATE "anv".property
SET defaultName='Resource Utilization', singular='Resource Utilization', plural='Resource Utilization',
    shortcut='RU', moduleCode='pm', isactive=true
WHERE objectName='resourceUtil';
INSERT INTO "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
SELECT 'resourceUtil', 'Resource Utilization', 'Resource Utilization', 'Resource Utilization', 'RU', 'pm', true
WHERE NOT EXISTS (SELECT 1 FROM "anv".property WHERE objectName='resourceUtil');

UPDATE "anv".property
SET defaultName='Monthly Timesheet', singular='Monthly Timesheet', plural='Monthly Timesheet',
    shortcut='MT', moduleCode='pm', isactive=false
WHERE objectName='monthlyTimesheet';
INSERT INTO "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
SELECT 'monthlyTimesheet', 'Monthly Timesheet', 'Monthly Timesheet', 'Monthly Timesheet', 'MT', 'pm', false
WHERE NOT EXISTS (SELECT 1 FROM "anv".property WHERE objectName='monthlyTimesheet');

UPDATE "anv".container
SET defaultName='Projects', moduleCode='pm', sorder=1, changed=false, preparedView='taskList'
WHERE code='project';
INSERT INTO "anv".container (code, defaultName, moduleCode, sorder, changed, preparedView)
SELECT 'project', 'Projects', 'pm', 1, false, 'taskList'
WHERE NOT EXISTS (SELECT 1 FROM "anv".container WHERE code='project');

INSERT INTO "anv".container_item (moduleID, propertyID, containerId, sorder, moduleCode)
VALUES
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='task' LIMIT 1),             (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 1,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='issue' LIMIT 1),            (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 2,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='timesheet' LIMIT 1),        (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 3,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='monthlyTimesheet' LIMIT 1), (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 4,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='timesheetApproval' LIMIT 1),(SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 5,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='project' LIMIT 1),          (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 6,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='clientList' LIMIT 1),       (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 7,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='employee' LIMIT 1),         (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 8,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='bookingItemsList' LIMIT 1), (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 9,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='EXPENSES_CLAIM' LIMIT 1),   (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 10, 'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='contractList' LIMIT 1),     (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 4,  'pm'),
    ((SELECT id FROM "anv".mymodule WHERE code='PM_MODULE' LIMIT 1), (SELECT id FROM "anv".property WHERE objectName='resourceUtil' LIMIT 1),     (SELECT id FROM "anv".container WHERE code='project' LIMIT 1), 4,  'pm');

COMMIT;