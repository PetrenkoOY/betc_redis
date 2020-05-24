package redis.repo;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisVariableRepository {
	private RedisTemplate<String, Object> redisTemplate;

    public RedisVariableRepository(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void save(String variable, Integer value){
    	redisTemplate.opsForValue().set(variable, value);
    }

    public Integer get(String variable){
        return (Integer) redisTemplate.opsForValue().get(variable);
    }

}
