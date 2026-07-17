delete
from "anv".customlayout_new_ui
where formid = 'BRAND_FORM';

insert into "anv".customlayout_new_ui (active, addform, editform, formid, title, layout)
values (true, true, true, 'BRAND_FORM', 'Custom Form Add/Edit',
        '<div class="contentBar" style="padding:0 10px;margin-top:2px;">
    <ul class="collapsible--panels collapsible--arrows-left collapsible collapsible--gwt required"
        data-collapsible="expandable">
        <li class="slideDown-box group active">
            <div class="collapsible-header active"
                 onclick="var myclick=this.parentElement;if(myclick &amp;&amp; myclick.className);{var cls=myclick.className;if(cls.indexOf(''active'')>=0){cls=cls.replace(''active'','''');}else{cls+='' active'';}myclick.className=cls; } var myclick=this;if(myclick &amp;&amp; myclick.className);{var cls=myclick.className;if(cls.indexOf(''active'')>=0){cls=cls.replace(''active'','''');}else{cls+='' active'';}myclick.className=cls; }">
                <a style="cursor: pointer;"><span>$$label:TITLE$$</span></a>
            </div>
            <a style="cursor: pointer;"><span>$$label:TITLE$$</span></a>
            <div class="collapsible-body">
                <div class="grid-row">
                    <div class="col-2">
                        <div class="form-group">
                            <div class="form-group__label">$$label:NAME$$</div>
                            <div class="form-group__content">$$input:NAME$$</div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-group">
                            <div class="form-group__label">$$label:PARENT$$</div>
                            <div class="form-group__content">$$input:PARENT$$</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <div class="form-group__label">$$label:IMAGE_PANEL$$</div>
                            <div class="form-group__content">$$input:IMAGE_PANEL$$</div>
                        </div>
                    </div>
                </div>
                <div class="grid-row">

                    <div class="col-4">
                        <div class="form-group">
                            <div class="form-group__label">$$label:DESCRIPTION$$</div>
                            <div class="form-group__content">$$input:DESCRIPTION$$</div>
                        </div>
                    </div>
                </div>
            </div>
        </li>
    </ul>
</div>');