update "anv".financialsettings set
enable_multi_warehouse=(select case when value='YES' then true else false end from "anv".genericsettings where key='MULTIWAREHOUSE_ENABLED');

update "anv".financialsettings set
enable_landed_cost=(select case when value='YES' then true else false end from "anv".genericsettings where key='LANDED_COST');

update "anv".financialsettings set
enable_accounting_department_relation=(select case when value='YES' then true else false end from "anv".genericsettings where key='ACCOUNTING_DEPARTMENT_RELATION_ENABLED');

