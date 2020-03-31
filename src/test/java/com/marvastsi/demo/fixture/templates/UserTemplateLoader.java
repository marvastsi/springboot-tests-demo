package com.marvastsi.demo.fixture.templates;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import com.marvastsi.demo.fixture.functions.PasswordEnconderFunction;
import com.marvastsi.demo.user.User;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class UserTemplateLoader implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(User.class).addTemplate("gandalfLogin", new Rule() {
			{
				add("id", "ee000041-0213-439d-ab5c-3ad349606a7c");
				add("name", "Gandalf");
				add("active", true);
				add("login", "${name}@lordoftherings.com");
				add("password", "${name}123");
				add("roles", new HashSet<>(Arrays.asList("ROLE_USER")));
			}
		}).addTemplate("valid", new Rule() {
			{
				add("id", UUID.randomUUID().toString());
				add("name", random("Smigle", "Gimli", "Legolas"));
				add("active", true);
				add("login", "${name}@lordoftherings.com");
				add("createdAt", instant("2020-03-01T12:10:55.638"));
				add("updatedAt", instant("2020-03-01T12:10:55.638"));
				add("password", new PasswordEnconderFunction("${name}123"));
				add("roles", new HashSet<>(Arrays.asList("ROLE_USER")));
			}
		});
	}

}