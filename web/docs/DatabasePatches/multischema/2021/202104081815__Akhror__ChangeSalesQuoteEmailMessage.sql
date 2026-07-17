update "anv".emailtemplate set messagehtml = '<p>Dear ${first_name}  ${last_name},</p>
<p>Please be advised that ${name} has submitted a Sales ${type} for your attention at ${productName} on ${start_date}</p>
<p>${hasaccesslink}</p>
<p>Once you approve or Reject the Sales ${type} ${name} will receive email notification about its status.</p>
                <p>Your Sincerely,</br> ${name} </br><b> ${company_name} </b></p>', subject = 'Sales ${type}' where name = 'Default Sales Quote For Manager';

