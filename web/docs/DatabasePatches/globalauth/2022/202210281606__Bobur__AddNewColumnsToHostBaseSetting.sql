ALTER TABLE hostbasedsetting
    ADD COLUMN is_dynamic_login_active BOOLEAN DEFAULT false,
    ADD COLUMN logo_enable BOOLEAN DEFAULT true,
    ADD COLUMN logo_url VARCHAR DEFAULT '../../mainStyles/new-ui/images/new-kpi-logo.svg',
    ADD COLUMN favicon_enable BOOLEAN DEFAULT true,
    ADD COLUMN favicon_url VARCHAR DEFAULT '/mainStyles/new-ui/login/img/favicon.ico?v=2',
    ADD COLUMN description_enable BOOLEAN DEFAULT true,
    ADD COLUMN description TEXT DEFAULT 'Manage your business from <br> a single platform.',
    ADD COLUMN social_login_enable BOOLEAN DEFAULT true,
    ADD COLUMN forgot_password_enable BOOLEAN DEFAULT true,
    ADD COLUMN sign_up_enable BOOLEAN DEFAULT true;
