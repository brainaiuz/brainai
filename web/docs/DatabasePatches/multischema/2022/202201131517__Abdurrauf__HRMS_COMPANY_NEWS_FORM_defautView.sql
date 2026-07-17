update "90826".modelfield
set columntype = 'COL_2'
where form_id = 'HRMS_COMPANY_NEWS_FORM'
  and fsection = 'HRMS_COMPANY_NEWS'
  and field_id in ('NEWS_FULL_TEXT', 'NEWS_AUTHOR');
update "90826".modelfield
set forder = '5'
where form_id = 'HRMS_COMPANY_NEWS_FORM'
  and fsection = 'HRMS_COMPANY_NEWS'
  and field_id = 'NEWS_AUTHOR';

update "anv".modelfield
set columntype = 'COL_2'
where form_id = 'HRMS_COMPANY_NEWS_FORM'
  and fsection = 'HRMS_COMPANY_NEWS'
  and field_id in ('NEWS_FULL_TEXT', 'NEWS_AUTHOR');
update "anv".modelfield
set forder = '5'
where form_id = 'HRMS_COMPANY_NEWS_FORM'
  and fsection = 'HRMS_COMPANY_NEWS'
  and field_id = 'NEWS_AUTHOR';
