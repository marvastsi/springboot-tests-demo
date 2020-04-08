package com.marvastsi.demo.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.marvastsi.demo.SpringbootTestsDemoApplication;
import com.marvastsi.demo.fixture.functions.PasswordEnconderFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.six2six.fixturefactory.Fixture;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class UserControllerTest extends AbstractControllerTest {

	@SpyBean
	private UserService userServiceMock;
	
	@Autowired
	private UserController userController;

	@BeforeEach
	public void setUp() throws Exception {
		initMocks(this, userController);
		authenticate();
	}

	@Test
	@DisplayName("Should Create an User")
	public void shouldCreateAnUser() throws Exception {
		UserCreateDTO dto = new UserCreateDTO();
		dto.setLogin("Samwise@lordoftherings.com");
		dto.setName("Samwise");
		dto.setPassword("Samwise123");
		User expected = new User();
		expected.setLogin("Samwise@lordoftherings.com");
		expected.setName("Samwise");
		expected.setPassword(new PasswordEnconderFunction("Samwise123").generateValue());
		expected.setActive(true);
		when(userServiceMock.save(dto)).thenReturn(Optional.of(expected));
		
		this.mockMvc.perform(
				 post("/api/user")
				 .accept(MediaType.APPLICATION_JSON)
				.headers(makeHeaders())
				.content(mapToJson(dto))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.login").value(expected.getLogin()))
				.andExpect(jsonPath("$.name").value(expected.getName()))
				.andExpect(jsonPath("$.active").value(expected.isActive()));
		
		verify(userServiceMock, times(1)).findByLogin(expected.getLogin());
        verifyNoMoreInteractions(userServiceMock);
	}
	
	@Test
	@DisplayName("Should Return a Single User With Given Login")
	public void shouldReturnUserWithGivenLogin() throws Exception {
		User expected = Fixture.from(User.class).gimme("valid");
		when(userServiceMock.findByLogin(expected.getLogin())).thenReturn(Optional.of(expected));
		
		this.mockMvc.perform(
				 get("/api/user/{username}", expected.getLogin())
				.headers(makeHeaders()))
				.andDo(print())
				.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
				.andExpect(content().json(mapToJson(expected)));
		
		verify(userServiceMock, atLeastOnce()).findByLogin(expected.getLogin());
        verifyNoMoreInteractions(userServiceMock);
	}
	
	@Test
	@DisplayName("Should Return List of All Users")
	public void shouldReturnAllUsers() throws Exception {
		List<User> expected = Fixture.from(User.class).gimme(2, "valid");
		when(userServiceMock.findAll()).thenReturn(expected);
		
		this.mockMvc.perform(
				get("/api/user")
				.headers(makeHeaders()))
				.andDo(print())
				.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(expected.get(0).getId())))
                .andExpect(jsonPath("$[0].login", is(expected.get(0).getLogin())))
				.andExpect(jsonPath("$[0].name", is(expected.get(0).getName())))
				.andExpect(jsonPath("$[0].active", is(expected.get(0).isActive())))
				.andExpect(jsonPath("$[1].id", is(expected.get(0).getId())))
				.andExpect(jsonPath("$[1].login", is(expected.get(0).getLogin())))
				.andExpect(jsonPath("$[1].name", is(expected.get(0).getName())))
				.andExpect(jsonPath("$[1].active", is(expected.get(0).isActive())));
		
		verify(userServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(userServiceMock);
	}

}