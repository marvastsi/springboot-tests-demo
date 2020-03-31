CREATE SCHEMA IF NOT EXISTS envers_audit;
----
----
-- SEQUENCE: public.hibernate_sequence
-- DROP SEQUENCE public.hibernate_sequence;
CREATE SEQUENCE public.hibernate_sequence
    INCREMENT 1
    START 20
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
--
CREATE TABLE IF NOT EXISTS envers_audit.revinfo
(
    rev INTEGER NOT NULL,
    revtstmp bigint,
    CONSTRAINT revinfo_pkey PRIMARY KEY (rev)
);
----
CREATE TABLE IF NOT EXISTS envers_audit.user_audit
(
    id VARCHAR(255) NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    password VARCHAR(255),
    login VARCHAR(255),
    name VARCHAR(255),
    active BOOLEAN,
    CONSTRAINT user_audit_pkey PRIMARY KEY (id, rev),
    CONSTRAINT fkey_user_audit_revinfo_rev FOREIGN KEY (rev)
        REFERENCES envers_audit.revinfo (rev)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);