
UPDATE "0".certificateofemployment set contenthtml='<div class="mainStyle_reset">
    <table cellpadding="10" cellspacing="0" class="section" id="table" width="700">
        <tbody>
            <tr>
                <td class="space_y" style="font: 20px/20px Arial;">
                </td>
            </tr>
            <tr>
                <td>
                    <table align="left" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                      <tbody>
                        <tr>
                            <th align="left" height="120" valign="top" >
                                <span style="font-family:times new roman,times,serif;">
                                    <label style="font-size: 14px;">
                                        From:
                                    </label>
                                </span>
                            </th>
                            <td align="left" style="font-size: 12px;" valign="top">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${companyname},
                                        <br/>
                                        ${companyaddress}
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
            <td >
                <table align="right" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                   <tbody>
                    <tr>
                        <th align="left" height="120" valign="top">
                            <span style="font-family:times new roman,times,serif;">
                                <label for="hrCert_to" style="font-size: 14px;">
                                    To:
                                </label>
                            </span>
                        </th>
                        <td align="left" style="font-size: 12px;" valign="top">
                            <span style="font-size:14px;">
                                <span style="font-family: times new roman,times,serif;">
                                    $$input:textarea1$$
                                </span>
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td style="color: rgb(187, 187, 187);">
            <span style="font-family:times new roman,times,serif;">
                Date: ${currentdate}
            </span>
        </td>
    </tr>
</tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <h1 style="text-align: center; margin: 0pt; font: bold 28px/1.4 Arial;">
                    <span style="font-family:times new roman,times,serif;">
                        Certificate of Employment
                    </span>
                </h1>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td align="left">
                <span style="font-size:16px;">
                    <span style="font-family: times new roman,times,serif;">
                        <span style="line-height: 1.6;">
                            This is to certify that&nbsp; ${firstname} ${lastname}, Passport
                            <strong>
                                No.${passportnumber}
                            </strong>
                            and his monthly gross salary is
                            <strong>
                                ${currency}
                            </strong>
                            ${salaryamount}. He is working in
                            <strong>
                                ${companyname}
                            </strong>
                            as ${position} from
                            <strong>
                            </strong>
                           <span>${hiredate} till this date. He wants to spend his annual vacation in the </span> <span style="display: inline-block;width: 150px;margin-right: 10px;">$$input:textbox1$$</span> <span>for the purpose of tourism.</span>
                        </span>
                    </span>
                </span>
                <br/>
                <p>
                    <span style="font-size:14px;">
                        <span style="font-family: times new roman,times,serif;">
                            <small>
                                This certificate is granted upon his/her request and without any responsibility on the part of
                                <strong>
                                    ${companyname}
                                </strong>
                                .
                            </small>
                        </span>
                    </span>
                </p>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <span style="font-size:14px;">
                    <span style="font-family: times new roman,times,serif;">
                        Regards,
                    </span>
                </span>
                <table border="0" cellpadding="0" cellspacing="0" width="30%">
                    <tbody>
                        <tr>
                            <td style="padding-bottom: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${yourname}
                                    </span>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="border-top: 1px solid rgb(221, 221, 221); padding-top: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        <strong>
                                            ${yourrole}
                                        </strong>
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>
</div>' WHERE certificatetypeid IN (SELECT id FROM "0".certificateofemploymenttype WHERE (deleted is null or deleted = false) AND name = 'Certificate of employment' ORDER BY id ASC LIMIT 1)
        and id = (select id from "0".certificateofemployment where certificatetypeid IN (SELECT id FROM "0".certificateofemploymenttype WHERE (deleted is null or deleted = false) AND name = 'Certificate of employment') ORDER BY id ASC LIMIT 1);


