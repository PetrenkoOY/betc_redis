package redis.repo;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.stereotype.Repository;

@Repository
public class RedisVariableRepository {
	private RedisTemplate<String, Integer> redisTemplate;

    public RedisVariableRepository(RedisTemplate<String, Integer> redisTemplate){
    	redisTemplate.setValueSerializer( new GenericToStringSerializer<Integer>( Integer.class ) );
        this.redisTemplate = redisTemplate;
    }

    public void save(String variable, Integer value){
    	redisTemplate.opsForValue().set(variable, value);
    }

    public Integer get(String variable){
        return redisTemplate.opsForValue().get(variable);
    }

}
