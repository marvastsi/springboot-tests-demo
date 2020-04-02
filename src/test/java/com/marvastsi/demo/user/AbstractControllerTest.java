package com.marvastsi.demo.user;

import java.io.IOException;

import org.junit.BeforeClass;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

@AutoConfigureMockMvc
@SpringBootTest
public abstract class AbstractControllerTest {
	
	public static final String AUTHORIZATION_HEADER = "Authorization";

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
	
	protected void initMocks(AbstractControllerTest self, GenericController<?> controller) {
		MockitoAnnotations.initMocks(self);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				// .addFilters(new CorsFilter())
				// .dispatchOptions(true)
				.build();
	}

	protected HttpHeaders makeHeaders(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// headers.set(CorsFilter.REQUEST_HEADER_ORIGIN, "*");
		headers.set(AUTHORIZATION_HEADER, String.format("Bearer %s", token));
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
	 * 
	 * @param login User's login
	 * @param pass  User's password
	 * 
	 */
	protected void authenticate() {
		User user = Fixture.from(User.class).gimme("login");
		this.token = authenticationUtils.authenticate(new LoginDTO(user.getLogin(), user.getPassword()));
	}

}