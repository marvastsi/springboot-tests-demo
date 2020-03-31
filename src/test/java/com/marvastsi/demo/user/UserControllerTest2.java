package com.marvastsi.demo.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Optional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
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
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { User.class, SpringbootTestsDemoApplication.class, UserController.class })
public class UserControllerTest2 {
	protected static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	@Autowired
	private AuthenticationUtils authenticationUtils;
	private Token token;
	
	@SpyBean
	private UserService userService;
	
	@Autowired
	private MockMvc mockMvc;

	@BeforeClass
	public static void setUpClass() throws Exception {
		FlywayLoader.build().load().clean().migrate();
		FixtureFactoryLoader.loadTemplates("com.marvastsi.demo.fixture.templates");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		User user = Fixture.from(User.class).gimme("gandalfLogin");
		this.token = authenticate(user.getLogin(), user.getPassword());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void shouldReturnUserWithGivenLogin() throws Exception {
		User expected = Fixture.from(User.class).gimme("valid");
		when(userService.findByLogin(expected.getLogin())).thenReturn(Optional.of(expected));
		
		this.mockMvc.perform(
				get("/api/user/{username}", expected.getLogin())
				.header("X-Authorization", token.getToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(mapToJson(expected)));
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
	 * Perform user authentication, based on login and password.
	 * 
	 * @param login User's login
	 * @param pass  User's password
	 * @return A {@link Token} object with the user authentication JWT token.
	 */
	protected Token authenticate(String login, String pass) {
		return authenticationUtils.authenticate(new LoginDTO(login, pass));
	}

}