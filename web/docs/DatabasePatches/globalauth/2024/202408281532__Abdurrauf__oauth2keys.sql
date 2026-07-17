CREATE TABLE oauth_tokens
(
    id            SERIAL PRIMARY KEY,
    access_token  TEXT NOT NULL,
    refresh_token TEXT,
    token_type    VARCHAR(50),
    expires_at    TIMESTAMPTZ,
    scope         TEXT,
    email         TEXT,
    user_id       VARCHAR(50)
);
