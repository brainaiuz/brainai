DO $$
DECLARE
    company RECORD;
BEGIN
    FOR company IN (SELECT c.id,c.name FROM company c inner join (SELECT nspname as id FROM pg_namespace WHERE nspname ~ '^[0-9]+$' ORDER BY nspname) sch on sch.id = trim(to_char(c.id, '999999')) where c.active is true and c.isdeleted is not true order by id)
    LOOP
        EXECUTE 'insert into "'||company.id||'".businessevent(issuperuser, additionalsourceid, attempts, chatregistr, companyid, createdbyid, customstringfield, entityid, entityids, entitytype, eventtype, isrbacindexed, myupdatesitemadd, myupdatesitemdelete, myupdatesitemedit, processfailed, processed, processorname, relationid, relationids, relationtype, sendemailnotification, sendmail1, sendmail2, solrindexed, sorder, sourceid, status, "time") ' ||
         ' select issuperuser, additionalsourceid, attempts, chatregistr, companyid, createdbyid, customstringfield, entityid, entityids, entitytype, eventtype, isrbacindexed, myupdatesitemadd, myupdatesitemdelete, myupdatesitemedit, false, processed, processorname, relationid, relationids, relationtype, sendemailnotification, sendmail1, sendmail2, solrindexed, 0, sourceid, status, "time" from businessevent b where b.processed = false and b.attempts = 0 and b.companyid = $1'
        USING company.id;

    END LOOP;
END $$;