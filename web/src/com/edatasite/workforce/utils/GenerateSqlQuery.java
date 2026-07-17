package com.edatasite.workforce.utils;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: 18/01/11
 * Time: 18:29
 * To change this template use File | Settings | File Templates.
 */
public class GenerateSqlQuery {

    public static String getMyUserMoveToMultiSchemaAuthUserQuery(Integer companyId) {
        return "select 'insert into userauth(username,password) values('''||replace(username,'''','''''')||''','''||password||''')' from userauth where id in (select authid from usercompany where companyid = " + companyId + ")";
    }

    public static String getMyUserMoveToMultiSchemaAuthUserQueryForMap(Integer companyId) {
        return "select username, 'insert into userauth(username,password) SELECT '''||replace(username,'''','''''')||''','''||password||''' WHERE NOT EXISTS (SELECT id FROM userauth WHERE username='''||username||''')' from myuser where  companyid = " + companyId;
    }

    public static String getCompanyForInsert(Integer companyId) {
        return "SELECT 'INSERT INTO company(id,name,description,address1,address2,city,phone," +
                "workarea,issetup,countryzoneid,registrationdate,lastweeklyinvoicedate,lastmonthlyinvoicedate," +
                "lastquarterlyinvoicedate,isdeleted,postcode,indexed,timeslot,signeduppage,faxnumber,paymentdue,creationtime,lastupdatetime," +
                "updaterid,creatorid,testcompany,defaultdepartmentid,active,countryregionid," +
                "defaultprojectid,isaccountingsetup,mailingcity,mailingpostcode,sameasbilling,mailingcountryregionid," +
                "mailingcountryzoneid,isshowworkforcelogoonpdf,sigupcompip,localecode,pages,haschat," +
                " massmailenabled,companysettingsid,isfree)" +
                "VALUES ('||c.id||','''||c.name||''','''||coalesce(c.description,'null')||''','''||coalesce(c.address1,'null')||''','''||coalesce(c.address2,'null')||''','''||coalesce(c.city,'null')||''','''||coalesce(c.phone,'null')||''','''||coalesce(c.workarea,'0')||''','''||coalesce(c.issetup,false)||''','''||" +
                "coalesce(c.countryzoneid,'0')||''','''||coalesce(c.registrationdate,'1999-01-01 00:00:00')||''','''||coalesce(c.lastweeklyinvoicedate,'1999-01-01 00:00:00')||''','''||coalesce(c.lastmonthlyinvoicedate,'1999-01-01 00:00:00')||''','''||coalesce(c.lastquarterlyinvoicedate,'1999-01-01 00:00:00')||''','''||" +
                "coalesce(c.isdeleted,'false')||''','''||coalesce(c.postcode,'null')||''','''||coalesce(c.indexed,'false')||''','''||coalesce(c.timeslot,'0')||''','''||" +
                "coalesce(c.signeduppage,'null')||''','''||coalesce(c.faxnumber,'null')||''','''||coalesce(c.paymentdue,'0')||''','''||coalesce(c.creationtime,'1999-01-01 00:00:00')||''','''||coalesce(c.lastupdatetime,'1999-01-01 00:00:00')||''','''||coalesce(c.updaterid,'0')||''','''||coalesce(c.creatorid,'0')||''','''||coalesce(c.testcompany,'false')||'''," +
                "'''||coalesce(c.defaultdepartmentid,'0')||''','''||coalesce(c.active,'false')||''','''||coalesce(c.countryregionid,'0')||''','''||coalesce(c.defaultprojectid,'0')||''','''||coalesce(c.isaccountingsetup,'false')||''','''||" +
                "coalesce(c.mailingcity,'null')||''','''||coalesce(c.mailingpostcode,'null')||''','''||coalesce(c.sameasbilling,'false')||''','''||coalesce(c.mailingcountryregionid,'0')||''','''||coalesce(c.mailingcountryzoneid,'0')||''','''||" +
                "coalesce(c.isshowworkforcelogoonpdf,'false')||''','''||coalesce(c.sigupcompip,'null')||''','''||coalesce(c.localecode,'0')||''','''||coalesce(c.pages,'null')||''','''||coalesce(c.haschat,'false')||''','''||coalesce(c.massmailenabled,'false')||''','''||coalesce(c.companysettingsid,'0')||''','''||c.isfree||''')' FROM company c where c.id = " + companyId;
    }

