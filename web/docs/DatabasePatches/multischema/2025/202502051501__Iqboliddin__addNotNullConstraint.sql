UPDATE "anv".modelfield
SET gridX = 0
WHERE gridX IS NULL;

UPDATE "anv".modelfield
SET gridY = 0
WHERE gridY IS NULL;

UPDATE "anv".modelfield
SET gridWidth = 4
WHERE gridWidth IS NULL;

UPDATE "anv".modelfield
SET gridHeight = 1
WHERE gridHeight IS NULL;

UPDATE "anv".modelfield
SET customizableTable = false
WHERE customizableTable IS NULL;

UPDATE "anv".modelfield
SET isWorkflowAttribute = false
WHERE isWorkflowAttribute IS NULL;

UPDATE "anv".modelfield
SET systemDisable = false
WHERE systemDisable IS NULL;

UPDATE "anv".modelfield
SET hideInCustomizeForm = false
WHERE hideInCustomizeForm IS NULL;

UPDATE "anv".modelfield
SET usableByWorkflow = false
WHERE usableByWorkflow IS NULL;

UPDATE "anv".modelfield
SET disableUpdate = false
WHERE disableUpdate IS NULL;

UPDATE "anv".modelfield
SET isEntityField = false
WHERE isEntityField IS NULL;

UPDATE "anv".modelfield
SET fullWidth = false
WHERE fullWidth IS NULL;

UPDATE "anv".modelfield
SET hide = false
WHERE hide IS NULL;

UPDATE "anv".modelfield
SET isCustomField = false
WHERE isCustomField IS NULL;

UPDATE "anv".modelfield
SET systemmandatory = false
WHERE systemmandatory IS NULL;

UPDATE "anv".modelfield
SET mandatory = false
WHERE mandatory IS NULL;

UPDATE "anv".modelfield
SET split = false
WHERE split IS NULL;

UPDATE "anv".reference
SET deleted = false
WHERE deleted IS NULL;

UPDATE "anv".reference
SET hasProrata = false
WHERE hasProrata IS NULL;

UPDATE "anv".reference
SET isActive = true
WHERE isActive IS NULL;

UPDATE "anv".reference
SET isCustomButton = false
WHERE isCustomButton IS NULL;

UPDATE "anv".reference
SET requiredComment = false
WHERE requiredComment IS NULL;

UPDATE "anv".reference
SET shared = true
WHERE shared IS NULL;

UPDATE "anv".reference
SET changed = false
WHERE changed IS NULL;

UPDATE "anv".reference
SET isRemovable = true
WHERE isRemovable IS NULL;

UPDATE "anv".reference
SET isSystemReference = false
WHERE isSystemReference IS NULL;

UPDATE "0".modelfield
SET gridX = 0
WHERE gridX IS NULL;

UPDATE "0".modelfield
SET gridY = 0
WHERE gridY IS NULL;

UPDATE "0".modelfield
SET gridWidth = 4
WHERE gridWidth IS NULL;

UPDATE "0".modelfield
SET gridHeight = 1
WHERE gridHeight IS NULL;

UPDATE "0".modelfield
SET customizableTable = false
WHERE customizableTable IS NULL;

UPDATE "0".modelfield
SET isWorkflowAttribute = false
WHERE isWorkflowAttribute IS NULL;

UPDATE "0".modelfield
SET systemDisable = false
WHERE systemDisable IS NULL;

UPDATE "0".modelfield
SET hideInCustomizeForm = false
WHERE hideInCustomizeForm IS NULL;

UPDATE "0".modelfield
SET usableByWorkflow = false
WHERE usableByWorkflow IS NULL;

UPDATE "0".modelfield
SET disableUpdate = false
WHERE disableUpdate IS NULL;

UPDATE "0".modelfield
SET isEntityField = false
WHERE isEntityField IS NULL;

UPDATE "0".modelfield
SET fullWidth = false
WHERE fullWidth IS NULL;

UPDATE "0".modelfield
SET hide = false
WHERE hide IS NULL;

UPDATE "0".modelfield
SET isCustomField = false
WHERE isCustomField IS NULL;