UPDATE "0".certificateofemploymenttype  SET defaulthtml='<div class="mainStyle_reset">
    <table cellpadding="10" cellspacing="0" class="section" id="table" width="700">
        <tbody>
            <tr>
                <td class="space_y" style="font: 20px/20px Arial;">
                </td>
            </tr>
            <tr>
                <td>
                    <table align="left" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                      <tbody>
                        <tr>
                            <th align="left" height="120" valign="top" >
                                <span style="font-family:times new roman,times,serif;">
                                    <label style="font-size: 14px;">
                                        From:
                                    </label>
                                </span>
                            </th>
                            <td align="left" style="font-size: 12px;" valign="top">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${companyname},
                                        <br/>
                                        ${companyaddress}
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
            <td >
                <table align="right" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                   <tbody>
                    <tr>
                        <th align="left" height="120" valign="top">
                            <span style="font-family:times new roman,times,serif;">
                                <label for="hrCert_to" style="font-size: 14px;">
                                    To:
                                </label>
                            </span>
                        </th>
                        <td align="left" style="font-size: 12px;" valign="top">
                            <span style="font-size:14px;">
                                <span style="font-family: times new roman,times,serif;">
                                    $$input:textarea1$$
                                </span>
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td style="color: rgb(187, 187, 187);">
            <span style="font-family:times new roman,times,serif;">
                Date: ${currentdate}
            </span>
        </td>
    </tr>
</tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <h1 style="text-align: center; margin: 0pt; font: bold 28px/1.4 Arial;">
                    <span style="font-family:times new roman,times,serif;">
                        Certificate of Employment
                    </span>
                </h1>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td align="left">
                <span style="font-size:16px;">
                    <span style="font-family: times new roman,times,serif;">
                        <span style="line-height: 1.6;">
                            This is to certify that&nbsp; ${firstname} ${lastname}, Passport
                            <strong>
                                No.${passportnumber}
                            </strong>
                            and his monthly gross salary is
                            <strong>
                                ${currency}
                            </strong>
                            ${salaryamount}. He is working in
                            <strong>
                                ${companyname}
                            </strong>
                            as ${position} from
                            <strong>
                            </strong>
                           <span>${hiredate} till this date. He wants to spend his annual vacation in the </span> <span style="display: inline-block;width: 150px;margin-right: 10px;">$$input:textbox1$$</span> <span>for the purpose of tourism.</span>
                        </span>
                    </span>
                </span>
                <br/>
                <p>
                    <span style="font-size:14px;">
                        <span style="font-family: times new roman,times,serif;">
                            <small>
                                This certificate is granted upon his/her request and without any responsibility on the part of
                                <strong>
                                    ${companyname}
                                </strong>
                                .
                            </small>
                        </span>
                    </span>
                </p>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <span style="font-size:14px;">
                    <span style="font-family: times new roman,times,serif;">
                        Regards,
                    </span>
                </span>
                <table border="0" cellpadding="0" cellspacing="0" width="30%">
                    <tbody>
                        <tr>
                            <td style="padding-bottom: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${yourname}
                                    </span>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="border-top: 1px solid rgb(221, 221, 221); padding-top: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        <strong>
                                            ${yourrole}
                                        </strong>
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>
</div>' WHERE id in (SELECT id FROM "0".certificateofemploymenttype WHERE (defaultHTML IS NOT NULL AND defaultHTML <>'') and (deleted is null or deleted = false) AND name = 'Certificate of employment' ORDER BY id ASC LIMIT 1);

UPDATE "0".certificateofemploymenttype SET customHTML='<div class="mainStyle_reset">
    <table cellpadding="10" cellspacing="0" class="section" id="table" width="700">
        <tbody>
            <tr>
                <td class="space_y" style="font: 20px/20px Arial;">
                </td>
            </tr>
            <tr>
                <td>
                    <table align="left" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                      <tbody>
                        <tr>
                            <th align="left" height="120" valign="top" >
                                <span style="font-family:times new roman,times,serif;">
                                    <label style="font-size: 14px;">
                                        From:
                                    </label>
                                </span>
                            </th>
                            <td align="left" style="font-size: 12px;" valign="top">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${companyname},
                                        <br/>
                                        ${companyaddress}
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
            <td >
                <table align="right" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                   <tbody>
                    <tr>
                        <th align="left" height="120" valign="top">
                            <span style="font-family:times new roman,times,serif;">
                                <label for="hrCert_to" style="font-size: 14px;">
                                    To:
                                </label>
                            </span>
                        </th>
                        <td align="left" style="font-size: 12px;" valign="top">
                            <span style="font-size:14px;">
                                <span style="font-family: times new roman,times,serif;">
                                    $$input:textarea1$$
                                </span>
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td style="color: rgb(187, 187, 187);">
            <span style="font-family:times new roman,times,serif;">
                Date: ${currentdate}
            </span>
        </td>
    </tr>
</tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <h1 style="text-align: center; margin: 0pt; font: bold 28px/1.4 Arial;">
                    <span style="font-family:times new roman,times,serif;">
                        Certificate of Employment
                    </span>
                </h1>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td align="left">
                <span style="font-size:16px;">
                    <span style="font-family: times new roman,times,serif;">
                        <span style="line-height: 1.6;">
                            This is to certify that&nbsp; ${firstname} ${lastname}, Passport
                            <strong>
                                No.${passportnumber}
                            </strong>
                            and his monthly gross salary is
                            <strong>
                                ${currency}
                            </strong>
                            ${salaryamount}. He is working in
                            <strong>
                                ${companyname}
                            </strong>
                            as ${position} from
                            <strong>
                            </strong>
                           <span>${hiredate} till this date. He wants to spend his annual vacation in the </span> <span style="display: inline-block;width: 150px;margin-right: 10px;">$$input:textbox1$$</span> <span>for the purpose of tourism.</span>
                        </span>
                    </span>
                </span>
                <br/>
                <p>
                    <span style="font-size:14px;">
                        <span style="font-family: times new roman,times,serif;">
                            <small>
                                This certificate is granted upon his/her request and without any responsibility on the part of
                                <strong>
                                    ${companyname}
                                </strong>
                                .
                            </small>
                        </span>
                    </span>
                </p>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <span style="font-size:14px;">
                    <span style="font-family: times new roman,times,serif;">
                        Regards,
                    </span>
                </span>
                <table border="0" cellpadding="0" cellspacing="0" width="30%">
                    <tbody>
                        <tr>
                            <td style="padding-bottom: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${yourname}
                                    </span>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="border-top: 1px solid rgb(221, 221, 221); padding-top: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        <strong>
                                            ${yourrole}
                                        </strong>
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>
</div>' WHERE id in (SELECT id FROM "0".certificateofemploymenttype WHERE (customHTML IS NOT NULL AND customHTML <>'') and (deleted is null or deleted = false) AND name = 'Certificate of employment' ORDER BY id ASC LIMIT 1);



UPDATE "anv".certificateofemployment set contenthtml='<div class="mainStyle_reset">
    <table cellpadding="10" cellspacing="0" class="section" id="table" width="700">
        <tbody>
            <tr>
                <td class="space_y" style="font: 20px/20px Arial;">
                </td>
            </tr>
            <tr>
                <td>
                    <table align="left" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                      <tbody>
                        <tr>
                            <th align="left" height="120" valign="top" >
                                <span style="font-family:times new roman,times,serif;">
                                    <label style="font-size: 14px;">
                                        From:
                                    </label>
                                </span>
                            </th>
                            <td align="left" style="font-size: 12px;" valign="top">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${companyname},
                                        <br/>
                                        ${companyaddress}
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
            <td >
                <table align="right" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                   <tbody>
                    <tr>
                        <th align="left" height="120" valign="top">
                            <span style="font-family:times new roman,times,serif;">
                                <label for="hrCert_to" style="font-size: 14px;">
                                    To:
                                </label>
                            </span>
                        </th>
                        <td align="left" style="font-size: 12px;" valign="top">
                            <span style="font-size:14px;">
                                <span style="font-family: times new roman,times,serif;">
                                    $$input:textarea1$$
                                </span>
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td style="color: rgb(187, 187, 187);">
            <span style="font-family:times new roman,times,serif;">
                Date: ${currentdate}
            </span>
        </td>
    </tr>
</tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <h1 style="text-align: center; margin: 0pt; font: bold 28px/1.4 Arial;">
                    <span style="font-family:times new roman,times,serif;">
                        Certificate of Employment
                    </span>
                </h1>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td align="left">
                <span style="font-size:16px;">
                    <span style="font-family: times new roman,times,serif;">
                        <span style="line-height: 1.6;">
                            This is to certify that&nbsp; ${firstname} ${lastname}, Passport
                            <strong>
                                No.${passportnumber}
                            </strong>
                            and his monthly gross salary is
                            <strong>
                                ${currency}
                            </strong>
                            ${salaryamount}. He is working in
                            <strong>
                                ${companyname}
                            </strong>
                            as ${position} from
                            <strong>
                            </strong>
                           <span>${hiredate} till this date. He wants to spend his annual vacation in the </span> <span style="display: inline-block;width: 150px;margin-right: 10px;">$$input:textbox1$$</span> <span>for the purpose of tourism.</span>
                        </span>
                    </span>
                </span>
                <br/>
                <p>
                    <span style="font-size:14px;">
                        <span style="font-family: times new roman,times,serif;">
                            <small>
                                This certificate is granted upon his/her request and without any responsibility on the part of
                                <strong>
                                    ${companyname}
                                </strong>
                                .
                            </small>
                        </span>
                    </span>
                </p>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <span style="font-size:14px;">
                    <span style="font-family: times new roman,times,serif;">
                        Regards,
                    </span>
                </span>
                <table border="0" cellpadding="0" cellspacing="0" width="30%">
                    <tbody>
                        <tr>
                            <td style="padding-bottom: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${yourname}
                                    </span>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="border-top: 1px solid rgb(221, 221, 221); padding-top: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        <strong>
                                            ${yourrole}
                                        </strong>
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>
</div>' WHERE certificatetypeid IN (SELECT id FROM "anv".certificateofemploymenttype WHERE (deleted is null or deleted = false) AND name = 'Certificate of employment' ORDER BY id ASC LIMIT 1)
        and id = (select id from "anv".certificateofemployment where certificatetypeid IN (SELECT id FROM "anv".certificateofemploymenttype WHERE (deleted is null or deleted = false) AND name = 'Certificate of employment') ORDER BY id ASC LIMIT 1);


UPDATE "anv".certificateofemploymenttype  SET defaulthtml='<div class="mainStyle_reset">
    <table cellpadding="10" cellspacing="0" class="section" id="table" width="700">
        <tbody>
            <tr>
                <td class="space_y" style="font: 20px/20px Arial;">
                </td>
            </tr>
            <tr>
                <td>
                    <table align="left" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                      <tbody>
                        <tr>
                            <th align="left" height="120" valign="top" >
                                <span style="font-family:times new roman,times,serif;">
                                    <label style="font-size: 14px;">
                                        From:
                                    </label>
                                </span>
                            </th>
                            <td align="left" style="font-size: 12px;" valign="top">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${companyname},
                                        <br/>
                                        ${companyaddress}
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
            <td >
                <table align="right" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                   <tbody>
                    <tr>
                        <th align="left" height="120" valign="top">
                            <span style="font-family:times new roman,times,serif;">
                                <label for="hrCert_to" style="font-size: 14px;">
                                    To:
                                </label>
                            </span>
                        </th>
                        <td align="left" style="font-size: 12px;" valign="top">
                            <span style="font-size:14px;">
                                <span style="font-family: times new roman,times,serif;">
                                    $$input:textarea1$$
                                </span>
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td style="color: rgb(187, 187, 187);">
            <span style="font-family:times new roman,times,serif;">
                Date: ${currentdate}
            </span>
        </td>
    </tr>
</tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <h1 style="text-align: center; margin: 0pt; font: bold 28px/1.4 Arial;">
                    <span style="font-family:times new roman,times,serif;">
                        Certificate of Employment
                    </span>
                </h1>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td align="left">
                <span style="font-size:16px;">
                    <span style="font-family: times new roman,times,serif;">
                        <span style="line-height: 1.6;">
                            This is to certify that&nbsp; ${firstname} ${lastname}, Passport
                            <strong>
                                No.${passportnumber}
                            </strong>
                            and his monthly gross salary is
                            <strong>
                                ${currency}
                            </strong>
                            ${salaryamount}. He is working in
                            <strong>
                                ${companyname}
                            </strong>
                            as ${position} from
                            <strong>
                            </strong>
                           <span>${hiredate} till this date. He wants to spend his annual vacation in the </span> <span style="display: inline-block;width: 150px;margin-right: 10px;">$$input:textbox1$$</span> <span>for the purpose of tourism.</span>
                        </span>
                    </span>
                </span>
                <br/>
                <p>
                    <span style="font-size:14px;">
                        <span style="font-family: times new roman,times,serif;">
                            <small>
                                This certificate is granted upon his/her request and without any responsibility on the part of
                                <strong>
                                    ${companyname}
                                </strong>
                                .
                            </small>
                        </span>
                    </span>
                </p>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <span style="font-size:14px;">
                    <span style="font-family: times new roman,times,serif;">
                        Regards,
                    </span>
                </span>
                <table border="0" cellpadding="0" cellspacing="0" width="30%">
                    <tbody>
                        <tr>
                            <td style="padding-bottom: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${yourname}
                                    </span>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="border-top: 1px solid rgb(221, 221, 221); padding-top: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        <strong>
                                            ${yourrole}
                                        </strong>
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>
</div>' WHERE id in (SELECT id FROM "anv".certificateofemploymenttype WHERE (defaultHTML IS NOT NULL AND defaultHTML <>'') and (deleted is null or deleted = false) AND name = 'Certificate of employment' ORDER BY id ASC LIMIT 1);

