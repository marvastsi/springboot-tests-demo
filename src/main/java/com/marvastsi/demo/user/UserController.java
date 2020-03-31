package com.marvastsi.demo.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marvastsi.demo.annotation.RestConfig;
import com.marvastsi.demo.annotation.RoleUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestConfig
@Api(tags = "User")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	@ApiOperation("Save a new User.")
	public ResponseEntity<?> create(@Valid @RequestBody UserCreateDTO user, @ApiIgnore Errors err) {
		if (!err.hasErrors()) {
			Optional<User> saved = userService.save(user);
			if (saved.isPresent()) {
				return ResponseEntity.status(HttpStatus.CREATED).body(saved.get());
			}
			throw new RuntimeException("Not Completed. An error ocurred saving entity.");
		}
		return ResponseEntity
				.badRequest()
				.body(err
						.getAllErrors()
						.stream()
						.map(msg -> msg.getDefaultMessage())
						.collect(Collectors.joining(",")));
	}

	@RoleUser
	@PutMapping
	@ApiOperation("Update an User.")
	public ResponseEntity<?> update(@Valid @RequestBody UserEditDTO user, @ApiIgnore Errors err) {
		if (!err.hasErrors()) {
			Optional<User> edited = userService.update(user);
			if (edited.isPresent()) {
				return ResponseEntity.ok(edited.get());
			}
			throw new RuntimeException("Not Completed. An error ocurred updating entity.");
		}
		return ResponseEntity
				.badRequest()
				.body(err
						.getAllErrors()
						.stream()
						.map(msg -> msg.getDefaultMessage())
						.collect(Collectors.joining(",")));
	}

	@RoleUser
	@DeleteMapping("/{id}")
	@ApiOperation("Delete an User.")
	public ResponseEntity<?> delete(@PathVariable String id) {
		userService.delete(id);
		return ResponseEntity.ok().body("Deleted succefully!");
	}

	@RoleUser
	@GetMapping("/{username}")
	@ApiOperation("Find one User.")
	public ResponseEntity<?> findOne(@PathVariable("username") String login) {
		User responseUser = null;
		Optional<User> found = userService.findByLogin(login);
		if (found.isPresent()) {
			responseUser = found.get();
		}
		return ResponseEntity.ok(responseUser);
	}

	@RoleUser
	@GetMapping
	@ApiOperation("Find all Users.")
	public ResponseEntity<List<User>> findAll() {
		List<User> list = userService.findAll();
		if (list.isEmpty()) {
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok().body(list);
	}

	@RoleUser
	@GetMapping("/findPage")
	@ApiOperation("Find Users with pagination.")
	public Page<User> findPage(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		return userService.findPage(page, size);
	}
}
