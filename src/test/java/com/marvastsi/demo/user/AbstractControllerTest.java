package com.marvastsi.demo.user;

import java.io.IOException;

import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvastsi.demo.SpringbootTestsDemoApplication;
import com.marvastsi.demo.auth.AuthenticationUtils;
import com.marvastsi.demo.auth.LoginDTO;
import com.marvastsi.demo.auth.Token;
import com.marvastsi.demo.flyway.FlywayLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@AutoConfigureMockMvc
@ContextConfiguration(classes = SpringbootTestsDemoApplication.class)
public abstract class AbstractControllerTest {
	protected static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	@Autowired
	private AuthenticationUtils authenticationUtils;
	
	protected Token token;
	
	@Autowired
	protected MockMvc mockMvc;

	@BeforeClass
	public static void setUpClass() throws Exception {
		FlywayLoader.build().load().clean().migrate();
		FixtureFactoryLoader.loadTemplates("com.marvastsi.demo.fixture.templates");
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	/**
	 * Perform user authentication and stores generated token in the property {@link #token}
	 * 
	 * @param login User's login
	 * @param pass  User's password
	 * 
	 */
	protected void authenticate() {
		User user = Fixture.from(User.class).gimme("gandalfLogin");
		this.token = authenticationUtils.authenticate(new LoginDTO(user.getLogin(), user.getPassword()));
	}

}