UPDATE "anv".certificateofemploymenttype SET customHTML='<div class="mainStyle_reset">
    <table cellpadding="10" cellspacing="0" class="section" id="table" width="700">
        <tbody>
            <tr>
                <td class="space_y" style="font: 20px/20px Arial;">
                </td>
            </tr>
            <tr>
                <td>
                    <table align="left" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                      <tbody>
                        <tr>
                            <th align="left" height="120" valign="top" >
                                <span style="font-family:times new roman,times,serif;">
                                    <label style="font-size: 14px;">
                                        From:
                                    </label>
                                </span>
                            </th>
                            <td align="left" style="font-size: 12px;" valign="top">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${companyname},
                                        <br/>
                                        ${companyaddress}
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
            <td >
                <table align="right" border="0" cellpadding="10" cellspacing="0" height="120" style="border: 1px solid rgb(221, 221, 221);" width="45%">
                   <tbody>
                    <tr>
                        <th align="left" height="120" valign="top">
                            <span style="font-family:times new roman,times,serif;">
                                <label for="hrCert_to" style="font-size: 14px;">
                                    To:
                                </label>
                            </span>
                        </th>
                        <td align="left" style="font-size: 12px;" valign="top">
                            <span style="font-size:14px;">
                                <span style="font-family: times new roman,times,serif;">
                                    $$input:textarea1$$
                                </span>
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td style="color: rgb(187, 187, 187);">
            <span style="font-family:times new roman,times,serif;">
                Date: ${currentdate}
            </span>
        </td>
    </tr>
</tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <h1 style="text-align: center; margin: 0pt; font: bold 28px/1.4 Arial;">
                    <span style="font-family:times new roman,times,serif;">
                        Certificate of Employment
                    </span>
                </h1>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td align="left">
                <span style="font-size:16px;">
                    <span style="font-family: times new roman,times,serif;">
                        <span style="line-height: 1.6;">
                            This is to certify that&nbsp; ${firstname} ${lastname}, Passport
                            <strong>
                                No.${passportnumber}
                            </strong>
                            and his monthly gross salary is
                            <strong>
                                ${currency}
                            </strong>
                            ${salaryamount}. He is working in
                            <strong>
                                ${companyname}
                            </strong>
                            as ${position} from
                            <strong>
                            </strong>
                           <span>${hiredate} till this date. He wants to spend his annual vacation in the </span> <span style="display: inline-block;width: 150px;margin-right: 10px;">$$input:textbox1$$</span> <span>for the purpose of tourism.</span>
                        </span>
                    </span>
                </span>
                <br/>
                <p>
                    <span style="font-size:14px;">
                        <span style="font-family: times new roman,times,serif;">
                            <small>
                                This certificate is granted upon his/her request and without any responsibility on the part of
                                <strong>
                                    ${companyname}
                                </strong>
                                .
                            </small>
                        </span>
                    </span>
                </p>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="10" cellspacing="0" class="section" width="700">
    <tbody>
        <tr>
            <td>
                <span style="font-size:14px;">
                    <span style="font-family: times new roman,times,serif;">
                        Regards,
                    </span>
                </span>
                <table border="0" cellpadding="0" cellspacing="0" width="30%">
                    <tbody>
                        <tr>
                            <td style="padding-bottom: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        ${yourname}
                                    </span>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="border-top: 1px solid rgb(221, 221, 221); padding-top: 4px;">
                                <span style="font-size:14px;">
                                    <span style="font-family: times new roman,times,serif;">
                                        <strong>
                                            ${yourrole}
                                        </strong>
                                    </span>
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>
</div>' WHERE id in (SELECT id FROM "anv".certificateofemploymenttype WHERE (customHTML IS NOT NULL AND customHTML <>'') and (deleted is null or deleted = false) AND name = 'Certificate of employment' ORDER BY id ASC LIMIT 1);
