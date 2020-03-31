package com.marvastsi.demo.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.NotAudited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.marvastsi.demo.base.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = { "roles" }, allowGetters = true)
public abstract class UserAuth extends BaseEntity implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@NotBlank
	@Size(min = 8)
	@JsonIgnore
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@Email
	@NotNull
	@Column(name = "login", unique = true, updatable = false)
	private String login;

	@NotAudited
	@JsonIgnore
	@ElementCollection(fetch = FetchType.EAGER)
	@ApiModelProperty(hidden = true)
	private Set<String> roles = new HashSet<>();

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return this.getLogin();
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return false;
	}

}
