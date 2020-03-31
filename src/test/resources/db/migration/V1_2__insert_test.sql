--
INSERT INTO public.user(id, active, created_at, updated_at, login, name, password, not_audited_property)
	VALUES ('78e7ebc7-9355-4eec-a28a-7baf5e9cf873', true, now()::timestamp without time zone, now()::timestamp without time zone, 'Aragorn@lordoftherings.com', 'Aragorn', '$2a$10$9yV3mcSM6qH86.tc3e9OsOznGfxnD2.OBv.xmW2SBAnby3ELfDJ/q', ''),--Aragorn123
		   ('ee000041-0213-439d-ab5c-3ad349606a7c', true, now()::timestamp without time zone, now()::timestamp without time zone, 'Gandalf@lordoftherings.com', 'Gandalf', '$2a$10$2QbbPZXiWMsDmvD5t5nr9eNwVLf6UunthfU7WqQFHTxEVSS9WyfrO', '');--Gandalf123
		   
INSERT INTO public.user_roles (user_id, roles)
    VALUES ('78e7ebc7-9355-4eec-a28a-7baf5e9cf873', 'ROLE_USER'),
           ('ee000041-0213-439d-ab5c-3ad349606a7c', 'ROLE_USER');