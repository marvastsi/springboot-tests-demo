package com.marvastsi.demo.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserEditDTO {

	@NotNull
	private String id;
	private String name;
	@Size(min = 8)
	private String password;
}
