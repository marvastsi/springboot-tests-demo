package com.marvastsi.demo.user;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.marvastsi.demo.auth.role.UserRole;
import com.marvastsi.demo.exception.NotFoundException;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	public Optional<User> save(UserCreateDTO dto) {
		User user = new User();
		user.setLogin(dto.getLogin());
		user.setName(dto.getName());
		String password = bcrypt.encode(dto.getPassword());
		user.setPassword(password);
		user.setActive(true);
		user.getRoles().add(UserRole.ROLE_USER.name());

		User saved = userRepository.save(user);

		return saved != null ? Optional.of(saved) : Optional.empty();
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<User> findByLogin(String login) {
		return userRepository.findByLogin(login);
	}

	public Optional<User> update(UserEditDTO dto) {
		Optional<User> optUser = userRepository.findById(dto.getId());
		if (optUser.isEmpty()) {
			throw new NotFoundException("User not found with ID: " + dto.getId());
		}
		User found = optUser.get();
		found.setName(dto.getName());
		found.setPassword(bcrypt.encode(dto.getPassword()));

		User edited = userRepository.save(found);

		return edited != null ? Optional.of(edited) : Optional.empty();
	}

	public void delete(String id) {
		Optional<User> optUser = userRepository.findById(id);
		if (optUser.isEmpty()) {
			throw new NotFoundException("User not found with ID: " + id);
		}
		User found = optUser.get();
		found.setActive(false);
		userRepository.save(found);
	}

	public Page<User> findPage(int page, int size) {
		PageRequest pageRequest = PageRequest.of(
				page,
				size,
				Sort.Direction.ASC,
				new String[] { "name", "createdAt" });
		return userRepository.findPage(pageRequest);
	}
}
