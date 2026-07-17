-- Function: deletecompanyfunc3(text)

-- DROP FUNCTION deletecompanyfunc3(text);

CREATE OR REPLACE FUNCTION deletecompanyfunc3(cid text)
  RETURNS void AS
$BODY$
       DECLARE tbltocopy text;
       BEGIN
       RAISE NOTICE 'DROP SCHEMA  "%" CASCADE',cid;
       EXECUTE('DROP SCHEMA IF EXISTS "' || cid ||'" CASCADE;');
       RAISE NOTICE 'delete FROM vatefiling where companyid in (%);',cid;
       EXECUTE('delete FROM vatefiling where companyid in (' || cid || ');');
       RAISE NOTICE 'delete FROM subscription_payment where usageplan_id in(select id from usageplan where company_id in (%));',cid;
       EXECUTE('delete FROM subscription_payment where usageplan_id in(select id from usageplan where company_id in (' || cid || '));');
       RAISE NOTICE 'update usageplan set subscriptionhistory=null where company_id in (%);',cid;
       EXECUTE('update usageplan set subscriptionhistory=null where company_id in (' || cid || ');');
       RAISE NOTICE 'delete FROM subscriptionhistory where usageplan_id in (select id from usageplan where company_id in (%));',cid;
       EXECUTE('delete FROM subscriptionhistory where usageplan_id in (select id from usageplan where company_id in (' || cid || '));');
       RAISE NOTICE 'delete FROM rolereporttemplate where companyid in (%);',cid;
       EXECUTE('delete FROM rolereporttemplate where companyid in (' || cid || ');');
       RAISE NOTICE 'delete FROM usageplan where company_id in (%);',cid;
       EXECUTE('delete FROM usageplan where company_id in (' || cid || ');');
       RAISE NOTICE 'delete FROM companytrace where company_id in (%);',cid;
       EXECUTE('delete FROM companytrace where company_id in (' || cid || ');');
       RAISE NOTICE 'delete FROM companysystemsettings where companyid in (%);',cid;
       EXECUTE('delete FROM companysystemsettings where companyid in (' || cid || ');');
       RAISE NOTICE 'delete from storefront where companyid in (%);',cid;
       EXECUTE('delete from storefront where companyid in (' || cid || ');');
       RAISE NOTICE 'delete from public_event where website_id in (select id from wfp_website where company_id in (%));',cid;
       EXECUTE('delete from public_event where website_id in (select id from wfp_website where company_id in (' || cid || '));');
       RAISE NOTICE 'delete FROM wfp_website where company_id in (%);',cid;
       EXECUTE('delete FROM wfp_website where company_id in (' || cid || ');');
       RAISE NOTICE 'delete from recurrence where companyid in (%);',cid;
       EXECUTE('delete from recurrence where companyid in (' || cid || ');');
       RAISE NOTICE 'delete from backendmanagement where company_id in (%);',cid;
       EXECUTE('delete from backendmanagement where company_id in (' || cid || ');');
       RAISE NOTICE 'delete from smssettings where companyid in (%);',cid;
       EXECUTE('delete from smssettings where companyid in (' || cid || ');');
       RAISE NOTICE 'delete from localizationpermissions where company_id in (%);',cid;
       EXECUTE('delete from localizationpermissions where company_id in (' || cid || ');');
       RAISE NOTICE 'delete FROM company where id in (%);',cid;
       EXECUTE('delete FROM company where id in (' || cid || ');');
       RAISE NOTICE 'delete from companysettings where id not in (select companysettingsid from company);';
       EXECUTE('delete from companysettings where id not in (select companysettingsid from company);');
       RAISE NOTICE 'delete from financialsettings where id not in (select financialsettingsid from company);';
       EXECUTE('delete from financialsettings where id not in (select financialsettingsid from company);');
       END;
       $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION deletecompanyfunc3(text)
  OWNER TO postgres;