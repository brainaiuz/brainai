--For overdue
update "0".emailTemplate set subject = replace(subject,'${date}','${start_date}'), messagehtml = replace(messagehtml,'${date}','${start_date}')
where categoryid = (select id from "0".reference where code = 'OVERDUE_INVOICE_REMINDER_FOR_CLIENT_CATEGORY');

update "anv".emailTemplate set subject = replace(subject,'${date}','${start_date}'), messagehtml = replace(messagehtml,'${date}','${start_date}')
where categoryid = (select id from "anv".reference where code = 'OVERDUE_INVOICE_REMINDER_FOR_CLIENT_CATEGORY');


update "0".emailTemplate set subject = replace(subject,'${firstname}','${first_name}'), messagehtml = replace(messagehtml,'${firstname}','${first_name}');
update "0".emailTemplate set subject = replace(subject,'${lastname}','${last_name}'), messagehtml = replace(messagehtml,'${lastname}','${last_name}');
update "0".emailTemplate set subject = replace(subject,'${companyname}','${company_name}'), messagehtml = replace(messagehtml,'${companyname}','${company_name}');
update "0".emailTemplate set subject = replace(subject,'${clientname}','${customer}'), messagehtml = replace(messagehtml,'${clientname}','${customer}');
update "0".emailTemplate set subject = replace(subject,'${startdate}','${start_date}'), messagehtml = replace(messagehtml,'${startdate}','${start_date}');
update "0".emailTemplate set subject = replace(subject,'${duedate}','${due_date}'), messagehtml = replace(messagehtml,'${duedate}','${due_date}');
update "0".emailTemplate set subject = replace(subject,'${totalamount}','${total_amount}'), messagehtml = replace(messagehtml,'${totalamount}','${total_amount}');
update "0".emailTemplate set subject = replace(subject,'${paidamount}','${paid_amount}'), messagehtml = replace(messagehtml,'${paidamount}','${total_amount}');
update "0".emailTemplate set subject = replace(subject,'${enddate}','${end_date}'), messagehtml = replace(messagehtml,'${enddate}','${end_date}');
update "0".emailTemplate set subject = replace(subject,'${proirity}','${priority}'), messagehtml = replace(messagehtml,'${proirity}','${priority}');

--workflow
update "0".workflow_alerts set subject = replace(subject,'${firstname}','${first_name}'), content = replace(content,'${firstname}','${first_name}');
update "0".workflow_alerts set subject = replace(subject,'${lastname}','${last_name}'), content = replace(content,'${lastname}','${last_name}');


update "anv".emailTemplate set subject = replace(subject,'${firstname}','${first_name}'), messagehtml = replace(messagehtml,'${firstname}','${first_name}');
update "anv".emailTemplate set subject = replace(subject,'${lastname}','${last_name}'), messagehtml = replace(messagehtml,'${lastname}','${last_name}');
update "anv".emailTemplate set subject = replace(subject,'${companyname}','${company_name}'), messagehtml = replace(messagehtml,'${companyname}','${company_name}');
update "anv".emailTemplate set subject = replace(subject,'${clientname}','${customer}'), messagehtml = replace(messagehtml,'${clientname}','${customer}');
update "anv".emailTemplate set subject = replace(subject,'${startdate}','${start_date}'), messagehtml = replace(messagehtml,'${startdate}','${start_date}');
update "anv".emailTemplate set subject = replace(subject,'${duedate}','${due_date}'), messagehtml = replace(messagehtml,'${duedate}','${due_date}');
update "anv".emailTemplate set subject = replace(subject,'${totalamount}','${total_amount}'), messagehtml = replace(messagehtml,'${totalamount}','${total_amount}');
update "anv".emailTemplate set subject = replace(subject,'${paidamount}','${paid_amount}'), messagehtml = replace(messagehtml,'${paidamount}','${paid_amount}');
update "anv".emailTemplate set subject = replace(subject,'${enddate}','${end_date}'), messagehtml = replace(messagehtml,'${enddate}','${end_date}');
update "anv".emailTemplate set subject = replace(subject,'${proirity}','${priority}'), messagehtml = replace(messagehtml,'${proirity}','${priority}');
--workflow
update "anv".workflow_alerts set subject = replace(subject,'${firstname}','${first_name}'), content = replace(content,'${firstname}','${first_name}');
update "anv".workflow_alerts set subject = replace(subject,'${lastname}','${last_name}'), content = replace(content,'${lastname}','${last_name}');

