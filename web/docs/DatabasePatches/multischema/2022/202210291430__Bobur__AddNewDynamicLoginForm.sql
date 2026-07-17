delete
from "23039".model
where formid = 'DYNAMIC_LOGIN_FORM';
delete
from "23039".modelfield
where form_id = 'DYNAMIC_LOGIN_FORM';
delete
from "23039".customformsection
where form_id = 'DYNAMIC_LOGIN_FORM';
delete
from "23039".customlayout_new_ui
where formid = 'DYNAMIC_LOGIN_FORM';

insert into "23039".model (formid, title, viewname, active)
values ('DYNAMIC_LOGIN_FORM', 'Dynamic Login Form', 'dynamicLoginView', true);


insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('DYNAMIC_LOGIN_FORM', 'HOST_NAME', true, false, 'COL_1', 'BASIC_INFORMATION', 0);

insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('DYNAMIC_LOGIN_FORM', 'LOGO_ENABLE', true, false, 'COL_1', 'BASIC_INFORMATION', 1);


insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('DYNAMIC_LOGIN_FORM', 'LOGO_URL', true, false, 'COL_2', 'BASIC_INFORMATION', 1);

insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('DYNAMIC_LOGIN_FORM', 'FAVICON_ENABLE', true, false, 'COL_1', 'BASIC_INFORMATION', 2);

insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('DYNAMIC_LOGIN_FORM', 'FAVICON_URL', true, false, 'COL_2', 'BASIC_INFORMATION', 2);

insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('DYNAMIC_LOGIN_FORM', 'DESCRIPTION', true, false, 'COL_1', 'BASIC_INFORMATION', 3);

insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('DYNAMIC_LOGIN_FORM', 'DESCRIPTION_ENABLE', true, false, 'COL_1', 'BASIC_INFORMATION', 4);


insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('DYNAMIC_LOGIN_FORM', 'SOCIAL_LOGIN_ENABLE', true, false, 'COL_1', 'BASIC_INFORMATION', 5);

insert into "23039".customformsection (form_id, section, sorder, expanded)
values ('DYNAMIC_LOGIN_FORM', 'DETAILS', 0, true);


insert into "23039".customlayout_new_ui (active, addform, editform, formid, title, layout)
values (true, true, true, 'DYNAMIC_LOGIN_FORM', 'Custom Form Add/Edit',
        '
<div class="contentBar" style="padding:0 10px;margin-top:2px;">
    <ul class="collapsible--panels collapsible--arrows-left collapsible collapsible--gwt required"
        data-collapsible="expandable">
        <li class="slideDown-box group active">
            <div class="collapsible-header active"
                 onclick="var myclick=this.parentElement;if(myclick &amp;&amp; myclick.className);{var cls=myclick.className;if(cls.indexOf(''active'')>=0){cls=cls.replace(''active'','''');}else{cls+='' active'';}myclick.className=cls; } var myclick=this;if(myclick &amp;&amp; myclick.className);{var cls=myclick.className;if(cls.indexOf(''active'')>=0){cls=cls.replace(''active'','''');}else{cls+='' active'';}myclick.className=cls; }">
                <a style="cursor: pointer;"><span>$$label:DETAILS$$</span></a>
            </div>
            <a style="cursor: pointer;"><span>$$label:DETAILS$$</span></a>
            <div class="collapsible-body">
                <div class="grid-row">
                    <div class="col-3">
                        <div class="form-group">
                            <div class="form-group__label">$$label:HOST_NAME$$</div>
                            <div class="form-group__content">$$input:HOST_NAME$$</div>
                        </div>
                    </div>
                </div>
                <div class="grid-row">
                    <div class="col-2">
                        <div class="form-group">
                            <div class="form-group__label">$$label:LOGO_ENABLE$$</div>
                            <div class="form-group__content">$$input:LOGO_ENABLE$$</div>
                        </div>
                    </div>
                     <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:LOGO_URL$$</div>
                            <div class="form-group__content">$$input:LOGO_URL$$</div>
                        </div>
                    </div>
                </div>
                <div class="grid-row">
                    <div class="col-2">
                        <div class="form-group">
                            <div class="form-group__label">$$label:FAVICON_ENABLE$$</div>
                            <div class="form-group__content">$$input:FAVICON_ENABLE$$</div>
                        </div>
                    </div>
                     <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:FAVICON_URL$$</div>
                            <div class="form-group__content">$$input:FAVICON_URL$$</div>
                        </div>
                    </div>
                </div>
                <div class="grid-row">
                    <div class="col-2">
                        <div class="form-group">
                            <div class="form-group__label">$$label:DESCRIPTION_ENABLE$$</div>
                            <div class="form-group__content">$$input:DESCRIPTION_ENABLE$$</div>
                        </div>
                    </div>
                </div>
                <div class="grid-row">
                    <div class="col-10">
                        <div class="form-group">
                            <div class="form-group__label">$$label:DESCRIPTION$$</div>
                            <div class="form-group__content">$$input:DESCRIPTION$$</div>
                        </div>
                    </div>
                </div>
                <div class="grid-row">
                    <div class="col-2">
                        <div class="form-group">
                            <div class="form-group__label">$$label:SOCIAL_LOGIN_ENABLE$$</div>
                            <div class="form-group__content">$$input:SOCIAL_LOGIN_ENABLE$$</div>
                        </div>
                    </div>
                    <div class="col-2">
                        <div class="form-group">
                            <div class="form-group__label">$$label:FORGOT_PASSWORD_ENABLE$$</div>
                            <div class="form-group__content">$$input:FORGOT_PASSWORD_ENABLE$$</div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="form-group">
                            <div class="form-group__label">$$label:SIGNUP_ENABLE$$</div>
                            <div class="form-group__content">$$input:SIGNUP_ENABLE$$</div>
                        </div>
                    </div>
                </div>
            </div>
        </li>
    </ul>
</div>');