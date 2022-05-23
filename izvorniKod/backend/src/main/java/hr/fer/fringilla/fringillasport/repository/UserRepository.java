package hr.fer.fringilla.fringillasport.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import hr.fer.fringilla.fringillasport.domain.User;

@Transactional
public interface UserRepository extends UserBaseRepository<User> {
	Optional<User> findByUsername(String username);
}