    public static String getCompanySettingsInsert(Integer id) {
        return "SELECT 'INSERT INTO companysettings(id, enablemessagingcenter, isshowprivatecontact, longdateformat, " +
                "            shortdateformat, pdf_logo_height, pdf_logo_width, themeforsystem, " +
                "            issetupsubproject, oi_converting_type, is_fill_oi_with_inventory, " +
                "            excellimit, pdffont_id, pdflimit)" +
                "    VALUES ('||c.id||','''||coalesce(c.enablemessagingcenter,'false')||''','''||coalesce(c.isshowprivatecontact,'false')||''','''||coalesce(c.longdateformat,'null')||'''," +
                "'''||coalesce(c.shortdateformat,'null')||''','''||coalesce(c.pdf_logo_height,'0')||''','''||coalesce(c.pdf_logo_width,'0')||''','''||coalesce(c.themeforsystem,'null')||'''," +
                "'''||coalesce(c.issetupsubproject,'false')||''','''||coalesce(c.oi_converting_type,'0')||''','''||coalesce(c.is_fill_oi_with_inventory,'false')||'''," +
                "'''||coalesce(c.excellimit,'null')||''','''||coalesce(c.pdffont_id,'0')||''','''||coalesce(c.pdflimit,'null')||''')' FROM companysettings c where c.id = " + id;
    }

    public static String getUsagePlan(Integer companyId) {
        return "SELECT 'INSERT INTO usageplan(id,deleted,discount,enddate,iscurrencygbp,ispaid,isukcompany," +
                "isupgrade,messagesended,mobile,payment_enddate,payment_startdate," +
                "paypalstatus,projectcount,startdate,storage,storagefree," +
                "taskcount,taxt,totalamount,totalpayable,upgradepayable,users," +
                "usersfree,company_id,periodtype,plantype,paymentstatus,subscriptionhistory,userrate,categorycode,supportpackagename)" +
                " VALUES ('||id||','''||coalesce(deleted,'false')||''','||coalesce(discount,0)||','''||enddate||''','''||coalesce(iscurrencygbp,'false')||''','''||coalesce(ispaid,'false')||''','''||coalesce(isukcompany,'false')||''','''||" +
                "coalesce(isupgrade,false)||''','''||coalesce(messagesended,false)||''','''||coalesce(mobile,false)||''','''||coalesce(payment_enddate,'1999-01-01 00:00:00')||''','''||coalesce(payment_startdate,'1999-01-01 00:00:00')||''','''||" +
                "coalesce(paypalstatus,'false')||''','||coalesce(projectcount,0)||','''||coalesce(startdate,'1999-01-01 00:00:00')||''','||coalesce(storage,0)||','||coalesce(storagefree,'0')||','||" +
                "coalesce(taskcount,0)||','||coalesce(taxt,0)||','||coalesce(totalamount,0)||','||coalesce(totalpayable,0)||','||coalesce(upgradepayable,'0')||','||coalesce(users,0)||','||" +
                "coalesce(usersfree,0)||','||company_id||','''||coalesce(periodtype,'0')||''','''||coalesce(plantype,'0')||''','''||coalesce(paymentstatus,'0')||''','''||coalesce(subscriptionhistory,'0')||''','''||coalesce(userrate,0)||''','''||coalesce(categorycode,'null')||''','''||coalesce(supportpackagename,'null')||''')' " +
                "FROM usageplan where (deleted=false or deleted is null) and company_id=" + companyId;
    }

