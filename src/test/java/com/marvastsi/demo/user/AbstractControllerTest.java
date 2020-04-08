package com.marvastsi.demo.user;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvastsi.demo.auth.AuthenticationUtils;
import com.marvastsi.demo.auth.LoginDTO;
import com.marvastsi.demo.auth.Token;
import com.marvastsi.demo.base.GenericController;
import com.marvastsi.demo.flyway.FlywayLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	
	protected Token token;

	@Autowired
	private AuthenticationUtils authenticationUtils;
	
	@Autowired
	protected MockMvc mockMvc;

	@BeforeAll
	public static void setUpClass() throws Exception {
		FlywayLoader.build().load().clean().migrate();
		FixtureFactoryLoader.loadTemplates("com.marvastsi.demo.fixture.templates");
	}
	
	protected void initMocks(AbstractControllerTest self, GenericController<?> ...controllers) {
		MockitoAnnotations.initMocks(self);
		mockMvc = MockMvcBuilders.standaloneSetup(controllers)
				// .addFilters(new CorsFilter())
				// .dispatchOptions(true)
				.build();
	}

	protected HttpHeaders makeHeaders() {
		HttpHeaders headers = new HttpHeaders();
		// headers.set(CorsFilter.REQUEST_HEADER_ORIGIN, "*");
		headers.set(AUTHORIZATION_HEADER, String.format("Bearer %s", token.getToken()));
		return headers;
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
	 * @param login User's login
	 * @param pass  User's password
	 */
	protected void authenticate() {
		User user = Fixture.from(User.class).gimme("login");
		this.token = authenticationUtils.authenticate(new LoginDTO(user.getLogin(), user.getPassword()));
	}

}