UPDATE "0".modelfield
SET systemmandatory = false
WHERE systemmandatory IS NULL;

UPDATE "0".modelfield
SET mandatory = false
WHERE mandatory IS NULL;

UPDATE "0".modelfield
SET split = false
WHERE split IS NULL;

UPDATE "0".reference
SET deleted = false
WHERE deleted IS NULL;

UPDATE "0".reference
SET hasProrata = false
WHERE hasProrata IS NULL;

UPDATE "0".reference
SET isActive = true
WHERE isActive IS NULL;

UPDATE "0".reference
SET isCustomButton = false
WHERE isCustomButton IS NULL;

UPDATE "0".reference
SET requiredComment = false
WHERE requiredComment IS NULL;

UPDATE "0".reference
SET shared = true
WHERE shared IS NULL;

UPDATE "0".reference
SET changed = false
WHERE changed IS NULL;

UPDATE "0".reference
SET isRemovable = true
WHERE isRemovable IS NULL;

UPDATE "0".reference
SET isSystemReference = false
WHERE isSystemReference IS NULL;

ALTER TABLE "anv".modelfield
    ALTER COLUMN gridX SET NOT NULL,
    ALTER COLUMN gridX SET DEFAULT '0',
    ALTER COLUMN gridY SET NOT NULL,
    ALTER COLUMN gridY SET DEFAULT '0',
    ALTER COLUMN gridWidth SET NOT NULL,
    ALTER COLUMN gridWidth SET DEFAULT '4',
    ALTER COLUMN customizableTable SET NOT NULL,
    ALTER COLUMN customizableTable SET DEFAULT 'false',
    ALTER COLUMN isWorkflowAttribute SET NOT NULL,
    ALTER COLUMN isWorkflowAttribute SET DEFAULT 'false',
    ALTER COLUMN systemDisable SET NOT NULL,
    ALTER COLUMN systemDisable SET DEFAULT 'false',
    ALTER COLUMN hideInCustomizeForm SET NOT NULL,
    ALTER COLUMN hideInCustomizeForm SET DEFAULT 'false',
    ALTER COLUMN usableByWorkflow SET NOT NULL,
    ALTER COLUMN usableByWorkflow SET DEFAULT 'false',
    ALTER COLUMN disableUpdate SET NOT NULL,
    ALTER COLUMN disableUpdate SET DEFAULT 'false',
    ALTER COLUMN isEntityField SET NOT NULL,
    ALTER COLUMN isEntityField SET DEFAULT 'false',
    ALTER COLUMN fullWidth SET NOT NULL,
    ALTER COLUMN fullWidth SET DEFAULT 'false',
    ALTER COLUMN hide SET NOT NULL,
    ALTER COLUMN hide SET DEFAULT 'false',
    ALTER COLUMN isCustomField SET NOT NULL,
    ALTER COLUMN isCustomField SET DEFAULT 'false',
    ALTER COLUMN systemmandatory SET NOT NULL,
    ALTER COLUMN systemmandatory SET DEFAULT 'false',
    ALTER COLUMN mandatory SET NOT NULL,
    ALTER COLUMN mandatory SET DEFAULT 'false',
    ALTER COLUMN split SET NOT NULL,
    ALTER COLUMN split SET DEFAULT 'false',
    ALTER COLUMN gridHeight SET NOT NULL,
    ALTER COLUMN gridHeight SET DEFAULT '1';

