-- Configure jpa spring property 'ddl-auto' => spring.jpa.hibernate.ddl-auto=none | validate
----
CREATE TABLE IF NOT EXISTS public.user
(
    id VARCHAR(255) NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    password VARCHAR(255),
    login VARCHAR(255),
    name VARCHAR(255),
    active BOOLEAN,
    not_audited_property VARCHAR(255),
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT ukey_user_login UNIQUE (login)
);
--
CREATE TABLE IF NOT EXISTS public.user_roles
(
    user_id VARCHAR(255) NOT NULL,
    roles VARCHAR(255),
    CONSTRAINT fkey_user_roles_user_id FOREIGN KEY (user_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);