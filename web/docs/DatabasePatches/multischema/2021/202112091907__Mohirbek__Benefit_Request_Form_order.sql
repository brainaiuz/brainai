update "anv".modelfield set columntype = 'COL_3', forder = 1 where form_id = 'BENEFIT_REQUEST_FORM'  and field_id = 'DESCRIPTION';
update "anv".modelfield set columntype = 'COL_3', forder = 0 where form_id = 'BENEFIT_REQUEST_FORM'  and field_id = 'APPROVER';
update "anv".modelfield set columntype = 'COL_1', forder = 0 where form_id = 'BENEFIT_REQUEST_FORM'  and field_id = 'REQUESTER';
update "anv".modelfield set columntype = 'COL_1', forder = 1 where form_id = 'BENEFIT_REQUEST_FORM'  and field_id = 'DATE_PERIOD';
update "anv".modelfield set columntype = 'COL_2', forder = 2 where form_id = 'BENEFIT_REQUEST_FORM'  and field_id = 'LEFT_QUANTITY';
update "anv".modelfield set columntype = 'COL_2', forder = 0 where form_id = 'BENEFIT_REQUEST_FORM'  and field_id = 'BENEFIT_TYPE';
update "anv".modelfield set columntype = 'COL_2', forder = 1 where form_id = 'BENEFIT_REQUEST_FORM'  and field_id = 'REQUESTED_QUANTITY';