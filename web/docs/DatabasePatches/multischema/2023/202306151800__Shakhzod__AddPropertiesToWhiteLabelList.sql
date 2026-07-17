insert into "23039".model (formid, title, viewname, active)
values ('WHITE_LABEL_FORM', 'White Label Form', 'whiteLabelView', true);


insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('WHITE_LABEL_FORM', 'PRODUCT_NAME', true, false, 'COL_1', 'BASIC_INFORMATION', 1);

insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('WHITE_LABEL_FORM', 'LOGO_URL', true, false, 'COL_1', 'BASIC_INFORMATION', 2);

insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('WHITE_LABEL_FORM', 'HOST_NAME', true, false, 'COL_1', 'BASIC_INFORMATION', 0);

insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('WHITE_LABEL_FORM', 'EMAIL', true, false, 'COL_1', 'BASIC_INFORMATION', 3);

insert into "23039".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('WHITE_LABEL_FORM', 'WEBSITE', true, false, 'COL_1', 'BASIC_INFORMATION', 4);



insert into "23039".customformsection (form_id, section, sorder, expanded)
values ('WHITE_LABEL_FORM', 'DETAILS', 0, true);



insert into "23039".customlayout_new_ui (active, addform, editform, formid, title, layout)
values (true, true, true, 'WHITE_LABEL_FORM', 'Custom Form Add/Edit',
        '<div class="contentBar" style="padding:0 10px;margin-top:2px;">

            <ul class="collapsible--panels collapsible--arrows-left collapsible collapsible--gwt required"
                data-collapsible="expandable">
                <li class="slideDown-box group active">
                    <div class="collapsible-header active"
                         onclick="var myclick=this.parentElement;if(myclick &amp;&amp; myclick.className);{var cls=myclick.className;if(cls.indexOf(''''''''''''''''active'''''''''''''''')>=0){cls=cls.replace(''''''''''''''''active'''''''''''''''','''''''''''''''''''''''''''''''');}else{cls+=''''''''''''''''active'''''''''''''''';}myclick.className=cls; } var myclick=this;if(myclick &amp;&amp; myclick.className);{var cls=myclick.className;if(cls.indexOf(''''''''''''''''active'''''''''''''''')>=0){cls=cls.replace(''''''''''''''''active'''''''''''''''','''''''''''''''''''''''''''''''');}else{cls+='''''''''''''''' active'''''''''''''''';}myclick.className=cls; }">
                <a style="cursor: pointer;"><span>$$label:DETAILS$$</span></a>
            </div>
            <a style="cursor: pointer;"><span>$$label:DETAILS$$</span></a>
            <div class="collapsible-body">
                <div class="grid-row">
                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:PRODUCT_NAME$$</div>
                            <div class="form-group__content">$$input:PRODUCT_NAME$$</div>
                        </div>
                    </div>
                	<div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:LOGO_URL$$</div>
                            <div class="form-group__content">$$input:LOGO_URL$$</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:EMAIL$$</div>
                            <div class="form-group__content">$$input:EMAIL$$</div>
                        </div>
                    </div>
                <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:WEBSITE$$</div>
                            <div class="form-group__content">$$input:WEBSITE$$</div>
                        </div>
                    </div>
            </div>
            </div>
        </li>
    </ul>
</div>');
