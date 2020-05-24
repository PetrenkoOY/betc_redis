package redis.repo;

import java.util.List;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import redis.entity.User;

@Repository
public class RedisUserRepository {
    private static final String USER = "USER";
	private HashOperations<String, Integer, User> hashOperations;
    private RedisTemplate<String, User> redisTemplate;

    public RedisUserRepository(RedisTemplate<String, User> redisTemplate){
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public void save(User user){
        hashOperations.put(USER, user.getId(), user);
    }
    public List<User> findAll(){
        return hashOperations.values(USER);
    }

    public User findById(String id){
        return (User) hashOperations.get(USER, id);
    }

    public void delete(String id){
        hashOperations.delete(USER, id);
    }

}
