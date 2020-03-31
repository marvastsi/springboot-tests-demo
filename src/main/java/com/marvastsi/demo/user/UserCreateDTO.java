package com.marvastsi.demo.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserCreateDTO {

	@Email
	private String login;
	private String name;
	@Size(min = 8)
	private String password;
}
