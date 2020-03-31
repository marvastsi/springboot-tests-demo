--
INSERT INTO public.user(id, active, created_at, updated_at, login, name, password, not_audited_property)
	VALUES ('78e7ebc7-9355-4eec-a28a-7baf5e9cf873', true, now()::timestamp without time zone, now()::timestamp without time zone, 'aragorn@lordoftherings.com', 'Aragorn', '$2a$10$1KMGhzuwG2TOoN2Q8WuV9eLbF4mGsWSpt/i9bvgBozceDJwrwhJ4e', ''),--aragorn123
		   ('ee000041-0213-439d-ab5c-3ad349606a7c', true, now()::timestamp without time zone, now()::timestamp without time zone, 'gandalf@lordoftherings.com', 'Gandalf', '$2a$10$DcykksNntM14aAc/RHzXjevOh2ESiErXjh0Mo8.fBA.z8WUjZvdVW', '');--gandalf123
		   
INSERT INTO public.user_roles (user_id, roles)
    VALUES ('78e7ebc7-9355-4eec-a28a-7baf5e9cf873', 'ROLE_USER'),
           ('ee000041-0213-439d-ab5c-3ad349606a7c', 'ROLE_USER');