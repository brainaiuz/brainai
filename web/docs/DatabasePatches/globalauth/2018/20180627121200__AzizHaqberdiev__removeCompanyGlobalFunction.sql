CREATE OR REPLACE FUNCTION removeCompanyGlobalFunc(cid text)
	RETURNS void
LANGUAGE plpgsql
AS $function$
BEGIN
	RAISE NOTICE 'DELETE FROM usercompany WHERE id IN (SELECT (uc.id) FROM usercompany uc INNER JOIN clustercompany cc ON cc.id = uc.clustercompanyid WHERE cc.companyid IN (%))',cid;
	EXECUTE('DELETE FROM usercompany WHERE id IN (SELECT (uc.id) FROM usercompany uc INNER JOIN clustercompany cc ON cc.id = uc.clustercompanyid WHERE cc.companyid IN (' || cid || '));');
	RAISE NOTICE 'DELETE FROM googlegadgetauth WHERE userauthid IN (SELECT id FROM userauth WHERE id NOT IN (SELECT authid FROM usercompany))';
	EXECUTE('DELETE FROM googlegadgetauth WHERE userauthid IN (SELECT id FROM userauth WHERE id NOT IN (SELECT authid FROM usercompany));');
	RAISE NOTICE 'DELETE FROM push_notification_token WHERE userid IN (SELECT id FROM userauth WHERE id NOT IN (SELECT authid FROM usercompany))';
	EXECUTE('DELETE FROM push_notification_token WHERE userid IN (SELECT id FROM userauth WHERE id NOT IN (SELECT authid FROM usercompany));');
	RAISE NOTICE 'DELETE FROM userauth WHERE id NOT IN (SELECT authid FROM usercompany)';
	EXECUTE('DELETE FROM userauth WHERE id NOT IN (SELECT authid FROM usercompany);');
	RAISE NOTICE 'DELETE FROM clustercompany WHERE companyid IN (%)',cid;
	EXECUTE('DELETE FROM clustercompany WHERE companyid IN (' || cid || ');');
	RAISE NOTICE 'DELETE FROM companydomains WHERE companyid IN (%)',cid;
	EXECUTE('DELETE FROM companydomains WHERE companyid IN (' || cid || ');');
END;
$function$