ALTER TABLE "0".modelfield
    ALTER COLUMN gridX SET NOT NULL,
    ALTER COLUMN gridX SET DEFAULT '0',
    ALTER COLUMN gridY SET NOT NULL,
    ALTER COLUMN gridY SET DEFAULT '0',
    ALTER COLUMN gridWidth SET NOT NULL,
    ALTER COLUMN gridWidth SET DEFAULT '4',
    ALTER COLUMN customizableTable SET NOT NULL,
    ALTER COLUMN customizableTable SET DEFAULT 'false',
    ALTER COLUMN isWorkflowAttribute SET NOT NULL,
    ALTER COLUMN isWorkflowAttribute SET DEFAULT 'false',
    ALTER COLUMN systemDisable SET NOT NULL,
    ALTER COLUMN systemDisable SET DEFAULT 'false',
    ALTER COLUMN hideInCustomizeForm SET NOT NULL,
    ALTER COLUMN hideInCustomizeForm SET DEFAULT 'false',
    ALTER COLUMN usableByWorkflow SET NOT NULL,
    ALTER COLUMN usableByWorkflow SET DEFAULT 'false',
    ALTER COLUMN disableUpdate SET NOT NULL,
    ALTER COLUMN disableUpdate SET DEFAULT 'false',
    ALTER COLUMN isEntityField SET NOT NULL,
    ALTER COLUMN isEntityField SET DEFAULT 'false',
    ALTER COLUMN fullWidth SET NOT NULL,
    ALTER COLUMN fullWidth SET DEFAULT 'false',
    ALTER COLUMN hide SET NOT NULL,
    ALTER COLUMN hide SET DEFAULT 'false',
    ALTER COLUMN isCustomField SET NOT NULL,
    ALTER COLUMN isCustomField SET DEFAULT 'false',
    ALTER COLUMN systemmandatory SET NOT NULL,
    ALTER COLUMN systemmandatory SET DEFAULT 'false',
    ALTER COLUMN mandatory SET NOT NULL,
    ALTER COLUMN mandatory SET DEFAULT 'false',
    ALTER COLUMN split SET NOT NULL,
    ALTER COLUMN split SET DEFAULT 'false',
    ALTER COLUMN gridHeight SET NOT NULL,
    ALTER COLUMN gridHeight SET DEFAULT '1';

ALTER TABLE "anv".reference
    ALTER COLUMN deleted SET NOT NULL,
    ALTER COLUMN deleted SET DEFAULT 'false',
    ALTER COLUMN hasProrata SET NOT NULL,
    ALTER COLUMN hasProrata SET DEFAULT 'false',
    ALTER COLUMN isActive SET NOT NULL,
    ALTER COLUMN isActive SET DEFAULT 'true',
    ALTER COLUMN requiredComment SET NOT NULL,
    ALTER COLUMN requiredComment SET DEFAULT 'false',
    ALTER COLUMN shared SET NOT NULL,
    ALTER COLUMN shared SET DEFAULT 'true',
    ALTER COLUMN changed SET NOT NULL,
    ALTER COLUMN changed SET DEFAULT 'false',
    ALTER COLUMN isSystemReference SET NOT NULL,
    ALTER COLUMN isSystemReference SET DEFAULT 'false',
    ALTER COLUMN isRemovable SET NOT NULL,
    ALTER COLUMN isRemovable SET DEFAULT 'true',
    ALTER COLUMN isCustomButton SET NOT NULL,
    ALTER COLUMN isCustomButton SET DEFAULT 'false';

ALTER TABLE "0".reference
    ALTER COLUMN deleted SET NOT NULL,
    ALTER COLUMN deleted SET DEFAULT 'false',
    ALTER COLUMN hasProrata SET NOT NULL,
    ALTER COLUMN hasProrata SET DEFAULT 'false',
    ALTER COLUMN isActive SET NOT NULL,
    ALTER COLUMN isActive SET DEFAULT 'true',
    ALTER COLUMN requiredComment SET NOT NULL,
    ALTER COLUMN requiredComment SET DEFAULT 'false',
    ALTER COLUMN shared SET NOT NULL,
    ALTER COLUMN shared SET DEFAULT 'true',
    ALTER COLUMN changed SET NOT NULL,
    ALTER COLUMN changed SET DEFAULT 'false',
    ALTER COLUMN isSystemReference SET NOT NULL,
    ALTER COLUMN isSystemReference SET DEFAULT 'false',
    ALTER COLUMN isRemovable SET NOT NULL,
    ALTER COLUMN isRemovable SET DEFAULT 'true',
    ALTER COLUMN isCustomButton SET NOT NULL,
    ALTER COLUMN isCustomButton SET DEFAULT 'false';