    public static String getCompanyStatistic(Integer companyId) {
        return "SELECT 'INSERT INTO companystatistic (" +
                "id, accesscount, activated, activeuserscount, appraisalscount, " +
                "assessmentcount, casecount, clientcount, clientsignupcompip, companyid, " +
                "companyname, companysignedupfrom, contactcount, contactperson, country, " +
                "crmtaskcount, email, eventcount, expensecount, filecount, firstaccessdate, " +
                "foldercount, indexed, industry, invoicecount, lastaccessdate, leadcount, " +
                "periodaccess, phone, productcount, projectcount, registrationdate, signeduppage, " +
                "statisticupdatedtime, suppliercount, taskcount, timesheetcount, usercount, adminemail, " +
                "host, adminname, affiliate, compaing, source, gclid, medium, redirected, referrer, " +
                "noaccessuserscount, essuserscount, clientcontactcount, currentusageplanid, " +
                "plannedactiveusers, plannedessusers, plannednoaccessusers, usagplanenddate, " +
                "usagplanstartdate, usageplanpaymentstatus, usageplanpaymenttype, usageplanuserrate, orgtype, adminphone) " +
                " VALUES ('||id||','||COALESCE(accesscount,0)||'," +
                "'''||COALESCE(activated,'true')||''','|| COALESCE(activeuserscount,0)||'," +
                "'||COALESCE(appraisalscount,0)||','||COALESCE(assessmentcount,0)||','||COALESCE(casecount,0)||','||" +
                "COALESCE(clientcount,0)||','''||COALESCE(clientsignupcompip,'N/A')||'''," +
                "'||companyid||','''||COALESCE(companyname,'null')||''','''||" +
                "COALESCE(companysignedupfrom,'null')||''','||COALESCE(contactcount,0)||','''||" +
                "COALESCE(contactperson,'null')||''','''||COALESCE(country,'null')||''','||" +
                "COALESCE(crmtaskcount,0)||','''||COALESCE(email,'null')||''','||COALESCE(eventcount,0)||','||" +
                "COALESCE(expensecount,0)||','||COALESCE(filecount,0)||'," +
                "'''||COALESCE(firstaccessdate,'1999-01-01 00:00:00')||''','||COALESCE(foldercount,0)||'," +
                "'''||coalesce(indexed,true)||''','''||COALESCE(industry,'')||''','||" +
                "COALESCE(invoicecount,0)||','''||coalesce(lastaccessdate,'1999-01-01 00:00:00')||''','||" +
                "COALESCE(leadcount,0)||','||COALESCE(periodaccess,0)||','''||" +
                "COALESCE(phone,'null')||''','||COALESCE(productcount,0)||','||COALESCE(projectcount,0)||','''||" +
                "COALESCE(registrationdate,'1999-01-01 00:00:00')||''','''||COALESCE(signeduppage,'null')||''','''||" +
                "COALESCE(statisticupdatedtime,'1999-01-01 00:00:00')||''','||COALESCE(suppliercount,0)||','||" +
                "COALESCE(taskcount,0)||','||COALESCE(timesheetcount,0)||','||COALESCE(usercount,0)||','''||" +
                "COALESCE(adminemail,'null')||''','''||COALESCE(host,'null')||''','''||COALESCE(adminname,'null')||''','''||" +
                "COALESCE(affiliate,'null')||''','''||COALESCE(compaing,'null')||''','''||COALESCE(source,'null')||''','''||" +
                "COALESCE(gclid,'null')||''','''||COALESCE(medium,'null')||''','''||COALESCE(redirected,'null')||''','''||" +
                "COALESCE(referrer,'null')||''','||COALESCE(noaccessuserscount,0)||','||COALESCE(essuserscount,0)||','||" +
                "COALESCE(clientcontactcount,0)||','||COALESCE(currentusageplanid,0)||','||COALESCE(plannedactiveusers,0)||','||" +
                "COALESCE(plannedessusers,0)||','||COALESCE(plannednoaccessusers,0)||','''||COALESCE(usagplanenddate,'1999-01-01 00:00:00')||''','''||" +
                "COALESCE(usagplanstartdate,'1999-01-01 00:00:00')||''','''||COALESCE(usageplanpaymentstatus,'null')||''','''||" +
                "COALESCE(usageplanpaymenttype,'null')||''','||COALESCE(usageplanuserrate,0)||','''||COALESCE(orgtype,'null')||''','''||" +
                "COALESCE(adminphone,'null')||''')'" +
                "FROM companystatistic WHERE companyid = " + companyId;
    }

    public static String getCompanySystemSettings(Integer companyId) {
        return "SELECT 'INSERT INTO companysystemsettings(adminemail,googleappdomain,showpopups,companyid,companysignedupfrom,showsimpleimages,maxsizefileupload,shownwftfooter,host,massmaillimit,isattachmentinmassmailing)" +
                " VALUES ('''||coalesce(adminemail,'null')||''','''||coalesce(googleappdomain,'null')||''','''||coalesce(showpopups,'false')||''','''||coalesce(companyid,'0')||''','''||coalesce(companysignedupfrom,'null')||''','''||coalesce(showsimpleimages,'false')||''','''||" +
                "coalesce(maxsizefileupload,0)||''','''||coalesce(shownwftfooter,false)||''','''||coalesce(host,'null')||''','''||coalesce(massmaillimit,'0')||''','''||coalesce(isattachmentinmassmailing,false)||''')' FROM companysystemsettings where companyid=" + companyId;
    }

    public static String getCompanyWebsites(Integer companyId) {
        return "SELECT 'INSERT INTO wfp_website (id,deleted,domain,name,company_id,number,theme_id,iskpidefaultwebsite,ispublished )" +
                "VALUES('||id||','''||coalesce(deleted,'false')||''','''||coalesce(domain,'null')||''','''||coalesce(name,'null')||''','''||coalesce(company_id,'0')||''','''||coalesce(number,'null')||''','''||coalesce(theme_id,'0')||''','''||coalesce(iskpidefaultwebsite,'false')||''','''||coalesce(ispublished,'false')||''')' " +
                "from wfp_website where company_id = " + companyId;
    }

    public static String getMyUserMoveToMultiSchemaUserCompanyQuery(Integer companyId) {
        return "select 'insert into userCompany(userID,companyID,authID,email) values('||id||','||companyid||',(select id from userauth where username='''||replace(username,'''','''''')||''' and password='''||password||''' limit 1),'''||email||''')' from myuser where  companyid =" + companyId;
    }
}
