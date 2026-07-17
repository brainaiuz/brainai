update "0".modelfield set mandatory = false where section='CRM_ACCOUNT_PRODUCT_SERVICE';
update "anv".modelfield set mandatory = false where section='CRM_ACCOUNT_PRODUCT_SERVICE';
update modelfield set mandatory = false where section='CRM_ACCOUNT_PRODUCT_SERVICE';

update "anv".customformsection set active = false where section='CRM_ACCOUNT_PRODUCT_SERVICE';
update "0".customformsection set active = false where section='CRM_ACCOUNT_PRODUCT_SERVICE';
