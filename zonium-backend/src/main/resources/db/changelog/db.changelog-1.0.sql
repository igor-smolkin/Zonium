--liquibase formatted sql

--changeset ataraxii:1
CREATE TABLE proxy
(
    id         BIGSERIAL PRIMARY KEY,
    name       varchar(64) NOT NULL,
    host       varchar(64) NOT NULL,
    port       int         NOT NULL,
    username   varchar(64),
    password   varchar(64),
    created_at timestamp DEFAULT now()
);

CREATE TABLE browser_session
(
    id             BIGSERIAL PRIMARY KEY,
    proxy_id       BIGINT      NOT NULL REFERENCES proxy (id),
    fingerprint    TEXT,
    user_agent     VARCHAR(512),
    status         VARCHAR(20) NOT NULL,
    created_at     TIMESTAMP DEFAULT now()
);