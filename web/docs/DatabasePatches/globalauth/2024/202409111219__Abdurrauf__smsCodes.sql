CREATE TABLE sms_verification_codes
(
    id                SERIAL PRIMARY KEY,
    user_id           INT         NOT NULL,
    company_id        INT         NOT NULL,
    phone_number      VARCHAR(15) NOT NULL,
    verification_code VARCHAR(6)  NOT NULL,
    request_count     INT       DEFAULT 0,
    last_request_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at        TIMESTAMP   NOT NULL,
    is_verified       BOOLEAN   DEFAULT FALSE
);

ALTER TABLE sms_verification_codes
    ADD CONSTRAINT unique_phone_company UNIQUE (phone_number, company_id);
