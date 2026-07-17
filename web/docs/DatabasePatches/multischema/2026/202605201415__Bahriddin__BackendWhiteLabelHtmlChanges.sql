
delete
from "anv".customlayout_new_ui
where formid = 'WHITE_LABEL_FORM';

insert into "anv".customlayout_new_ui (active, addform, editform, formid, title, layout)
values (true, true, true, 'WHITE_LABEL_FORM', 'Custom Form Add/Edit',
        '<<div class="contentBar" style="padding:0 10px;margin-top:2px;">

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

                </div>
            </div>
        </li>
    </ul>
</div>');
