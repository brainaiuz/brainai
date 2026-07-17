DROP function if EXISTS "anv".insertNumberOfWidgetForExistings();
CREATE OR replace function "anv".insertNumberOfWidgetForExistings()
    returns INTEGER AS
$body$
DECLARE  dashboard record;
BEGIN

    FOR dashboard IN (SELECT * FROM "anv".module_dashboards order by id)
        loop
       update "anv".module_dashboards set numberofwidgets = (case when (select count(*) from "anv".dashboard_components where dashboard_id = dashboard.id) > 9 then (select count(*) from "anv".dashboard_components where dashboard_id = dashboard.id) else 9 end ) where id = dashboard.id;
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".insertNumberOfWidgetForExistings() owner TO wfmtest;
UPDATE company SET selectFunctioncolumn =(SELECT "anv".insertNumberOfWidgetForExistings()) WHERE  id=(SELECT id FROM company LIMIT 1);