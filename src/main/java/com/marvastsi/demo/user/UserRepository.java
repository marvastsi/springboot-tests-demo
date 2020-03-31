package com.marvastsi.demo.user;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByLogin(String login);

	@Query("FROM User u WHERE u.active = true")
	Page<User> findPage(Pageable pageable);
}
