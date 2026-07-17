insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('WHITE_LABEL_FORM', 'SHOW_WIKI', false, false, 'COL_1', 'BASIC_INFORMATION', 8);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('WHITE_LABEL_FORM', 'SHOW_APP_LINKS', false, false, 'COL_1', 'BASIC_INFORMATION', 9);


update "anv".customlayout_new_ui set layout = '<<div class="contentBar" style="padding:0 10px;margin-top:2px;">

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
                            <div class="form-group__label">$$label:FAVICON_URL$$</div>
                            <div class="form-group__content">$$input:FAVICON_URL$$</div>
                        </div>
                    </div>

                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:WEBSITE$$</div>
                            <div class="form-group__content">$$input:WEBSITE$$</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:ANDROID$$</div>
                            <div class="form-group__content">$$input:ANDROID$$</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:IOS$$</div>
                            <div class="form-group__content">$$input:IOS$$</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:DESCRIPTION$$</div>
                            <div class="form-group__content">$$input:DESCRIPTION$$</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:FREETRIALDAYS$$</div>
                            <div class="form-group__content">$$input:FREETRIALDAYS$$</div>
                        </div>
                    </div>

                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:OPENAI$$</div>
                            <div class="form-group__content">$$input:OPENAI$$</div>
                        </div>
                    </div>
                      <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:SCHEDULE_DEMO_URL$$</div>
                            <div class="form-group__content">$$input:SCHEDULE_DEMO_URL$$</div>
                        </div>
                    </div>
                     <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:PHONE_NUMBER$$</div>
                            <div class="form-group__content">$$input:PHONE_NUMBER$$</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:SHOW_PHONE_NUMBER$$</div>
                            <div class="form-group__content">$$input:SHOW_PHONE_NUMBER$$</div>
                        </div>
                    </div>
                     <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:SHOW_SCHEDULE_DEMO$$</div>
                            <div class="form-group__content">$$input:SHOW_SCHEDULE_DEMO$$</div>
                        </div>
                    </div>
                     <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:SHOW_WIKI$$</div>
                            <div class="form-group__content">$$input:SHOW_WIKI$$</div>
                        </div>
                    </div>
                     <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:SHOW_APP_LINKS$$</div>
                            <div class="form-group__content">$$input:SHOW_APP_LINKS$$</div>
                        </div>
                    </div>

                </div>
            </div>
        </li>
    </ul>
</div>' where formid = 'WHITE_LABEL_FORM';
