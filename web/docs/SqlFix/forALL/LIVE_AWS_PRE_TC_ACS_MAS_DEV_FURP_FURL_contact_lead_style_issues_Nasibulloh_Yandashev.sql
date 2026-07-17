	--// Contact Form style issue
	update modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id = 'CONTACT_FORM';
	update modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id = 'CONTACT_FORM' and section !='CRM_DETAILS';
	update modelfield set halfSetStyle = 'halfSet-1 left' where form_id = 'CONTACT_FORM';
	update modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id = 'CONTACT_FORM' and section != 'ADDRESS_INFORMATION';
	update modelfield set rowStyle = 'row hideCustomField' where form_id = 'CONTACT_FORM';
	update modelfield set fieldStyle = 'field' where form_id = 'CONTACT_FORM';

	--//Lead Form style issue
	update modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id = 'LEAD_FORM';
    update modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id = 'LEAD_FORM' and section !='ADDRESS_INFORMATION';
    update modelfield set fieldSetStyle = 'slideDown-content group nobrd' where noLabelFor is not null and noLabelFor != '' and form_id = 'LEAD_FORM' and section != 'ADDRESS_INFORMATION';
    update modelfield set halfSetStyle = 'halfSet-1 left' where form_id = 'LEAD_FORM';
    update modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id = 'LEAD_FORM' and section != 'ADDRESS_INFORMATION';
    update modelfield set rowStyle = 'row hideCustomField' where form_id = 'LEAD_FORM';
    update modelfield set fieldStyle = 'field' where form_id = 'LEAD_FORM';


--=============For Zero =================
	--// Contact Form style issue
	update "0".modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id = 'CONTACT_FORM';
	update "0".modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id = 'CONTACT_FORM' and section !='CRM_DETAILS';
	update "0".modelfield set halfSetStyle = 'halfSet-1 left' where form_id = 'CONTACT_FORM';
	update "0".modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id = 'CONTACT_FORM' and section != 'ADDRESS_INFORMATION';
	update "0".modelfield set rowStyle = 'row hideCustomField' where form_id = 'CONTACT_FORM';
	update "0".modelfield set fieldStyle = 'field' where form_id = 'CONTACT_FORM';

	--//Lead Form style issue
	update "0".modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id = 'LEAD_FORM';
    update "0".modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id = 'LEAD_FORM' and section !='ADDRESS_INFORMATION';
    update "0".modelfield set fieldSetStyle = 'slideDown-content group nobrd' where noLabelFor is not null and noLabelFor != '' and form_id = 'LEAD_FORM' and section != 'ADDRESS_INFORMATION';
    update "0".modelfield set halfSetStyle = 'halfSet-1 left' where form_id = 'LEAD_FORM';
    update "0".modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id = 'LEAD_FORM' and section != 'ADDRESS_INFORMATION';
    update "0".modelfield set rowStyle = 'row hideCustomField' where form_id = 'LEAD_FORM';
    update "0".modelfield set fieldStyle = 'field' where form_id = 'LEAD_FORM';


--=============For PRIVATE =================
	--// Contact Form style issue
	update "anv".modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id = 'CONTACT_FORM';
	update "anv".modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id = 'CONTACT_FORM' and section !='CRM_DETAILS';
	update "anv".modelfield set halfSetStyle = 'halfSet-1 left' where form_id = 'CONTACT_FORM';
	update "anv".modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id = 'CONTACT_FORM' and section != 'ADDRESS_INFORMATION';
	update "anv".modelfield set rowStyle = 'row hideCustomField' where form_id = 'CONTACT_FORM';
	update "anv".modelfield set fieldStyle = 'field' where form_id = 'CONTACT_FORM';

	--//Lead Form style issue
	update "anv".modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id = 'LEAD_FORM';
    update "anv".modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id = 'LEAD_FORM' and section !='ADDRESS_INFORMATION';
    update "anv".modelfield set fieldSetStyle = 'slideDown-content group nobrd' where noLabelFor is not null and noLabelFor != '' and form_id = 'LEAD_FORM' and section != 'ADDRESS_INFORMATION';
    update "anv".modelfield set halfSetStyle = 'halfSet-1 left' where form_id = 'LEAD_FORM';
    update "anv".modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id = 'LEAD_FORM' and section != 'ADDRESS_INFORMATION';
    update "anv".modelfield set rowStyle = 'row hideCustomField' where form_id = 'LEAD_FORM';
    update "anv".modelfield set fieldStyle = 'field' where form_id = 'LEAD_FORM';