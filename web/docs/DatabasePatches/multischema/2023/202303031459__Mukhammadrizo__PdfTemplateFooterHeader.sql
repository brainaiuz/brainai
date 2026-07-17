    delete from "anv".dynamic_footer_header;

    INSERT INTO "anv".dynamic_footer_header(enable, key, value, templateid)
	select paginationEnabled, 'HEADER_RIGHT', '<div style="text-align: right;"><span style="font-size: 10px;">${pagination}</span><br><br><span style="font-size: 14px;">${document_title}</span></div>', id
	from "anv".pdftemplate_settings;

	INSERT INTO "anv".dynamic_footer_header(enable, key, value, templateid)
	select companyNameEnabled, 'HEADER_CENTER', '<div style="vertical-align: top;height: 70px; padding-top: 19px; text-align: left;white-space: nowrap; width=34%">${company_name}</div>', id
	from "anv".pdftemplate_settings;

	INSERT INTO "anv".dynamic_footer_header(enable, key, value, templateid)
	select companyLogoEnabled, 'HEADER_LEFT', '<div style="text-align: left;">${company_logo}</div>', id
	from "anv".pdftemplate_settings;

	INSERT INTO "anv".dynamic_footer_header(enable, key, value, templateid)
	select poweredByEnabled, 'FOOTER_RIGHT', '<div style="text-align: right;">${powered_by}</div>', id
	from "anv".pdftemplate_settings;

	INSERT INTO "anv".dynamic_footer_header(enable, key, value, templateid)
	select customAddressEnabled, 'FOOTER_CENTER', '<div style="text-align: justify;"><span style="font-size: 12px;">${company_main_address}</span><div style="font-size: 7pt; color:#939598; line-height:1.4;"><span>${fax_num} ${phone_number} </span><span>${email_id} ${website}</span></div></div>', id
	from "anv".pdftemplate_settings;

	INSERT INTO "anv".dynamic_footer_header(enable, key, value, templateid)
	select qrCodeEnabled, 'FOOTER_LEFT', '<div style="text-align: left;">${qr_code}</div>', id
	from "anv".pdftemplate_settings;



    INSERT INTO "anv".dynamic_footer_header(enable, key, value)
	select true, 'DEFAULT_HEADER_RIGHT', '<div style="text-align: right;"><span style="font-size: 10px;">${pagination}</span><br><br><span style="font-size: 14px;">${document_title}</span></div>';

	INSERT INTO "anv".dynamic_footer_header(enable, key, value)
	select true, 'DEFAULT_HEADER_CENTER', '<div style="vertical-align: top;height: 70px; padding-top: 19px; text-align: left;white-space: nowrap; width=34%">${company_name}</div>';

	INSERT INTO "anv".dynamic_footer_header(enable, key, value)
	select true, 'DEFAULT_HEADER_LEFT', '<div style="text-align: left;">${company_logo}</div>';

	INSERT INTO "anv".dynamic_footer_header(enable, key, value)
	select true, 'DEFAULT_FOOTER_RIGHT', '<div style="text-align: right;">${powered_by}</div>';

	INSERT INTO "anv".dynamic_footer_header(enable, key, value)
	select true, 'DEFAULT_FOOTER_CENTER', '<div style="text-align: justify;"><span style="font-size: 12px;">${company_main_address}</span><div style="font-size: 7pt; color:#939598; line-height:1.4;"><span>${fax_num} ${phone_number} </span><span>${email_id} ${website}</span></div></div>';

	INSERT INTO "anv".dynamic_footer_header(enable, key, value)
	select true, 'DEFAULT_FOOTER_LEFT', '<div style="text-align: left;">${qr_code}</div>';

