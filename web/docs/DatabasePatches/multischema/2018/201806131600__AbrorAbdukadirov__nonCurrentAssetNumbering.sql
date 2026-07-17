update company set selectFunctioncolumn =(select setval('"anv".accountnumbersettings_id_seq', (select max(id) from "anv".accountnumbersettings))) where  id=(select id from company limit 1);
do $$
    begin
        if exists (select id from accountType where code = 'NON_CURRENT_ASSET' and category = 'ASSETS' LIMIT 1)
        and (select startnumber from "anv".accountnumbersettings where accounttype_id = (select id from accounttype
                                                                                          where code = 'NON_CURRENT_ASSET'
                                                                                          and category = 'ASSETS')) is null
        then
            insert into "anv".accountnumbersettings(startnumber, endnumber, accounttype_id) values
            (1300, 1499, (select id from accountType where code = 'NON_CURRENT_ASSET' and category = 'ASSETS'));
        end if;
end$$;

