
DROP function if EXISTS "anv".convertCustomFormToContainer(moduleName text, permission text );
CREATE OR replace function "anv".convertCustomFormToContainer(moduleName text, permission text)
  returns INTEGER AS
$body$
DECLARE  customField record;counter INTEGER; firstContainerId INTEGER; permissionModuleId INTEGER;
BEGIN
  delete from "anv".container_item where id in (select ci.id from "anv".container_item ci left join "anv".property p on ci.propertyID = p.id where p.modulecode = moduleName and p.isCustom is true);
  firstContainerId = (select id from "anv".container where sorder=1 and modulecode = moduleName  limit 1);
  counter = (select max(sorder) from "anv".container_item where moduleCode = moduleName and containerId = firstContainerId);
  permissionModuleId = (select id from "anv".mymodule where code = permission limit 1);
  FOR customField IN (SELECT * FROM "anv".property WHERE modulecode = moduleName and isCustom is true order by  id)
    loop
      INSERT INTO "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode) VALUES
      (permissionModuleId, customField.id, firstContainerId,counter, moduleName);
      counter = counter + 1;
    END loop;
  return NULL;
END;
$body$
  LANGUAGE plpgsql;
ALTER function "anv".convertCustomFormToContainer(moduleName text, permission text) owner TO wfmtest;

