package com.marvastsi.demo.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.marvastsi.demo.SpringbootTestsDemoApplication;

import br.com.six2six.fixturefactory.Fixture;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { User.class, SpringbootTestsDemoApplication.class, UserController.class })
public class UserControllerTest extends AbstractControllerTest {
	protected static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	@SpyBean
	private UserService userService;

	@Before
	public void setUp() throws Exception {
		authenticate();
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

}