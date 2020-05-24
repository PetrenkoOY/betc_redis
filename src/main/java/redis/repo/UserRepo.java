package redis.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import redis.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	List<User> findByIdGreaterThan(int id);
}
